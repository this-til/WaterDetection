import _thread

import uwebsocket
from machine import UART
import time
import ucollections
import ubinascii
import ujson
from uio import StringIO
from misc import Power
from queue import Queue
import gc
import utime
from machine import Pin
import usys as sys

header = b'\xAA\xAA\xAA'
footer = b'\xFF\xFF\xFF'

SERVER = 0x01
CLIENT = 0x02
SCREEN = 0x03
_4G = 0x04

URL = 0x01
USERNAME = 0x02
PASSWORD = 0x03
EQUIPMENT = 0x04
RULE = 0x05

DATA_TYPE = 0x01
ACTUATOR = 0x02
DATA_TYPE_LIST = 0x03
ACTUATOR_LIST = 0x04

WRITE = 0x01
READ = 0x02
GET = 0x03
INIT_END = 0x04
SYNC_END = 0x05

ORDER = 0x01
ANSWER_BACK = 0x02

SUCCESSFUL = 0x01
FAIL = 0x02
EXCEPTION = 0x03

wdt = None

url: str = ''
username: str = ''
password: str = ''
equipment: str = ''
dataNameList: list[str] = list()
actuatorNameList: list[str] = list()

b_dataNameList: list[bytes] = list()

isDebug = True
isConnectionTest = False

uart_client = UART(UART.UART2, 9600, 8, 0, 1, 0)
uart_screen = UART(UART.UART1, 9600, 8, 0, 1, 0)
c_cache = bytearray()
s_cache = bytearray()

cli = None
heartbeatTime = 0

_idAdd: int = 0

toBeSentToClient = Queue(32)
toBeSentToServer = Queue(32)
toBeSentToScreen = Queue(32)

planExecution = Queue(4)


class CommandCallback:
    command: bytes = None
    cId: int = None
    to: int = None

    successCallback = None
    failCallback = None
    exceptionCallback = None
    outTime = None

    sendTime: int

    def __init__(self, command: bytearray, to, successCallback, failCallback, exceptionCallback, outTime):
        self.to = to

        pack = bytearray()
        pack.extend(header)
        pack.append(_4G)
        pack.append(to)
        pack.append(ORDER)

        global _idAdd
        _idAdd += 1
        self.cId = _idAdd

        addInt32ToBytes(self.cId, pack)

        pack.extend(command)
        pack.extend(footer)

        self.command = bytes(pack)

        self.successCallback = successCallback
        self.failCallback = failCallback
        self.exceptionCallback = exceptionCallback
        self.outTime = outTime

        self.sendTime = utime.time()

        pass


class WatchDog:

    def __init__(self, max_count):
        self.__max_count = max_count
        self.__count = self.__max_count
        self.__tid = None
        pass

    def feed(self):
        if isDebug:
            print('WatchDog feed')
        self.__count = self.__max_count  # 喂狗，重置看门狗计数器

    def __check(self):
        while True:  # 循环中检查计数器
            if (self.__count == 0):
                if isDebug:
                    print("==CHECK==")
                bark()  # 计数器归零时，触发重启
            else:
                self.__count = (self.__count - 1)  # 否则计数器减一

            utime.sleep(10)

    def start(self):
        if not self.__tid or (self.__tid and not _thread.threadIsRunning(self.__tid)):
            try:
                # _thread.stack_size(0x1000)
                self.__tid = _thread.start_new_thread(self.__check, ())

            except Exception as e:
                sys.print_exception(e)

    def stop(self):
        if self.__tid:
            try:
                _thread.stop_thread(self.__tid)
            except:
                pass

        self.__tid = None


commandCallbackList: list[CommandCallback] = list()
commandCallbackListLock = _thread.allocate_lock()


def init():
    global _idAdd
    _idAdd = 0

    commandCallbackListLock.acquire()
    commandCallbackList.clear()
    commandCallbackListLock.release()

    com = bytearray()
    com.append(0x00)
    com.append(0x00)

    com[0] = READ
    com[1] = URL
    down(CommandCallback(com, CLIENT, w_url, None, None, None))

    com[1] = USERNAME
    down(CommandCallback(com, CLIENT, w_username, None, None, None))

    com[1] = PASSWORD
    down(CommandCallback(com, CLIENT, w_password, None, None, None))

    com[1] = EQUIPMENT
    down(CommandCallback(com, CLIENT, w_equipment, None, None, None))

    com[0] = GET
    com[1] = DATA_TYPE_LIST
    down(CommandCallback(com, CLIENT, w_dataTypeList, None, None, None))

    com[1] = ACTUATOR_LIST
    down(CommandCallback(com, CLIENT, w_actuatorList, None, None, None))

    com = bytearray()
    com.append(INIT_END)
    down(CommandCallback(com, CLIENT, w_end, None, None, None))

    pass


def w_url(p: bytes):
    global url
    url = p[0:-1].decode('')
    if isDebug:
        print("url", url)
    pass


def w_username(p: bytes):
    global username
    username = p[0:-1].decode('')
    if isDebug:
        print("username", username)
    pass


def w_password(p: bytes):
    global password
    password = p[0:-1].decode('')
    if isDebug:
        print("password", password)
    pass


def w_equipment(p: bytes):
    global equipment
    equipment = p[0:-1].decode()
    if isDebug:
        print("equipment", equipment)
    pass


def w_dataTypeList(p: bytes):
    global dataNameList
    dataNameList.clear()
    for i, e in enumerate(p[4:].split(b'\0')):
        if len(e) is not 0:
            dataNameList.append(e.decode())
            b = bytearray()
            b.append(GET)
            b.append(DATA_TYPE)
            addInt32ToBytes(i, b)
            b_dataNameList.append(bytes(b))
    if isDebug:
        print("dataNameList", dataNameList)
    pass


def w_actuatorList(p: bytes):
    global actuatorNameList
    actuatorNameList.clear()
    for e in p[4:].split(b'\0'):
        if len(e) is not 0:
            actuatorNameList.append(e.decode())
    if isDebug:
        print("actuatorNameList", actuatorNameList)
    pass


def w_end(p: bytes):
    planExecution.put(lambda: connect())
    pass


def connect():
    global cli

    if isDebug:
        print("--------------------------------------------------------")

    if len(url) == 0 or len(username) == 0 or len(password) == 0 or len(equipment) == 0 or len(
            dataNameList) == 0 or len(actuatorNameList) == 0:
        print("ERROR")
        return

    _dataNameList: list[str] = []
    for e in dataNameList:
        _dataNameList.append("\"" + e + "\"")

    _actuatorNameList: list[str] = []
    for e in actuatorNameList:
        _actuatorNameList.append("\"" + e + "\"")
    json = ("""{"username":"%s","password":"%s","equipment":"%s","dataNameList":[%s],"actuatorNameList":[%s]}""" % (
        username,
        password,
        equipment,
        ','.join(_dataNameList),
        ','.join(_actuatorNameList)))

    if isDebug:
        print("json", json)

    _loginData = ubinascii.b2a_base64(json.encode()).decode()[0:-1]

    _url = url + "?" + "loginData=" + _loginData

    if isDebug:
        print("uwebsocket url", _url)

    try:
        cli = uwebsocket.Client.connect(_url, debug=isDebug)
    except Exception as e:
        print("CONNECTION FAILURE", e)
        return

    if isDebug:
        print("--------------------------------------------------------")


pass


def uwebsocketMonitoring():
    global cli
    while True:
        if cli is None:
            time.sleep(0.5)
            continue
        try:
            recv_data = cli.recv()
        except Exception as e:
            print("RECEIVE FAILURE", e)
            cli = None
            continue
        if recv_data is None:
            continue
        if not (isinstance(recv_data, bytes)):
            recv_data = bytes(recv_data)

        if isDebug:
            print("cli.recv()", recv_data)

        global heartbeatTime
        heartbeatTime = time.time()

        pack = extractDataBetweenHeaders(recv_data, header, footer)
        if pack is None:
            continue
        forwardData(pack[0])
    pass


# 串口监听
def clientSerialMonitoring():
    while True:
        time.sleep(0.5)
        any = uart_client.any()
        if any == 0:
            continue
        pack = uart_client.read(any)

        if isDebug:
            print("uart_client.read()", pack)

        global c_cache
        c_cache.extend(pack)

        pack = extractDataBetweenHeaders(bytes(c_cache), header, footer)
        while pack is not None:
            forwardData(pack[0])
            c_cache = c_cache[pack[1]:]
            pack = extractDataBetweenHeaders(bytes(c_cache), header, footer)

        if len(c_cache) > 256:
            c_cache = c_cache[len(c_cache) - 256:]
    pass


def screenSerialMonitoring():
    while True:
        time.sleep(0.5)
        any = uart_screen.any()
        if any == 0:
            continue
        pack = uart_screen.read(any)

        if isDebug:
            print("uart_screen.read()", pack)

        global s_cache
        s_cache.extend(pack)

        pack = extractDataBetweenHeaders(bytes(s_cache), header, footer)
        while pack is not None:
            forwardData(pack[0])
            s_cache = s_cache[pack[1]:]
            pack = extractDataBetweenHeaders(bytes(s_cache), header, footer)

        if len(s_cache) > 256:
            s_cache = s_cache[len(s_cache) - 256:]
    pass


def extractDataBetweenHeaders(data: bytes, header: bytes, footer: bytes) -> (bytes, int) | None:
    """
    从给定的bytes数据中提取帧头和帧尾之间的数据。

    :param data: 包含帧头和帧尾的bytes数据
    :param header: 帧头的bytes值
    :param footer: 帧尾的bytes值
    :return: 帧头和帧尾之间的bytes数据，如果没有找到则返回null
    """
    # 寻找帧头的位置
    header_start: int = data.find(header)
    if header_start == -1:
        # 没有找到帧头
        return None

    header_end: int = header_start + len(header)
    footer_start: int = data.find(footer, header_end)
    if footer_start == -1:
        # 没有找到帧尾，或者帧尾在帧头之前（这通常是不应该发生的）
        return None

        # 提取帧头和帧尾之间的数据

    return data[header_start:footer_start + len(footer)], footer_start + len(footer)


def forwardData(pack: bytes):
    if len(pack) <= 3 + 7 + 3:
        return

    _from: int = pack[3 + 0]
    _to: int = pack[3 + 1]
    _header: int = pack[3 + 2]

    if isDebug:
        print("forwardData", pack)

    if _from == _to:
        return

    if _to == SERVER:
        if cli is None:
            print("TO SEND VOID")
            return
        toBeSentToServer.put(pack)
        if isDebug:
            print("TO SEND SERVER")
    elif _to == CLIENT:
        # uart_main.write(pack)
        toBeSentToClient.put(pack)
        if isDebug:
            print("TO SEND CLIENT")
    elif _to == SCREEN:
        toBeSentToScreen.put(pack[10:])
        if isDebug:
            print("TO SEND SCREEN")
    elif _to == _4G:
        if isDebug:
            print("TO SEND 4G")
        id = readInt32FromBytes(pack, 3 + 3)

        if _header == ORDER:
            pack = bytearray()

            pack.extend(header)
            pack.append(_to)
            pack.append(_from)
            pack.append(ORDER)

            addInt32ToBytes(id, pack)

            pack.append(EXCEPTION)
            pack.extend(footer)

            forwardData(bytes(pack))

            return

        if _header == ANSWER_BACK:
            result = pack[3 + 7]
            _pack = pack[3 + 8: len(pack) - 3]

            callback: CommandCallback | None = None
            for e in commandCallbackList:
                if e.cId == id:
                    callback = e
                    break

            if callback is None:
                if isDebug:
                    print("NO callback")
                return

            commandCallbackList.remove(callback)

            if result == SUCCESSFUL:
                if callback.successCallback is not None:
                    callback.successCallback(_pack)
            elif result == FAIL:
                if callback.failCallback is not None:
                    callback.failCallback(_pack)
            elif result == EXCEPTION:
                if callback.exceptionCallback is not None:
                    callback.exceptionCallback(_pack)
            pass
    pass


def down(command: CommandCallback):
    forwardData(command.command)
    commandCallbackListLock.acquire()
    commandCallbackList.append(command)
    commandCallbackListLock.release()
    pass


def addInt32ToBytes(i: int, array: bytearray):
    array.append((i >> 8 * 3) & 0xFF)
    array.append((i >> 8 * 2) & 0xFF)
    array.append((i >> 8 * 1) & 0xFF)
    array.append((i >> 8 * 0) & 0xFF)
    pass


def readInt32FromBytes(array: bytes, h: int) -> int:
    i = 0
    i |= array[h] << 8 * 3
    i |= array[h + 1] << 8 * 2
    i |= array[h + 2] << 8 * 1
    i |= array[h + 3] << 8 * 0
    return i


def sendServer():
    while True:
        time.sleep(0.25)
        if cli is not None:
            while not toBeSentToServer.empty():
                pack = toBeSentToServer.get()
                if isDebug:
                    print("sentToServer", pack)
                cli.send(pack)
    pass


def sendClient():
    while True:
        time.sleep(0.75)
        if not toBeSentToClient.empty():
            pack = toBeSentToClient.get()
            if isDebug:
                print("sendToClient", pack)
            uart_client.write(pack)
    pass


def sendScreen():
    while True:
        time.sleep(0.25)
        if not toBeSentToScreen.empty():
            pack = toBeSentToScreen.get()
            if isDebug:
                print("sendToScreen", pack)
            uart_screen.write(pack)
    pass


def timeoutDetection():
    while True:
        time.sleep(10)
        if len(commandCallbackList) != 0:
            ntime = utime.time()
            commandCallbackListLock.acquire()
            rList = None
            for c in commandCallbackList:
                if ntime - c.sendTime > 30:
                    if rList is None:
                        rList = []
                    rList.append(c)
            if rList is not None:
                for r in rList:
                    commandCallbackList.remove(r)
                for e in rList:
                    if e.outTime is not None:
                        e.outTime()
            commandCallbackListLock.release()
    pass


def bark():
    global cli
    print("BARK")
    if cli is not None:
        cli.close()
        cli = None
    Power.powerRestart()
    pass


t_all: int = 0
t_success: int = 0
t_fail: int = 0
t_exception: int = 0
t_outTime: int = 0


def connect_test():
    com = bytearray()
    com.append(INIT_END)
    while True:
        time.sleep(0.75)
        down(CommandCallback(com, CLIENT, connect_test_success, connect_test_fail, connect_test_exception,
                             connect_test_out))
        global t_all
        t_all += 1

        if t_all > 100:
            return

    pass


def connect_test_log():
    while True:
        time.sleep(10)
        print(
            ">>> >",
            "t_all", t_all,
            "t_success", t_success,
            "t_fail", t_fail,
            "t_exception", t_exception,
            "t_outTime", t_outTime,
            "commandCallbackList.size()", len(commandCallbackList),
            "toBeSentToClient.size()", toBeSentToClient.size())
    pass


def connect_test_success(p):
    global t_success
    t_success += 1
    pass


def connect_test_fail(p):
    global t_fail
    t_fail += 1
    pass


def connect_test_exception(p):
    global t_exception
    t_exception += 1
    pass


def connect_test_out():
    global t_outTime
    t_outTime += 1
    pass


def feedThread():
    while True:
        time.sleep(10)
        if wdt is not None:
            wdt.feed()
    pass


def reconnectionThread():
    global cli
    while True:
        if cli is None:
            init()
        else:
            cli.send(b"\x00")
            if time.time() - heartbeatTime > 60:
                cli.close()
                cli = None
        time.sleep(10)


if __name__ == '__main__':

    if isDebug:
        powerDownReason = Power.powerDownReason()
        powerOnReason = Power.powerOnReason()
        print("powerDownReason", powerDownReason)
        print("powerOnReason", powerOnReason)
    pass

    gc.enable()
    wdt = WatchDog(5)
    wdt.start()

    if isConnectionTest:
        _thread.start_new_thread(connect_test, ())
        _thread.start_new_thread(connect_test_log, ())
    else:
        _thread.start_new_thread(reconnectionThread, ())

    _thread.start_new_thread(uwebsocketMonitoring, ())
    _thread.start_new_thread(clientSerialMonitoring, ())
    _thread.start_new_thread(screenSerialMonitoring, ())
    _thread.start_new_thread(sendServer, ())
    _thread.start_new_thread(sendClient, ())
    _thread.start_new_thread(sendScreen, ())

    _thread.start_new_thread(timeoutDetection, ())

    _thread.start_new_thread(feedThread, ())

    print("end")

    while True:
        time.sleep(1)
        while not planExecution.empty():
            planExecution.get()()
