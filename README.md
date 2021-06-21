# 2021.5月换了新工作，太忙了，项目更新暂时搁置，未来找合适的机会重新开启

GIoT
==========
**GIoT** is an open source **IoT** platform that supports device management, object model, device management, rule engine, multiple storage, multiple sinks, multiple protocols (http, mqtt, tcp, custom protocols), etc. And so on, provide plug-in development.

[![GitHub stars](https://img.shields.io/github/stars/GerryYuan/giot.svg?label=Stars&logo=github)](https://github.com/GerryYuan/giot)
[![Maven Central](https://img.shields.io/badge/License-Apache%202.0-blue.svg?label=license)](https://github.com/GerryYuan/giot/blob/master/LICENSE) 
![IoT Cloud Edge Collaboration Architecture Diagram](https://user-images.githubusercontent.com/11907624/117307186-ed163780-aeb2-11eb-932a-afdba3795abc.png)



# Documentation
# Quick Start
Quick starter `Module` -> `giot-starter` by source code.
```java
public class GiotStarter {

    public static void main(String[] args) throws FileNotFoundException, ContainerConfigException, ContainerStartException {
        Stopwatch sp = Stopwatch.createStarted();
        ResourceLoader resourceLoader = new ModuleResourceLoader("application.yml");
        ModuleConfiguration moduleConfiguration = resourceLoader.load();
        ModuleManager moduleManager = new ModuleManager();
        moduleManager.start(moduleConfiguration);
        log.info("GIoT server start success, cost time [{}] ms ", sp.elapsed(TimeUnit.MILLISECONDS));

    }
}
```
# How to build
Follow this [document](docs/guides/How-to-build.md)
# Downloads
# Live Demo
# License
[Apache 2.0 License.](LICENSE)
