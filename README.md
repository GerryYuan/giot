GIoT
==========
**GIoT** is an open source **IoT** platform that supports device management, object model, device management, rule engine, multiple storage, multiple sinks, multiple protocols (http, mqtt, tcp, custom protocols), etc. And so on, provide plug-in development.

[![GitHub stars](https://img.shields.io/github/stars/GerryYuan/giot.svg?label=Stars&logo=github)](https://github.com/GerryYuan/giot)
[![Maven Central](https://img.shields.io/badge/License-Apache%202.0-blue.svg?label=license)](https://github.com/GerryYuan/giot/blob/master/LICENSE) 
![IoT云边协同架构图](https://user-images.githubusercontent.com/11907624/111753822-41009900-88d2-11eb-9018-3d37dd2a9493.png)

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
