# giot
##数据上报上来，支持的协议（http、mqtt、tcp，自定义协议），网关（独立项目，类似thingsboard的gateway，用在协议解析），物模型，产品、设备管理，规则引擎
core:
  selector: default
  default:
    
storage:
  selector: postgresql
  postgresql:
    url: r2dbc:postgresql://localhost:5432/giot
    username: postgres
    password: giot

##设备数据上报插件，支持
device-data-sink:
  selector: elasticsearch/mysql/postgresql/
  elasticsearch:
    url: 127.0.0.1:9200
    username: elasticsearch
    password: giot
  mysql:
    url: xxx