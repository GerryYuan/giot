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
import org.giot.core.device.storage.IDevicePropertyRelationStorageDAO;
import org.giot.core.device.storage.IDevicePropertyRelationStorageService;
import org.giot.core.storage.StorageModule;

/**
 * @author yuanguohua on 2021/4/30 10:30
 */
public class DevicePropRelationService implements IDevicePropertyRelationStorageService {

    private ContainerManager containerManager;

    private String metaDataStorage;

    private IDevicePropertyRelationStorageDAO devicePropertyRelationStorageDAO;

    public DevicePropRelationService(final ContainerManager containerManager, final String metaDataStorage) {
        this.containerManager = containerManager;
        this.metaDataStorage = metaDataStorage;
    }

    private IDevicePropertyRelationStorageDAO getDevicePropertyRelationStorageDAO() {
        if (devicePropertyRelationStorageDAO == null) {
            this.devicePropertyRelationStorageDAO = containerManager.provider(StorageModule.NAME, metaDataStorage)
                                                                    .getService(
                                                                        IDevicePropertyRelationStorageDAO.class);
        }
        return devicePropertyRelationStorageDAO;
    }

    @Override
    public boolean bind(final String deviceId, final long devicePropDefId) throws SQLException {
        return getDevicePropertyRelationStorageDAO().createDevPropRelation(deviceId, devicePropDefId);
    }
}
