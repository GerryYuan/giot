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

import lombok.Setter;
import org.giot.core.exception.ServiceNotFoundException;
import org.giot.core.module.ModuleDefinition;
import org.giot.core.service.Service;
import org.giot.core.service.ServiceHandler;
import org.giot.core.service.ServiceManager;

/**
 * @author Created by gerry
 * @date 2021-02-27-11:22 PM
 */
public abstract class AbstractContainer implements Container, ServiceHandler {

    @Setter
    private ContainerManager containerManager;

    @Setter
    private ServiceManager serviceManager;

    public abstract String name();

    public abstract ModuleDefinition module();

    public abstract ContainerConfig createConfig();

    public ServiceHandler find(String moduleName, String containerName) {
        return this.containerManager.find(moduleName, containerName);
    }

    protected ModuleDefinition find(String moduleName) {
        return this.containerManager.find(moduleName);
    }

    @Override
    public <T extends Service> T getService(final Class<T> clazz) {
        return serviceManager.getService(clazz);
    }

    @Override
    public void register(final Class<? extends Service> serviceType,
                         final Service service) throws ServiceNotFoundException {
        serviceManager.register(serviceType, service);
    }
}
