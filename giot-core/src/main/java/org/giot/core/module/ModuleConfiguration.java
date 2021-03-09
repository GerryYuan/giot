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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.giot.core.exception.ModuleNotFoundException;

/**
 * @author yuanguohua on 2021/2/25 13:48
 */
@ToString
@Slf4j
public class ModuleConfiguration {

    public static final String WHICH = "which";

    @Getter
    private Multimap<ModuleDefinition, ContainerDefinition> moduleConfigurations = ArrayListMultimap.create();

    @Getter
    private Set<String> modules = new HashSet<>();

    @Getter
    private ServiceLoader<ModuleDefinition> defs;

    public void addModule(String moduleName, List<ContainerDefinition> cds) {
        ModuleDefinition md = supports(moduleName);
        for (ContainerDefinition cd : cds) {
            moduleConfigurations.put(md, cd);
        }
        modules.add(md.name());
    }

    protected ModuleDefinition supports(String moduleName) {
        if (this.defs == null) {
            this.defs = ServiceLoader.load(ModuleDefinition.class);
        }
        for (ModuleDefinition moduleDefinition : this.defs) {
            if (moduleDefinition.name().equalsIgnoreCase(moduleName)) {
                return moduleDefinition;
            }
        }
        throw new ModuleNotFoundException("Module [" + moduleName + "] not found");
    }

    @Getter
    @AllArgsConstructor
    public static class ContainerDefinition {

        private String name;

        private Properties properties;
    }

}
