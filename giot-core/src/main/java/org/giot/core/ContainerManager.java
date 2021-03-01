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

package org.giot.core;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ServiceLoader;
import lombok.extern.slf4j.Slf4j;
import org.giot.core.container.AbstractContainer;
import org.giot.core.container.ContainerConfig;
import org.giot.core.container.ContainerHandler;
import org.giot.core.exception.ContainerConfigException;
import org.giot.core.module.ModuleConfiguration;
import org.giot.core.utils.BeanUtils;

/**
 * 容器管理者
 *
 * @author Created by gerry
 * @date 2021-02-27-10:55 PM
 */
@Slf4j
public class ContainerManager implements ContainerHandler {

    private ModuleConfiguration moduleConfiguration;

    @Override
    public boolean has(final String moduleName) {
        return moduleConfiguration.getModules().contains(moduleName);
    }

    public void init(ModuleConfiguration moduleConfiguration) throws ContainerConfigException {
        this.moduleConfiguration = moduleConfiguration;
        ServiceLoader<AbstractContainer> containers = ServiceLoader.load(AbstractContainer.class);
        for (AbstractContainer container : containers) {
            prepare(container);
            container.start();
            container.after();
        }
    }

    private void prepare(AbstractContainer container) throws ContainerConfigException {
        //获取组件
        ModuleConfiguration.ComponentConfiguration component = moduleConfiguration.getComponentByContainerName(
            container.name());
        //把配置文件的内容赋值给ContainerConfig
        try {
            copyProperties(component.getProperties(), container.createConfig(), container.name());
        } catch (IllegalAccessException e) {
            throw new ContainerConfigException(
                container.name() + " container config transport to config bean failure.", e);
        }
        container.prepare();
    }

    private void copyProperties(Properties src, ContainerConfig dest,
                                String container) throws IllegalAccessException {
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
