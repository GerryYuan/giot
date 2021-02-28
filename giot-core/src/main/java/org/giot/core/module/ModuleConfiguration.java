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

package org.giot.core.module;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.giot.core.utils.EmptyUtils;

/**
 * @author yuanguohua on 2021/2/25 13:48
 */
@ToString
@Slf4j
public class ModuleConfiguration {

    public static final String WHICH = "which";

    @Getter
    private Map<ModuleDefinition, List<ComponentConfiguration>> moduleConfigurations = new ConcurrentHashMap<>();

    @Getter
    private Set<String> modules = new HashSet<>();

    private ServiceLoader<ModuleDefinition> defs;

    public List<ComponentConfiguration> getComponents(String name) {
        ModuleDefinition moduleDefinition = supports(name);
        return moduleConfigurations.get(moduleDefinition);
    }

    public List<ComponentConfiguration> getAllComponents() {
        List<ComponentConfiguration> componentConfigurations = new ArrayList<>(10);
        for (String module : modules) {
            List<ModuleConfiguration.ComponentConfiguration> components = getComponents(module);
            if (EmptyUtils.isEmpty(components)) {
                continue;
            }
            componentConfigurations.addAll(components);
        }
        return componentConfigurations;
    }

    public ModuleConfiguration.ComponentConfiguration getComponentByContainerName(String containerName) {
        return getAllComponents().stream()
                                 .filter(component -> component.getName().equalsIgnoreCase(containerName))
                                 .findFirst()
                                 .orElse(null);
    }

    public void addModule(String name) {
        addModule(name, new ArrayList<>(1));
    }

    public void addModule(String name, List<ComponentConfiguration> components) {
        ModuleDefinition moduleDefinition = supports(name);
        if (EmptyUtils.isEmpty(moduleDefinition)) {
            log.warn("Module [{}] is not support, don't load it.", name);
            return;
        }
        addModule(moduleDefinition, components);
    }

    private void addModule(ModuleDefinition module, List<ComponentConfiguration> components) {
        if (EmptyUtils.isEmpty(components)) {
            moduleConfigurations.put(module, new ArrayList<>(1));
        } else {
            List<ComponentConfiguration> oldC = moduleConfigurations.get(module);
            if (EmptyUtils.isEmpty(oldC)) {
                moduleConfigurations.put(module, components);
            } else {
                oldC.addAll(components);
            }
        }
        modules.add(module.module());
    }

    private ModuleDefinition supports(String name) {
        if (this.defs == null) {
            this.defs = ServiceLoader.load(ModuleDefinition.class);
        }
        for (ModuleDefinition moduleDefinition : this.defs) {
            if (moduleDefinition.module().equalsIgnoreCase(name)) {
                return moduleDefinition;
            }
        }
        return null;
    }

    @Getter
    @AllArgsConstructor
    public static class ComponentConfiguration {
        private String name;

        private Properties properties;
    }

}
