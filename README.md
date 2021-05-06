GIoT
==========
**GIoT**是一个开源的**IoT**平台，支持设备管理、物模型，产品、设备管理、规则引擎、多种存储、多sink、多协议（http、mqtt、tcp，自定义协议）、多租户管理等等，提供插件化开发

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
