Warden
===================
#DEMO LINK: [DisNotice Web Admin](http://10.16.40.29:8080/)

> **1. 主要功能:**

> - 基于Zookeeper提供了一个分布式通知/分发系统。
> - 继承于Map的数据结构，支持捕捉数据变化的监听事件。
> - 通过split rowkey 支持对HBase表级别的异常监控。
> - Web 管理系统，可以主动对数据进行修改、分发。


> **2. 技术**

> - Core :  Zookeeper 、Netflix Curator 
> - Web System :  AngularJS 、Bootstrap


> **增加功能**

> - disNotice-web : 增加默认值的设置 ，通过json来构造对应的默认值