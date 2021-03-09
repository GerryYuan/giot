### giot IoT Platform, Device management, data collection, processing and visualization, multi protocol, rule engine
### 设计初衷
希望能提供IOT平台一切支持，通过插件开发，支持设备管理、物模型，产品、设备管理、规则引擎、多种存储、sink、协议（http、mqtt、tcp，自定义协议）等等，只要改动配置文件就可以切换需要存储的方式
把giot当成一个用于iot平台的中间件
### 架构设计：
### 1、配置文件中，第一层是数据模块，第二层是数据组件，ModuleConfiguration
### 2、一个模块(Module)对应多个容器(ContainerDefinition)，一个容器有多个服务(Service)
### 3、加载配置文件
### 4、初始化容器，实现依赖注入（比如postgresql实例，对应的配置）
```yaml
core:
  which: default
  default:
    metaDataStorage: postgresql
    deviceDataStorage: postgresql
    c: 1.6
    ## 物模型、传输协议（gateway）插件、消息协议插件
device:
  which: default
  default:
  ##
storage:
  which: postgresql,elasticsearch7
  postgresql:
    url: r2dbc:postgresql://localhost:5432/giot
    username: postgres
    password: giot
  elasticsearch:
    url: 127.0.0.1:9200
    username: elasticsearch
    password: giot
  mysql:
    url: xxx
  elasticsearch7:
    url: xxx
```

    
### IOT设备消息分为3个
properties//设备消息上报属性、funtions//设备接受的处理函数、events//设备触发的事件