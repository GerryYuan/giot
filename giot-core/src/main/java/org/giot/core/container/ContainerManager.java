/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.giot.core.container;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.giot.core.exception.ContainerConfigException;
import org.giot.core.exception.ContainerNotFoundException;
import org.giot.core.exception.ContainerStartException;
import org.giot.core.module.ModuleConfiguration;
import org.giot.core.module.ModuleDefinition;
import org.giot.core.module.ModuleManager;
import org.giot.core.service.ServiceHandler;
import org.giot.core.service.ServiceManager;
import org.giot.core.utils.EmptyUtils;

/**
 * 容器管理者
 *
 * @author Created by gerry
 * @date 2021-02-27-10:55 PM
 */
@Slf4j
public class ContainerManager implements ContainerHandler {

    private ModuleManager moduleManager;

    private ServiceManager serviceManager;

    private Map<ModuleDefinition, List<AbstractContainer>> containers = new ConcurrentHashMap<>();

    public ContainerManager(final ModuleManager moduleManager,
                            final ServiceManager serviceManager) {
        this.moduleManager = moduleManager;
        this.serviceManager = serviceManager;
    }

    @Override
    public boolean has(final String moduleName, final String containerName) {
        List<AbstractContainer> cs = containers.get(moduleManager.find(moduleName));
        return cs.stream()
                 .filter(container -> container.name().equalsIgnoreCase(containerName))
                 .findAny()
                 .isPresent();
    }

    @Override
    public ServiceHandler find(final String moduleName, final String containerName) {
        List<AbstractContainer> cs = containers.get(moduleManager.find(moduleName));
        return cs.stream()
                 .filter(container -> container.name().equalsIgnoreCase(containerName))
                 .findAny().orElseThrow(new ContainerNotFoundException("Container [" + containerName + "] not found"));
    }

    @Override
    public ModuleDefinition find(final String moduleName) {
        return moduleManager.find(moduleName);
    }

    public void init() throws ContainerConfigException, ContainerStartException {
        ServiceLoader<AbstractContainer> containerServiceLoader = ServiceLoader.load(AbstractContainer.class);
        for (AbstractContainer container : containerServiceLoader) {
            initialize(container);
            Properties properties = whichProperties(container);
            if (properties == null) {
                continue;
            }
            prepare(container, properties);
            container.start();
            after(container);
        }
    }

    private void initialize(AbstractContainer container) {
        container.setContainerManager(this);
        container.setServiceManager(this.serviceManager);
    }

    private Properties whichProperties(AbstractContainer container) {
        String moduleName = container.module().name();
        String containerName = container.name();
        //获取容器def
        ModuleConfiguration.ContainerDefinition containerDef = moduleManager.find(moduleName, containerName);
        //如果当前代码中实现了很多容器插件，比如storage插件eg:es,es7,mysql等，但是配置文件中which中没有填写，则过滤
        if (EmptyUtils.isEmpty(containerDef)) {
            log.debug(
                "Module [" + moduleName + "] should not be provided container [" + containerName + "], based on yaml file for 'which'.");
            return null;
        }
        return containerDef.getProperties();
    }

    private void prepare(AbstractContainer container, final Properties properties) throws ContainerConfigException {
        String containerName = container.name();
        //把配置文件的内容赋值给ContainerConfig
        try {
            copyProperties(properties, container.createConfig(), containerName);
        } catch (IllegalAccessException e) {
            throw new ContainerConfigException(
                container.name() + " component config transport to config bean failure.", e);
        }
        addContainer(container);
        container.prepare();
    }

    private void after(AbstractContainer container) {
        container.after();
        log.info("The container [" + container.name() + "] provided by the module [" + container.module()
                                                                                                .name() + "] is initialized");
    }

    private void addContainer(AbstractContainer container) {
        ModuleDefinition moduleDefinition = moduleManager.find(container.module().name());
        List<AbstractContainer> cs = this.containers.get(moduleDefinition);
        if (EmptyUtils.isEmpty(cs)) {
            containers.put(moduleDefinition, Stream.of(container).collect(Collectors.toList()));
        } else {
            cs.add(container);
        }
    }

    private void copyProperties(Properties src, ContainerConfig dest, String container) throws IllegalAccessException {
        if (dest == null) {
            return;
        }
        Enumeration<?> propertyNames = src.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String propertyName = (String) propertyNames.nextElement();
            Class<? extends ContainerConfig> destClass = dest.getClass();
            try {
                Field field = getDeclaredField(destClass, propertyName);
                field.setAccessible(true);
                field.set(dest, src.get(propertyName));
            } catch (NoSuchFieldException e) {
                log.warn(propertyName + " setting is not supported in " + container + " container");
            }
        }
    }

    private Field getDeclaredField(Class<?> destClass, String fieldName) throws NoSuchFieldException {
        if (destClass != null) {
            Field[] fields = destClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(fieldName)) {
                    return field;
                }
            }
            return getDeclaredField(destClass.getSuperclass(), fieldName);
        }

        throw new NoSuchFieldException();
    }
}
