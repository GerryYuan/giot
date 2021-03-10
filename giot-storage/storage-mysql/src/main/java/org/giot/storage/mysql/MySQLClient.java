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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.giot.core.storage.DBClient;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

/**
 * @author Created by gerry
 * @date 2021-03-09-10:53 PM
 */
public class MySQLClient implements DBClient {

    private HikariConfig hikariConfig;

    private HikariDataSource hikariDataSource;

    private DSLContext dslContext;

    public MySQLClient(final Properties properties) {
        this.hikariConfig = new HikariConfig(properties);
        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    @Override
    public DSLContext getDSLContext() throws SQLException {
        if (dslContext == null) {
            Connection connection = hikariDataSource.getConnection();
            dslContext = DSL.using(connection, SQLDialect.MYSQL);
        }
        return dslContext;
    }
}
