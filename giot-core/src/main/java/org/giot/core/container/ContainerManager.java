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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.giot.core.exception.ContainerConfigException;
import org.giot.core.exception.ContainerNotFoundException;
import org.giot.core.module.ModuleConfiguration;
import org.giot.core.module.ModuleDefinition;
import org.giot.core.module.ModuleDefinitionManager;
import org.giot.core.utils.EmptyUtils;

/**
 * 容器管理者
 *
 * @author Created by gerry
 * @date 2021-02-27-10:55 PM
 */
@Slf4j
public class ContainerManager implements ContainerHandler {

    private ModuleDefinitionManager moduleDefinitionManager;

    private Map<ModuleDefinition, List<AbstractContainer>> containers = new ConcurrentHashMap<>();

    public ContainerManager(final ModuleDefinitionManager moduleDefinitionManager) {
        this.moduleDefinitionManager = moduleDefinitionManager;
    }

    @Override
    public boolean has(final String moduleName, final String containerName) {
        List<AbstractContainer> cs = containers.get(moduleDefinitionManager.find(moduleName));
        return cs.stream()
                 .filter(container -> container.name().equalsIgnoreCase(containerName))
                 .findAny()
                 .isPresent();
    }

    @Override
    public AbstractContainer find(final String moduleName, final String containerName) {
        List<AbstractContainer> cs = containers.get(moduleDefinitionManager.find(moduleName));
        return cs.stream()
                 .filter(container -> container.name().equalsIgnoreCase(containerName))
                 .findAny().orElseThrow(new ContainerNotFoundException("Container [" + containerName + "] not found"));
    }

    public void init() throws ContainerConfigException {
        ServiceLoader<AbstractContainer> containerServiceLoader = ServiceLoader.load(AbstractContainer.class);
        for (AbstractContainer container : containerServiceLoader) {
            prepare(container);
            container.start();
            container.after();
            addContainer(moduleDefinitionManager.find(container.module()), container);
        }
    }

    private void addContainer(ModuleDefinition moduleDefinition, AbstractContainer container) {
        List<AbstractContainer> cs = this.containers.get(moduleDefinition);
        if (EmptyUtils.isEmpty(cs)) {
            containers.put(moduleDefinition, new ArrayList<>(3));
        } else {
            cs.add(container);
        }
    }

    private void prepare(AbstractContainer container) throws ContainerConfigException {
        //获取容器def
        ModuleConfiguration.ContainerDefinition containerDef = moduleDefinitionManager.find(
            container.module(), container.name());

        //把配置文件的内容赋值给ContainerConfig
        try {
            copyProperties(containerDef.getProperties(), container.createConfig(), container.name());
        } catch (IllegalAccessException e) {
            throw new ContainerConfigException(
                containerDef.getName() + " component config transport to config bean failure.", e);
        }
        container.setContainerManager(this);
        container.prepare();
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
