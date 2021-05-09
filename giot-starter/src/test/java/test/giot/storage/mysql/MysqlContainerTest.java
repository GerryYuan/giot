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

package test.giot.storage.mysql;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.giot.core.container.ContainerManager;
import org.giot.core.device.DeviceModule;
import org.giot.core.device.enums.DeviceProperFeildType;
import org.giot.core.device.enums.DeviceType;
import org.giot.core.device.query.IDeviceQueryService;
import org.giot.core.device.query.PageResult;
import org.giot.core.device.query.Pagination;
import org.giot.core.device.query.res.Devices;
import org.giot.core.device.storage.IDevicePropertyDefStorageService;
import org.giot.core.device.storage.IDeviceStorageService;
import org.giot.core.device.storage.req.DevicePropDefContext;
import org.giot.core.exception.ContainerConfigException;
import org.giot.core.exception.ContainerStartException;
import org.giot.core.loader.ModuleResourceLoader;
import org.giot.core.loader.ResourceLoader;
import org.giot.core.module.ModuleConfiguration;
import org.giot.core.module.ModuleManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author yuanguohua on 2021/4/30 15:29
 */
public class MysqlContainerTest {

    private ContainerManager containerManager;

    private IDeviceStorageService deviceStorageService;

    private IDevicePropertyDefStorageService devicePropertyDefStorageService;

    private IDeviceQueryService deviceQueryService;

    @Before
    public void start() throws FileNotFoundException, ContainerStartException, ContainerConfigException {
        ResourceLoader resourceLoader = new ModuleResourceLoader("application.yml");
        ModuleConfiguration moduleConfiguration = resourceLoader.load();
        ModuleManager moduleManager = new ModuleManager();
        moduleManager.start(moduleConfiguration);
        this.containerManager = moduleManager.getContainerManager();
        this.deviceStorageService = containerManager.provider(DeviceModule.NAME)
                                                    .getService(IDeviceStorageService.class);
        this.deviceQueryService = containerManager.provider(DeviceModule.NAME)
                                                  .getService(IDeviceQueryService.class);
        this.devicePropertyDefStorageService = containerManager.provider(DeviceModule.NAME)
                                                               .getService(IDevicePropertyDefStorageService.class);
    }

    @Test
    public void createDevice() throws SQLException {
        String deviceId = "fc8ffa765bf7482285e3994e55ea7302";
        boolean isExists = deviceQueryService.isExists(deviceId);
        if (!isExists) {
            Assert.assertTrue(
                deviceStorageService.createDevice(deviceId, "test-1", "test device des", DeviceType.NORMAL));
        }
    }

    @Test
    public void onlineDevice() throws SQLException {
        IDeviceStorageService deviceStorageService = containerManager.provider(DeviceModule.NAME)
                                                                     .getService(IDeviceStorageService.class);
        String deviceId = "cc8ffa765bf7482285e3994e55ea7302";
        boolean isExists = deviceQueryService.isExists(deviceId);
        if (!isExists) {
            Assert.assertTrue(
                deviceStorageService.createDevice(deviceId, "test-1", "test device des", DeviceType.NORMAL));
        }
        Assert.assertTrue(deviceStorageService.onlineDevice(deviceId));
    }

    @Test
    public void offlineDevice() throws SQLException {
        IDeviceStorageService deviceStorageService = containerManager.provider(DeviceModule.NAME)
                                                                     .getService(IDeviceStorageService.class);
        String deviceId = "cc8ffa765bf7482285e3994e55ea7302";
        boolean isExists = deviceQueryService.isExists(deviceId);
        if (!isExists) {
            Assert.assertTrue(
                deviceStorageService.createDevice(deviceId, "test-1", "test device des", DeviceType.NORMAL));
        }
        Assert.assertTrue(deviceStorageService.offlineDevice(deviceId));
    }

    @Test
    public void queryDevices() throws SQLException {
        Pagination pagination = new Pagination(0, 10, false);
        PageResult<Devices> result = deviceQueryService.queryDevices(pagination);
        Assert.assertNotNull(result);
    }

    @Test
    public void createDevPropDef() throws SQLException, IllegalAccessException {
        DevicePropDefContext devicePropDefContext = new DevicePropDefContext();
        devicePropDefContext.setName("things model name");
        List<DevicePropDefContext.PropDef> propDefs = new ArrayList<>(2);
        DevicePropDefContext.PropDef propDef = new DevicePropDefContext.PropDef();
        propDef.setId("field1");
        propDef.setDes("field1 des");
        propDef.setName("field name");
        DevicePropDefContext.VauleType vauleType = new DevicePropDefContext.VauleType();
        vauleType.setType(DeviceProperFeildType.string);
        vauleType.setUnit("/turn");
        vauleType.setLength(255);
        propDef.setValueType(vauleType);
        DevicePropDefContext.Expands expands = new DevicePropDefContext.Expands();
        expands.setReadOnly(true);
        expands.setReport(true);
        propDef.setExpands(expands);
        propDefs.add(propDef);
        devicePropDefContext.setPropDefs(propDefs);
        Assert.assertTrue(devicePropertyDefStorageService.createDevPropDef(devicePropDefContext));
    }
}
