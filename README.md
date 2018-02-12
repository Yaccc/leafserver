# leafserver
A high performance distributed unique ID generation system


### feature

  - 不限制RPC协议，但是提供默认的Dubbo、pigeon等。用户自行选择RPC框架（以后有计划提供单调递增ID时再考虑）、提供http接口
  - 基础框架基于springboot构建
  - 安全策略、鉴权暂时不考虑
  - 默认使用CAT监控，用户也可以自己实现，需要解耦。最好是SPI方式
  - 号段模式，同步或者异步取，需要开关控制
  - 根据并发度QPS调整号段大小，可以手动改变号段
  - hystrix对业务身份限流
  - 丰富的控制台界面,集群管理,key的迁移等(是否考虑)
  - 单调递增的feature,提供绝对递增的功能(最大的feature)


  
  
