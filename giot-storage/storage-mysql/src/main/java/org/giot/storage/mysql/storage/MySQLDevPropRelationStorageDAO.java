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

package org.giot.storage.mysql.storage;

import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.giot.core.container.ContainerManager;
import org.giot.core.device.metadata.DevicePropertyRelation;
import org.giot.core.device.storage.IDevicePropertyRelationStorageDAO;
import org.giot.core.storage.DBClient;
import org.giot.core.storage.StorageModule;
import org.giot.core.storage.model.ModelOperate;
import org.giot.storage.mysql.MySQLContainer;
import org.giot.storage.mysql.model.MySQLContext;
import org.jooq.Insert;
import org.jooq.conf.ParamType;

/**
 * @author yuanguohua on 2021/5/8 15:39
 */
@Slf4j
public class MySQLDevPropRelationStorageDAO implements IDevicePropertyRelationStorageDAO {

    private ContainerManager containerManager;

    private ModelOperate modelOperate;

    private DBClient dbClient;

    public MySQLDevPropRelationStorageDAO(final ContainerManager containerManager, final DBClient dbClient) {
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
    public boolean createDevPropRelation(final String deviceId, final long devicePropDefId) throws SQLException {
        MySQLContext context = getModelOperate().getTable(DevicePropertyRelation.class);
        Insert insert = dbClient.getDSLContext()
                                .insertInto(context.getTable())
                                .columns(context.getFields())
                                .values(
                                    deviceId, devicePropDefId, System.currentTimeMillis(), System.currentTimeMillis());
        if (log.isDebugEnabled()) {
            log.info("execute create device sql -> {}", insert.getSQL(ParamType.INLINED));
        }
        return insert.execute() == 1;
    }
}
