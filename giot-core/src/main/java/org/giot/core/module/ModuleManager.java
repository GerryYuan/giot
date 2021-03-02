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
import java.util.List;
import lombok.Getter;
import org.giot.core.container.ContainerManager;
import org.giot.core.exception.ContainerConfigException;
import org.giot.core.utils.EmptyUtils;

/**
 * @author yuanguohua on 2021/3/2 19:01
 */
public class ModuleManager implements ModuleHandler {

    private ModuleConfiguration moduleConfiguration;

    @Getter
    private ContainerManager containerManager;

    @Override
    public boolean hasModule(final String moduleName) {
        return moduleConfiguration.getModules().contains(moduleName);
    }

    public void init(ModuleConfiguration moduleConfiguration) throws ContainerConfigException {
        this.moduleConfiguration = moduleConfiguration;
        List<ModuleConfiguration.ContainerDefinition> containerDefinitions = new ArrayList<>(20);
        for (ModuleDefinition moduleDef : moduleConfiguration.getDefs()) {
            List<ModuleConfiguration.ContainerDefinition> containerDefs = moduleConfiguration.getModuleConfigurations()
                                                                                             .get(moduleDef);
            if (EmptyUtils.isEmpty(containerDefs)) {
                continue;
            }
            containerDefinitions.addAll(containerDefs);
        }
        this.containerManager = new ContainerManager(containerDefinitions);
        containerManager.init();
    }
}
