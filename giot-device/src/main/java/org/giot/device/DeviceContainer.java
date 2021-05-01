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

package org.giot.device;

import lombok.extern.slf4j.Slf4j;
import org.giot.core.CoreContainerConfig;
import org.giot.core.CoreModule;
import org.giot.core.container.AbstractContainer;
import org.giot.core.container.Container;
import org.giot.core.container.ContainerConfig;
import org.giot.core.device.DeviceModule;
import org.giot.core.device.query.IDeviceQueryService;
import org.giot.core.device.storage.IDeviceStorageService;
import org.giot.device.service.DeviceService;

/**
 * @author Created by gerry
 * @date 2021-03-01-9:46 PM
 */
@Slf4j
public class DeviceContainer extends AbstractContainer {

    private DeviceContainerConfig containerConfig;

    public DeviceContainer() {
        this.containerConfig = new DeviceContainerConfig();
    }

    @Override
    public String name() {
        return Container.DEFAULT;
    }

    @Override
    public String module() {
        return DeviceModule.NAME;
    }

    @Override
    public ContainerConfig createConfigIfAbsent() {
        return containerConfig;
    }

    @Override
    public void prepare() {
        CoreContainerConfig config = super.find(CoreModule.NAME).getConfig();
        //register storage & query service
        super.register(
            IDeviceStorageService.class, new DeviceService(getContainerManager(), config.getMetaDataStorage()));
        super.register(
            IDeviceQueryService.class, new DeviceService(getContainerManager(), config.getMetaDataStorage()));
    }

    @Override
    public void start() {
        log.info("DeviceContainer started...");
    }

    @Override
    public void after() {

    }

}
