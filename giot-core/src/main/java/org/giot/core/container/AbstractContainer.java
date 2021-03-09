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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;
import org.giot.core.exception.ServiceNotFoundException;
import org.giot.core.service.Service;
import org.giot.core.service.ServiceHandler;
import org.giot.core.utils.EmptyUtils;

/**
 * @author Created by gerry
 * @date 2021-02-27-11:22 PM
 */
public abstract class AbstractContainer implements Container, ServiceHandler{

    private final Map<Class<? extends Service>, Service> services = new ConcurrentHashMap<>();

    @Getter
    @Setter
    private ContainerManager containerManager;

    public abstract String name();

    public abstract String module();

    public abstract ContainerConfig createConfig();

    public ServiceHandler find(String moduleName) {
        return this.containerManager.find(moduleName);
    }

    @Override
    public <T extends Service> T getService(final Class<T> clazz) {
        Service service = services.get(clazz);
        if (EmptyUtils.isNotEmpty(service)) {
            return (T) service;
        }
        throw new ServiceNotFoundException(
            "Service " + clazz.getName() + " should not be provided, based on container.");

    }

    @Override
    public void register(final Class<? extends Service> serviceType,
                         final Service service) throws ServiceNotFoundException {
        if (serviceType.isInstance(service)) {
            this.services.put(serviceType, service);
        } else {
            throw new ServiceNotFoundException(serviceType + " is not implemented by " + service);
        }
    }
}
