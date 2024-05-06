# WaterDetection(渔场水质监测系统)

## 简介

- 嵌入式系统：
  本地系统实现PH值、温度、溶解氧监测，按后续需求添加浑浊度TDS等；
  本系统屏幕实时显示检测到的数据；
  本系统数据异常处理及自动报警，数据异常时判断是否在线，若离线系统优先自主处理如果系统处理之后数据恢复正常不会报警；在线时听从云平台调度；
- 服务端：
  服务端负责数据存储、检测功能，并且可以自定义上报数据类型
  服务端可以检测数据、若数据异常可以向预留手机、邮件发送信息；可以根据配置规则下饭执行命令
- 网页端：
  网页端则提供直观的可视化界面，用户可轻松查看数据趋势，进行设备配置

## 嵌入式命令

- /write {name} {value}

  表示更改配置，需要和外部存储器交互

  | {name}   | 解释        | 值类型                  | 数据大小(bit) |
      |----------|-----------|----------------------|-----------|
  | url      | 表示会话的服务器  | string ascii         | 128       |
  | username | 表示用户名     | string [8-32] ascii  | 32        |
  | password | 表示用户密码    | string [8-32] ascii  | 32        |
  | equipment | 表示当前设备的名称 | string [1-32] utf-16 | 64        |


- /read {name}

  表示返回当前配置的信息，需要和外部存储器交互 执行命令同 /write

- /set {name} {name2?} {value}

  表示设置当前缓存的值

  | {name} | 解释 |  {name2?} | {value} |
      |----------|---------------|---------|----------|
  | dataType | 表示当前缓存传感器的值 |  数据类型的名称  |float |
  | actuator | 表示当前缓存执行器是否开启 |  执行器的名称   |uint8_t |

- /get {name} {name2?}

  表示读取当前缓存的值

  | {name}       | 解释            | {name2?} | 值类型      |
      |--------------|---------------|---------|----------|
  | dataType     | 表示当前缓存传感器的值   | 数据类型的名称  | float    | 
  | dataTypeList | 表示支持所有传感器的名称  | null     | string[] |
  | actuator     | 表示当前缓存执行器是否开启 | 执行器的名称   | uint8_t  | 
  | actuatorList | 表示支持所有传感器的名称  |  null     | string[] |

- /start {actuatorName}

  表示开启特定的执行器

- /stop {actuatorName}

  表示停止特定的执行器

- /rule {ruleId} {dataTypeName} {exceptionUpper} {warnUpper} {warnLower} {exceptionLower}

  用来同步规则

  |                 | 解释                | 值类型      |  
    |-----------------|-------------------|----------|
  | {ruleId}        | 规则ID 由服务端下发用于连接命令 | uint32_t |
  | {dataTypeName}  | 对应数据类型的名称         | string   |
  | {exceptionUpper} | 异常上界              | float    |
  | {warnUpper}     | 警告上界              | float    |
  | {warnLower}     | 警告下界              | float    |
  | {exceptionLower} | 异常下界              | float    |

- /command {ruleId} 
  
  