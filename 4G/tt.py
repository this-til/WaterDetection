import _thread
import uwebsocket
from machine import UART
import time
import ucollections
import ubinascii
from misc import Power
from queue import Queue
import gc
import utime
from machine import Pin
import usys as sys

isDebug = True
wdt = None

uart_client = UART(UART.UART1, 9600, 8, 0, 1, 0)

cli = None

url: str = ""
username: str = "til"
password: str = "114514"
equipment: str = "AABB"
dataNameList: list[str] = ["PH", "浑浊度", "温度", "电导率"]
actuatorNameList: list[str] = ["报警器", "报警灯", "水泵"]

toBeSentToClient = Queue(64)
toBeSentToServer = Queue(64)


class WatchDog:

    def __init__(self, max_count):
        self.__max_count = max_count
        self.__count = self.__max_count
        self.__tid = None
        pass

    def feed(self):
        self.__count = self.__max_count  # 喂狗，重置看门狗计数器

    def __check(self):
        while True:  # 循环中检查计数器
            if (self.__count == 0):
                global bark
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


def bark():
    global cli
    print("BARK")
    if cli is not None:
        cli.close()
        cli = None
    Power.powerRestart()
    pass


def uwebsocketMonitoring():
    global cli
    while True:
        if cli is None:
            time.sleep(1)
            return

        recv_data = cli.recv()
        if recv_data is None:
            cli.close()
            cli = None
            return

        if not (isinstance(recv_data, bytes)):
            recv_data = bytes(recv_data)

        if isDebug:
            print("cli.recv()", recv_data)

        toBeSentToClient.put(recv_data)
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
            print("uart.read()", pack)

        toBeSentToServer.put(pack)
    pass


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

    _thread.start_new_thread(uwebsocketMonitoring, ())
    _thread.start_new_thread(clientSerialMonitoring, ())
    _thread.start_new_thread(sendServer, ())
    _thread.start_new_thread(sendClient, ())

    while True:
        if cli is None:

            _dataNameList: list[str] = []
            for e in dataNameList:
                _dataNameList.append("\"" + e + "\"")

            _actuatorNameList: list[str] = []
            for e in actuatorNameList:
                _actuatorNameList.append("\"" + e + "\"")
            json = (
                    """{"username":"%s","password":"%s","equipment":"%s","dataNameList":[%s],"actuatorNameList":[%s]} """
                    % (username, password, equipment, ','.join(_dataNameList), ','.join(_actuatorNameList))
            )

            if isDebug:
                print("json", json)

            _loginData = ubinascii.b2a_base64(json.encode()).decode()[0:-1]

            if isDebug:
                print("_loginData", _loginData)

            _url = url + "?" + "loginData=" + _loginData

            cli = uwebsocket.Client.connect(_url, debug=isDebug)

            pass
        time.sleep(10)
        wdt.feed()
