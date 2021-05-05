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
import java.util.Objects;
import org.giot.core.container.ContainerManager;
import org.giot.core.device.enums.DeviceType;
import org.giot.core.device.query.IDeviceQueryDAO;
import org.giot.core.device.query.IDeviceQueryService;
import org.giot.core.device.query.PageResult;
import org.giot.core.device.query.Pagination;
import org.giot.core.device.query.res.Devices;
import org.giot.core.device.storage.IDeviceStorageDAO;
import org.giot.core.device.storage.IDeviceStorageService;
import org.giot.core.storage.StorageModule;
import org.giot.core.utils.StringUtils;

/**
 * @author yuanguohua on 2021/4/30 10:30
 */
public class DeviceService implements IDeviceStorageService, IDeviceQueryService {

    private ContainerManager containerManager;

    private String metaDataStorage;

    private IDeviceStorageDAO deviceStorageDAO;

    private IDeviceQueryDAO deviceQueryDAO;

    public DeviceService(final ContainerManager containerManager, final String metaDataStorage) {
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

    private IDeviceQueryDAO getDeviceQueryDAO() {
        if (deviceQueryDAO == null) {
            this.deviceQueryDAO = containerManager.provider(StorageModule.NAME, metaDataStorage)
                                                  .getService(IDeviceQueryDAO.class);
        }
        return deviceQueryDAO;
    }

    @Override
    public boolean isExists(final String deviceId) throws SQLException {
        return getDeviceQueryDAO().isExists(deviceId);
    }

    @Override
    public PageResult<Devices> queryDevices(final Pagination pagination) throws SQLException {
        Pagination.Page page = Pagination.toPage(pagination);
        return getDeviceQueryDAO().queryDevices(page.getFrom(), page.getLimit());
    }

    @Override
    public boolean createDevice(final String name, final String des, final DeviceType deviceType) throws SQLException {
        return createDevice(StringUtils.createUUID(), name, des, deviceType);
    }

    @Override
    public boolean createDevice(final String deviceId,
                                final String name,
                                final String des,
                                final DeviceType deviceType) throws SQLException {
        return getDeviceStorageDAO().createDevice(deviceId, name, des, deviceType);
    }

    @Override
    public boolean onlineDevice(final String deviceId) throws SQLException {
        Objects.requireNonNull(deviceId);
        return getDeviceStorageDAO().onlineDevice(deviceId);
    }
}
