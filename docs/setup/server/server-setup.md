# Server setup
GIoT backend startup behaviours are driven by `config/application.yml`.
Understood the setting file will help you to read this document.

## Startup script
The default startup scripts are `/shell/bin/giot-start.sh`(.bat). 

## application.yml
The core concept behind this setting file is, GIoT server is based on pure module & container design. 
User can which or assemble the provider features by their own requirements.

Example:
```yaml
core:
  which: default
  default:
    metaDataStorage: mysql
    deviceDataStorage: postgresql
network:
  which: mqtt,http
  mqtt:
    host: localhost
    port: 1883
    userName: admin
    password: public
    clientId: test-clientId
  http:
    port: 18080
    workThreads: 10
```

1. **`core`** or **`network`** is the module.
1. **`which`** which one or many out of the all providers listed container, the unselected ones take no effect as if they were deleted.
1. **`default`** is the default implementor of module.
1. **`item`** such as `metaDataStorage`, `deviceDataStorage`... are all setting items of the implementor.

List the required modules here
1. **core**. All provider container & services basic.
1. **network**. Provide network receive data & dispatch, eventbus invoker.
1. **storage**. Make the device data or metadata process result persistence.
1. **device**. Provide device's api.