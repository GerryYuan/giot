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
import java.util.List;
import java.util.stream.Collectors;
import org.giot.core.container.ContainerManager;
import org.giot.core.device.metadata.DevicePropertyDef;
import org.giot.core.device.storage.IDevicePropertyDefStorageDAO;
import org.giot.core.device.storage.IDevicePropertyDefStorageService;
import org.giot.core.device.storage.req.DevicePropDefContext;
import org.giot.core.storage.StorageModule;

/**
 * @author yuanguohua on 2021/4/30 10:30
 */
public class DevicePropDefService implements IDevicePropertyDefStorageService {

    private ContainerManager containerManager;

    private String metaDataStorage;

    private IDevicePropertyDefStorageDAO devicePropertyDefStorageDAO;

    public DevicePropDefService(final ContainerManager containerManager, final String metaDataStorage) {
        this.containerManager = containerManager;
        this.metaDataStorage = metaDataStorage;
    }

    private IDevicePropertyDefStorageDAO getDevicePropertyDefStorageDAO() {
        if (devicePropertyDefStorageDAO == null) {
            this.devicePropertyDefStorageDAO = containerManager.provider(StorageModule.NAME, metaDataStorage)
                                                               .getService(IDevicePropertyDefStorageDAO.class);
        }
        return devicePropertyDefStorageDAO;
    }

    @Override
    public boolean createDevPropDef(final DevicePropDefContext devicePropDefContext) throws SQLException, IllegalAccessException {
        List<DevicePropertyDef> devicePropertyDefs = devicePropDefContext.getPropDefs().stream().map(propDef -> {
            DevicePropertyDef devicePropertyDef = new DevicePropertyDef();
            devicePropertyDef.setName(devicePropDefContext.getName());
            devicePropertyDef.setFieldId(propDef.getId());
            devicePropertyDef.setFieldName(propDef.getName());
            devicePropertyDef.setFieldDes(propDef.getDes());
            devicePropertyDef.setFieldIsRead(propDef.getExpands().isReadOnly());
            devicePropertyDef.setFieldIsWrite(propDef.getExpands().isReport());
            devicePropertyDef.setFieldType(propDef.getValueType().getType());
            devicePropertyDef.setFieldUnit(propDef.getValueType().getUnit());
            devicePropertyDef.setFieldLength(propDef.getValueType().getLength());
            devicePropertyDef.setCreateTime(System.currentTimeMillis());
            devicePropertyDef.setUpdateTime(System.currentTimeMillis());
            return devicePropertyDef;
        }).collect(Collectors.toList());
        return getDevicePropertyDefStorageDAO().createDevPropDefs(devicePropertyDefs);
    }
}
