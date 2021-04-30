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

package org.giot.device.service;

import java.sql.SQLException;
import org.giot.core.container.ContainerManager;
import org.giot.core.device.enums.DeviceType;
import org.giot.core.device.storage.IDeviceStorageDAO;
import org.giot.core.device.storage.IDeviceStorageService;
import org.giot.core.storage.StorageModule;

/**
 * @author yuanguohua on 2021/4/30 10:30
 */
public class DeviceStorageService implements IDeviceStorageService {

    private ContainerManager containerManager;

    private String metaDataStorage;

    private IDeviceStorageDAO deviceStorageDAO;

    public DeviceStorageService(final ContainerManager containerManager, final String metaDataStorage) {
        this.containerManager = containerManager;
        this.metaDataStorage = metaDataStorage;
    }

    private IDeviceStorageDAO getDeviceStorageDAO() {
        if (deviceStorageDAO == null) {
            this.deviceStorageDAO = containerManager.provider(StorageModule.NAME, metaDataStorage)
                                                    .getService(IDeviceStorageDAO.class);
        }
        return deviceStorageDAO;
    }

    @Override
    public boolean createDevice(final String name, final String des, final DeviceType deviceType) throws SQLException {
        return getDeviceStorageDAO().createDevice(name, des, deviceType);
    }
}
