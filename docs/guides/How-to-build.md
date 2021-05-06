# How to build project
This document helps people compile and build projects in your Maven and set up your IDE.

### Build from GitHub
1. Prepare git, JDK8 and maven3
1. Clone project
    ```bash
    git clone https://github.com/GerryYuan/giot.git
    cd giot/
    ```
   
1. Run `./mvn clean package -DskipTests`
1. Packages are in `/dist` (.tar.gz for Linux or MacOS).