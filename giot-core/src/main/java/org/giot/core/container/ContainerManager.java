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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ServiceLoader;
import lombok.extern.slf4j.Slf4j;
import org.giot.core.exception.ContainerConfigException;
import org.giot.core.exception.ContainerNotFoundException;
import org.giot.core.exception.ContainerStartException;
import org.giot.core.module.ModuleConfiguration;
import org.giot.core.module.ModuleDefinition;
import org.giot.core.module.ModuleManager;
import org.giot.core.service.ServiceHandler;
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

    private Multimap<ModuleDefinition, AbstractContainer> containers = ArrayListMultimap.create();

    public ContainerManager(final ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    @Override
    public boolean has(final String moduleName) {
        Collection<AbstractContainer> cs = containers.get(moduleManager.find(moduleName));
        return EmptyUtils.isNotEmpty(cs);
    }

    @Override
    public boolean has(final String moduleName, final String containerName) {
        Collection<AbstractContainer> cs = containers.get(moduleManager.find(moduleName));
        if (EmptyUtils.isEmpty(cs)) {
            return false;
        }
        return cs.stream().filter(c -> c.name().equalsIgnoreCase(containerName)).findAny().isPresent();
    }

    @Override
    public ServiceHandler find(final String moduleName) {
        return find(moduleName, Container.DEFAULT);
    }

    @Override
    public ServiceHandler find(final String moduleName, final String containerName) {
        Collection<AbstractContainer> cs = containers.get(moduleManager.find(moduleName));
        if (EmptyUtils.isEmpty(cs)) {
            throw new ContainerNotFoundException("Module [" + moduleName + "] not provider container.");
        }
        ServiceHandler container = cs.stream()
                                          .filter(c -> c.name().equalsIgnoreCase(containerName))
                                          .findFirst()
                                          .orElse(null);
        if (EmptyUtils.isEmpty(container)) {
            throw new ContainerNotFoundException(
                "Module [" + moduleName + "] has not [" + containerName + "] provider container.");
        }
        return container;
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
        }
        start();
        after();
    }

    private void initialize(AbstractContainer container) {
        container.setContainerManager(this);
    }

    private Properties whichProperties(AbstractContainer container) {
        String moduleName = container.module();
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

    private void start() throws ContainerStartException {
        for (AbstractContainer container : containers.values()) {
            container.start();
        }
    }

    private void after() {
        for (AbstractContainer container : containers.values()) {
            container.after();
            log.info(
                "The container [" + container.name() + "] provided by the module [" + container.module() + "] is initialized");
        }
    }

    private void addContainer(AbstractContainer container) {
        ModuleDefinition moduleDefinition = moduleManager.find(container.module());
        containers.put(moduleDefinition, container);
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
