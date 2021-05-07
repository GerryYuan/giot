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

package org.giot.storage.mysql.query;

import java.sql.SQLException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.giot.core.container.ContainerManager;
import org.giot.core.device.metadata.DeviceInstance;
import org.giot.core.device.query.IDeviceQueryDAO;
import org.giot.core.device.query.PageResult;
import org.giot.core.device.query.res.Devices;
import org.giot.core.storage.DBClient;
import org.giot.core.storage.StorageModule;
import org.giot.core.storage.model.Model;
import org.giot.core.storage.model.ModelOperate;
import org.giot.storage.mysql.MySQLContainer;
import org.giot.storage.mysql.model.MySQLContext;
import org.jooq.Select;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;

/**
 * @author Created by gerry
 * @date 2021-05-01-12:02
 */
@Slf4j
public class MySQLDeviceQueryDAO implements IDeviceQueryDAO {
    private ContainerManager containerManager;

    private ModelOperate modelOperate;

    private DBClient dbClient;

    public MySQLDeviceQueryDAO(final ContainerManager containerManager, final DBClient dbClient) {
        this.containerManager = containerManager;
        this.dbClient = dbClient;
    }

    private ModelOperate getModelOperate() {
        if (this.modelOperate == null) {
            this.modelOperate = containerManager.provider(StorageModule.NAME, MySQLContainer.NAME)
                                                .getService(ModelOperate.class);
        }
        return this.modelOperate;
    }

    @Override
    public boolean isExists(final String deviceId) throws SQLException {
        MySQLContext context = getModelOperate().getTable(DeviceInstance.class);
        Select select = dbClient.getDSLContext()
                                .select(DSL.field(Model.ID)).from(context.getTable())
                                .where(context.getFiled(DeviceInstance.UUID).eq(deviceId));
        if (log.isDebugEnabled()) {
            log.info("execute create device sql -> {}", select.getSQL(ParamType.INLINED));
        }
        return select.fetch().isNotEmpty();
    }

    @Override
    public PageResult<Devices> queryDevices(final int from, final int limit) throws SQLException {
        MySQLContext context = getModelOperate().getTable(DeviceInstance.class);
        List<Devices> result = dbClient.getDSLContext()
                                       .select(context.getFields())
                                       .from(context.getTable())
                                       .limit(from, limit).fetchInto(Devices.class);
        int count = dbClient.getDSLContext().fetchCount(context.getTable());
        return PageResult.build(result, count);
    }

}
