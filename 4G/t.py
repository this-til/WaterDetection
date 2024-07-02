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
import net
import quecgnss
import gnss

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

uart_client = UART(UART.UART2, 9600, 8, 0, 1, 0)
uart_screen = UART(UART.UART1, 9600, 8, 0, 1, 0)
c_cache = bytearray()
s_cache = bytearray()

cli = None
heartbeatTime = 0

_idAdd: int = 0

toBeSentToClient = Queue(8)
toBeSentToServer = Queue(8)
toBeSentToScreen = Queue(8)

planExecution = Queue(4)

openGnss: bool = False


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


def init():
    global _idAdd
    _idAdd = 0

    if not csqTest():
        return

    com = bytearray()
    com.append(0x00)
    com.append(0x00)

    com[0] = READ
    com[1] = URL
    forwardData(toCommand(com, CLIENT, 1))

    com[1] = USERNAME
    forwardData(toCommand(com, CLIENT, 2))

    com[1] = PASSWORD
    forwardData(toCommand(com, CLIENT, 3))

    com[1] = EQUIPMENT
    forwardData(toCommand(com, CLIENT, 4))

    com[0] = GET
    com[1] = DATA_TYPE_LIST
    forwardData(toCommand(com, CLIENT, 5))

    com[1] = ACTUATOR_LIST
    forwardData(toCommand(com, CLIENT, 6))

    com = bytearray()
    com.append(INIT_END)
    forwardData(toCommand(com, CLIENT, 7))

    pass


def toCommand(cmd: bytearray, to: int, id: int) -> bytes:
    pack = bytearray()
    pack.extend(header)
    pack.append(_4G)
    pack.append(to)
    pack.append(ORDER)

    addInt32ToBytes(id, pack)

    pack.extend(cmd)
    pack.extend(footer)

    return bytes(pack)
    pass


def response(_pack: bytes, result: int, id: int):
    if result != SUCCESSFUL:
        return

    if id == 1:
        w_url(_pack)
    elif id == 2:
        w_username(_pack)
    elif id == 3:
        w_password(_pack)
    elif id == 4:
        w_equipment(_pack)
    elif id == 5:
        w_dataTypeList(_pack)
    elif id == 6:
        w_actuatorList(_pack)
    elif id == 7:
        w_end(_pack)

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


def csqTest() -> bool:
    csq = net.csqQueryPoll()

    if isDebug:
        print("csq", csq)

    if csq == -1:
        print("csq query failed")
        return False

    if csq == 99:
        print("csq query abnormal")
        return False

    if csq <= 18:
        print("csq过低，无法进行连接")
        return False

    return True


def connect():
    global cli
    global url
    global username
    global password
    global equipment
    global dataNameList
    global actuatorNameList

    if isDebug:
        print("--------------------------------------------------------")

    if not csqTest():
        return

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

    cli = uwebsocket.Client.connect(_url, debug=isDebug)

    global heartbeatTime
    heartbeatTime = time.time()

    url = None
    username = None
    password = None
    equipment = None
    dataNameList = None
    actuatorNameList = None

    if isDebug:
        print("--------------------------------------------------------")


def close():
    global cli
    cli.close()
    cli = None
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
            if isDebug:
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
            response(_pack, result, id)
        pass
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
        time.sleep(0.75)
        if not toBeSentToScreen.empty():
            pack = toBeSentToScreen.get()
            if isDebug:
                print("sendToScreen", pack)
            uart_screen.write(pack)
    pass


def bark():
    global cli
    print("BARK")
    Power.powerRestart()
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
            toBeSentToServer.put(b"\x00")
            if time.time() - heartbeatTime > 60:
                planExecution.put(lambda: close())
        time.sleep(10)


def gnss():
    while True:
        global openGnss
        if not openGnss:
            quecgnss.configSet(0, 5)  # 设置定位星系为GPS+Beidou
            quecgnss.configSet(2, 1)  # 打开AGPS
            quecgnss.configSet(4, 1)  # 打开备电
            ret = quecgnss.init()
            openGnss = not bool(ret)

            if isDebug:

                if ret == 0:
                    print('GNSS init ok.')
                else:
                    print('GNSS init failed.')

        if openGnss:
            data = quecgnss.read(4096)

        time.sleep(10)


if __name__ == '__main__':

    if isDebug:
        print("powerDownReason", Power.powerDownReason())
        print("powerOnReason", Power.powerOnReason())
    pass

    gc.enable()
    wdt = WatchDog(5)
    wdt.start()

    _thread.start_new_thread(reconnectionThread, ())

    _thread.start_new_thread(uwebsocketMonitoring, ())
    _thread.start_new_thread(clientSerialMonitoring, ())
    # threadmonitoring.append(_thread.start_new_thread(screenSerialMonitoring, ()))
    _thread.start_new_thread(sendServer, ())
    _thread.start_new_thread(sendClient, ())
    # threadmonitoring.append(_thread.start_new_thread(sendScreen, ()))

    # _thread.start_new_thread(gnss, ())

    _thread.start_new_thread(feedThread, ())

    print("end")

    while True:
        time.sleep(1)
        while not planExecution.empty():
            try:
                planExecution.get()()
            except Exception as e:
                print("错误:", e)
