"""
运行本例程，需要通过串口线连接开发板的 MAIN 口和PC，在PC上通过串口工具
打开 MAIN 口，并向该端口发送数据，即可看到 PC 发送过来的消息。
"""
import _thread
import utime
import log
from machine import UART

'''
 * 参数1：端口
        注：EC100YCN平台与EC600SCN平台，UARTn作用如下
        UART0 - DEBUG PORT
        UART1 – BT PORT
        UART2 – MAIN PORT
        UART3 – USB CDC PORT
 * 参数2：波特率
 * 参数3：data bits  （5~8）
 * 参数4：Parity  （0：NONE  1：EVEN  2：ODD）
 * 参数5：stop bits （1~2）
 * 参数6：flow control （0: FC_NONE  1：FC_HW）
'''


# 设置日志输出级别
log.basicConfig(level=log.INFO)
uart_log = log.getLogger("UART")

class Example_uart(object):
    def __init__(self, no=UART.UART2, bate=115200, data_bits=8, parity=0, stop_bits=1, flow_control=0):
        self.uart = UART(no, bate, data_bits, parity, stop_bits, flow_control)
        self.uart.set_callback(self.callback)


    def callback(self, para):
        uart_log.info("call para:{}".format(para))
        if(0 == para[0]):
            self.uartRead(para[2])


    def uartWrite(self, msg):
        uart_log.info("write msg:{}".format(msg))
        self.uart.write(msg)

    def uartRead(self, len):
        msg = self.uart.read(len)
        utf8_msg = msg.decode()
        uart_log.info("UartRead msg: {}".format(utf8_msg))
        return utf8_msg

    def uartWrite_test(self):
        for i in range(10):
            write_msg = "Hello count={}".format(i)
            self.uartWrite(write_msg)
            utime.sleep(1)

if __name__ == "__main__":
    uart_test = Example_uart()
    uart_test.uartWrite_test()


# 运行结果示例
'''
INFO:UART:write msg:Hello count=0
INFO:UART:write msg:Hello count=1
INFO:UART:write msg:Hello count=2
INFO:UART:write msg:Hello count=3
INFO:UART:write msg:Hello count=4
INFO:UART:write msg:Hello count=5
INFO:UART:write msg:Hello count=6
INFO:UART:write msg:Hello count=7
INFO:UART:write msg:Hello count=8
INFO:UART:write msg:Hello count=9

INFO:UART:call para:[0, 2, 15]
INFO:UART:UartRead msg: my name is XXX


'''

