# giot
##�����ϱ�������֧�ֵ�Э�飨http��mqtt��tcp���Զ���Э�飩�����أ�������Ŀ������thingsboard��gateway������Э�����������ģ�ͣ���Ʒ���豸������������
core:
  selector: default
  default:
    
storage:
  selector: postgresql
  postgresql:
    url: r2dbc:postgresql://localhost:5432/giot
    username: postgres
    password: giot

##�豸�����ϱ������֧��
device-data-sink:
  selector: elasticsearch/mysql/postgresql/
  elasticsearch:
    url: 127.0.0.1:9200
    username: elasticsearch
    password: giot
  mysql:
    url: xxx