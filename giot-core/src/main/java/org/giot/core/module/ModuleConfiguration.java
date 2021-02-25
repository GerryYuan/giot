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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.giot.core.utils.EmptyUtils;

/**
 * @author yuanguohua on 2021/2/25 13:48
 */
public class ModuleConfiguration {

    private static final String SELECTOR = "selector";

    private Map<ModuleDefinition, List<ComponentConfiguration>> modules = new ConcurrentHashMap<>();

    private ServiceLoader<ModuleDefinition> moduleDefinitions;

    private ModuleDefinition supports(String name) {
        if (this.moduleDefinitions == null) {
            this.moduleDefinitions = ServiceLoader.load(ModuleDefinition.class);
        }
        for (ModuleDefinition moduleDefinition : this.moduleDefinitions) {
            if (moduleDefinition.module().equalsIgnoreCase(name)) {
                return moduleDefinition;
            }
        }
        return null;
    }

    public void addModule(String name) {
        ModuleDefinition moduleDefinition = supports(name);
        if (EmptyUtils.isNotEmpty(moduleDefinition)) {
            return;
        }
        addModule(moduleDefinition);
    }

    public void addModule(ModuleDefinition module) {
        modules.put(module, new ArrayList<>(1));
    }

    public void addModule(ModuleDefinition module, ComponentConfiguration component) {
        List<ComponentConfiguration> components = modules.get(module);
        if (EmptyUtils.isEmpty(components)) {
            modules.put(module, Stream.of(component).collect(Collectors.toList()));
        } else {
            components.add(component);
        }
    }

    public void addModule(ModuleDefinition module, ComponentConfiguration... components) {
        List<ComponentConfiguration> oldC = modules.get(module);
        List<ComponentConfiguration> ccs = Arrays.stream(components).collect(Collectors.toList());
        if (EmptyUtils.isEmpty(oldC)) {
            modules.put(module, ccs);
        } else {
            oldC.addAll(ccs);
        }
    }

    @AllArgsConstructor
    public static class ComponentConfiguration {
        private String name;

        private Properties properties;
    }

}
