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

package org.giot.storage.mysql;

import org.giot.core.container.AbstractContainer;
import org.giot.core.container.ContainerConfig;
import org.giot.core.exception.ContainerStartException;
import org.giot.core.storage.DBClient;
import org.giot.core.storage.StorageModule;
import org.giot.core.storage.model.ModelCreator;
import org.giot.storage.mysql.config.MySQLConfig;
import org.giot.storage.mysql.model.MySQLModelInstaller;

/**
 * @author yuanguohua on 2021/3/4 19:48
 */
public class MySQLContainer extends AbstractContainer {

    private String NAME = "mysql";

    private MySQLConfig mySQLConfig;

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String module() {
        return StorageModule.NAME;
    }

    @Override
    public ContainerConfig createConfig() {
        this.mySQLConfig = new MySQLConfig();
        return this.mySQLConfig;
    }

    @Override
    public void prepare() {
        MySQLClient mySQLClient = new MySQLClient(mySQLConfig.getProperties());
        super.register(DBClient.class, mySQLClient);
        super.register(ModelCreator.WhenCompleteListener.class, new MySQLModelInstaller(mySQLClient));
    }

    @Override
    public void start() throws ContainerStartException {
    }

    @Override
    public void after() {

    }
}
