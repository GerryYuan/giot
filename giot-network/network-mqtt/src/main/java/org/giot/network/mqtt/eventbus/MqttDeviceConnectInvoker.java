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

package org.giot.network.mqtt.eventbus;

import com.google.common.eventbus.Subscribe;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.giot.core.CoreContainerConfig;
import org.giot.core.CoreModule;
import org.giot.core.container.ContainerManager;
import org.giot.core.device.source.DeviceStatus;
import org.giot.core.device.storage.IDeviceStorageService;
import org.giot.core.eventbus.annotation.Invoker;
import org.giot.core.storage.StorageModule;

/**
 * @author Created by gerry
 * @date 2021-04-17-22:26
 */
@Slf4j
@Invoker
public class MqttDeviceConnectInvoker implements MqttBusInvoker {

    private ContainerManager containerManager;

    public MqttDeviceConnectInvoker(final ContainerManager containerManager) {
        this.containerManager = containerManager;
    }

    @Subscribe
    public void deviceConnect(DeviceStatus deviceStatus) {
        CoreContainerConfig containerConfig = containerManager.find(CoreModule.NAME).getConfig();
        IDeviceStorageService storageService = containerManager.provider(
            StorageModule.NAME, containerConfig.getMetaDataStorage()).getService(
            IDeviceStorageService.class);
        try {
            if (deviceStatus.isConnected()) {
                storageService.onlineDevice(deviceStatus.getDeviceId());
            } else {
                storageService.offlineDevice(deviceStatus.getDeviceId());
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }
}
