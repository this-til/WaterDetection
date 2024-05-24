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
dataNameList: list[str] = []
actuatorNameList: list[str] = []