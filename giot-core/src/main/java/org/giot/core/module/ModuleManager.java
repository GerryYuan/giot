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

import java.util.Collection;
import lombok.Getter;
import org.giot.core.container.ContainerManager;
import org.giot.core.exception.ContainerConfigException;
import org.giot.core.exception.ContainerStartException;
import org.giot.core.utils.EmptyUtils;

/**
 * @author yuanguohua on 2021/3/2 19:01
 */
public class ModuleManager implements ModuleHandler {

    private ModuleConfiguration moduleConfiguration;

    @Getter
    private ContainerManager containerManager;

    @Override
    public boolean has(final String moduleName) {
        return moduleConfiguration.getModules().contains(moduleName);
    }

    @Override
    public ModuleDefinition find(final String moduleName) {
        return moduleConfiguration.supports(moduleName);
    }

    @Override
    public ModuleConfiguration.ContainerDefinition find(final String moduleName, final String containerName) {
        Collection<ModuleConfiguration.ContainerDefinition> cds = moduleConfiguration.getModuleConfigurations()
                                                                                     .get(find(
                                                                                         moduleName));
        if (EmptyUtils.isEmpty(cds)) {
            return null;
        }
        ModuleConfiguration.ContainerDefinition containerDefinition = cds.stream()
                                                                         .filter(cd -> cd.getName()
                                                                                         .equalsIgnoreCase(
                                                                                             containerName))
                                                                         .findAny()
                                                                         .orElse(null);
        if (EmptyUtils.isEmpty(containerDefinition)) {
            return null;
        }
        return containerDefinition;
    }

    public void start(ModuleConfiguration moduleConfiguration) throws ContainerConfigException, ContainerStartException {
        this.moduleConfiguration = moduleConfiguration;
        this.containerManager = new ContainerManager(this);
        containerManager.load();
    }
}
