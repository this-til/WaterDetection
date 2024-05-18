import _thread

import uwebsocket
from machine import UART
import time
import ucollections

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

url: str = ''
username: str = ''
password: str = ''
equipment: str = ''
dataNameList: list[str] = []
actuatorNameList: list[str] = []

isDebug = True

uart_main = UART(UART.UART2, 9600, 8, 0, 1, 0)
uart_screen = UART(UART.UART1, 9600, 8, 0, 1, 0)
cli = None

_idAdd: int = 0

sentToServerCache: list[bytes] = []

toBeSentToClient: list[bytes] = ucollections.deque((), 32, 0)
# toBeSentToServer: list[bytes] = ucollections.deque((), 32, 0)
toBeSentToScreen: list[bytes] = ucollections.deque((), 32, 0)

toBeSentToClientLock = _thread.allocate_lock()
# toBeSentToServerLock = _thread.allocate_lock()
toBeSentToScreenLock = _thread.allocate_lock()


class CommandCallback:
    command: bytes = None
    cId: int = None
    to: int = None

    successCallback = None
    failCallback = None
    exceptionCallback = None

    def __init__(self, command: bytearray, to, successCallback, failCallback, exceptionCallback):
        self.to = to

        pack = bytearray()
        pack.extend(header)
        pack.append(_4G)
        pack.append(to)
        pack.append(ORDER)

        global _idAdd
        _idAdd += 1
        self.cId = _idAdd

        # pack.extend(self.cId.to_bytes(length=4, byteorder='big', signed=False))
        addInt32ToBytes(self.cId, pack)

        pack.extend(command)
        pack.extend(footer)

        self.command = bytes(pack)

        self.successCallback = successCallback
        self.failCallback = failCallback
        self.exceptionCallback = exceptionCallback

        pass


commandCallbackList: list[CommandCallback] = []


def init():
    com = bytearray()
    com.append(0x00)
    com.append(0x00)

    com[0] = READ
    com[1] = URL
    down(CommandCallback(com, CLIENT, w_url, None, None))

    com[1] = USERNAME
    down(CommandCallback(com, CLIENT, w_username, None, None))

    com[1] = PASSWORD
    down(CommandCallback(com, CLIENT, w_password, None, None))

    com[1] = EQUIPMENT
    down(CommandCallback(com, CLIENT, w_equipment, None, None))

    com[0] = GET
    com[1] = DATA_TYPE_LIST
    down(CommandCallback(com, CLIENT, w_dataTypeList, None, None))

    com[1] = ACTUATOR_LIST
    down(CommandCallback(com, CLIENT, w_actuatorList, None, None))

    com = bytearray()
    com.append(INIT_END)
    down(CommandCallback(com, CLIENT, w_end, None, None))

    pass


def w_url(p: bytes):
    global url
    url = p[0:-1].decode('utf-8')
    if isDebug:
        print("url", url)
    pass


def w_username(p: bytes):
    global username
    username = p[0:-1].decode('utf-8')
    if isDebug:
        print("username", username)
    pass


def w_password(p: bytes):
    global password
    password = p[0:-1].decode('utf-8')
    if isDebug:
        print("password", password)
    pass


def w_equipment(p: bytes):
    global equipment
    equipment = p[0:-1].decode('utf-8')
    if isDebug:
        print("equipment", equipment)
    pass


def w_dataTypeList(p: bytes):
    global dataNameList
    for e in p[4:].split(b'\0'):
        if len(e) is not 0:
            dataNameList.append(e.decode('utf-8'))
    if isDebug:
        print("dataNameList", dataNameList)
    pass


def w_actuatorList(p: bytes):
    for e in p[4:].split(b'\0'):
        if len(e) is not 0:
            actuatorNameList.append(e.decode('utf-8'))
    if isDebug:
        print("actuatorNameList", actuatorNameList)
    pass


def w_end(p: bytes):
    global cli

    if isDebug:
        print("IN END")

    _url = bytearray()
    _url.extend(url)
    _url.extend(b'?')
    _url.extend(b'username=')
    _url.extend(username)
    _url.extend(b'&password=')
    _url.extend(password)
    _url.extend(b'&dataTypeList=')
    _url.extend(b','.join(dataNameList))
    _url.extend(b'&actuatorList=')
    _url.extend(b','.join(actuatorNameList))

    

    # _url = ("%s?username=%s&password=%s&equipment=%s&dataTypeList=%s&actuatorList=%s" %
    #         (url, username, password, equipment, ','.join(dataNameList), ','.join(actuatorNameList)))

    # _url = _url.decode()

    if isDebug:
        print("uwebsocket url", _url)

    cli = uwebsocket.Client.connect(_url, headers=None, debug=isDebug)
    # global sentToServerCache
    # for e in sentToServerCache:
    #     cli.send(e)

    # sentToServerCache.clear()
    # sentToServerCache = None

    pass


def uwebsocketMonitoring():
    while True:
        if cli is None:
            time.sleep(1)
            continue
        recv_data = cli.recv()
        if not (isinstance(recv_data, bytes)):
            recv_data = bytes(recv_data)

        if isDebug:
            print("cli.recv()", recv_data)

        pack = extractDataBetweenHeaders(recv_data, header, footer)
        if pack is None:
            continue
        forwardData(pack)
        pass


# 串口监听
def serialMonitoring(uart):
    while True:

        any = uart.any()
        if any == 0:
            continue

        pack = uart.read(any)

        if isDebug:
            print("uart.read()", pack)

        pack = extractDataBetweenHeaders(pack, header, footer)
        if pack is None:
            continue
        forwardData(pack)

    pass


def extractDataBetweenHeaders(data: bytes, header: bytes, footer: bytes) -> (bytes, bytes):
    """
    从给定的bytes数据中提取帧头和帧尾之间的数据。

    :param data: 包含帧头和帧尾的bytes数据
    :param header: 帧头的bytes值
    :param footer: 帧尾的bytes值
    :return: 帧头和帧尾之间的bytes数据，如果没有找到则返回null
    """
    # 寻找帧头的位置
    header_start = data.find(header)
    if header_start == -1:
        # 没有找到帧头
        return None

    header_end = header_start + len(header)
    footer_start = data.find(footer, header_end)
    if footer_start == -1:
        # 没有找到帧尾，或者帧尾在帧头之前（这通常是不应该发生的）
        return None

        # 提取帧头和帧尾之间的数据
    return data[header_start:footer_start + len(footer)]


def forwardData(pack: bytes):
    if len(pack) <= 3 + 7 + 3:
        return

    _from = pack[3 + 0]
    _to = pack[3 + 1]
    _header = pack[3 + 2]

    if isDebug:
        print("IN FORWARD DATA", "len(pack)", len(pack), "_from", _from, "_to", _to, "_header", _header)

    if _from == _to:
        return

    if _to == SERVER:
        if cli is None:
            sentToServerCache.append(pack)
            return
        # toBeSentToServerLock.acquire()
        cli.send(pack)
        # toBeSentToServer.append(pack[1])
        # toBeSentToServerLock.release()
        if isDebug:
            print("TO SEND SERVER")
    elif _to == CLIENT:
        toBeSentToClientLock.acquire()
        # uart_main.write(pack)
        toBeSentToClient.append(pack)
        toBeSentToClientLock.release()
        if isDebug:
            print("TO SEND CLIENT")
    elif _to == SCREEN:
        toBeSentToScreenLock.acquire()
        # uart_screen.write(pack[10:])
        toBeSentToScreen.append(pack[10:])
        toBeSentToScreenLock.release()
        if isDebug:
            print("TO SEND SCREEN")
    elif _to == _4G:
        if isDebug:
            print("TO SEND 4G")
        # id_slice = pack[3 + 3: 3 + 7]
        id = readInt32FromBytes(pack, 3 + 3)  # int.from_bytes(id_slice, byteorder='big', signed=False)

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

            callback: CommandCallback = None
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

    if isDebug:
        print("OUT FORWARD DATA")
    pass


def down(command: CommandCallback):
    if isDebug:
        print("DOWN", command.command)

    forwardData(command.command)
    commandCallbackList.append(command)
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


# def sendToServer():
#     while True:
#         time.sleep(0.25)
#         if cli is None:
#             continue
#         if len(toBeSentToServer) == 0:
#             continue
#         # toBeSentToServerLock.acquire()
#         cli.send(toBeSentToServer.popleft())
#         # toBeSentToServerLock.release()
#     pass
#
#
def sendToClient():
    while True:
        time.sleep(1)
        if len(toBeSentToClient) == 0:
            continue
        pack = None
        toBeSentToClientLock.acquire()
        pack = toBeSentToClient.popleft()
        toBeSentToClientLock.release()

        if isDebug:
            print("sendToClient", pack)
            time.sleep(1)

        uart_main.write(pack)
    pass


def sendToScreen():
    while True:
        time.sleep(1)
        if len(toBeSentToScreen) == 0:
            continue
        pack = None
        toBeSentToScreenLock.acquire()
        pack = toBeSentToScreen.popleft()
        toBeSentToScreenLock.release()
        if isDebug:
            print("sendToScreen", pack)

        uart_screen.write(pack)
    pass


_thread.start_new_thread(init, ())
# cli = uwebsocket.Client.connect("wss://218.204.179.10:60762/EquipmentSocket?username=til&password=114514&equipment=AJCBD&dataTypeList=PH&actuatorList=", headers=None, debug=True)
_thread.start_new_thread(serialMonitoring, (uart_main,))
_thread.start_new_thread(serialMonitoring, (uart_screen,))

# _thread.start_new_thread(sendToServer, ())
_thread.start_new_thread(sendToClient, ())
_thread.start_new_thread(sendToScreen, ())
