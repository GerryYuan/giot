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

package org.giot.storage.mysql.model;

import java.sql.SQLException;
import org.giot.core.storage.model.Model;
import org.giot.core.storage.model.ModelColumn;
import org.giot.core.storage.model.ModelInstaller;
import org.giot.storage.mysql.MySQLClient;
import org.jooq.CreateTableColumnStep;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import static org.jooq.impl.SQLDataType.BIGINT;

/**
 * @author Created by gerry
 * @date 2021-03-07-11:02 PM
 */
public class MySQLModelInstaller extends ModelInstaller {

    private MySQLClient mySQLClient;

    public MySQLModelInstaller(final MySQLClient mySQLClient) {
        this.mySQLClient = mySQLClient;
    }

    @Override
    public void createTable(final Model model) throws SQLException {
        DSLContext dsl = mySQLClient.getDSLContext();
        CreateTableColumnStep table = dsl.createTableIfNotExists(model.getName()).column(Model.ID, BIGINT.length(20));
        for (ModelColumn column : model.getColumns()) {
            table.column(column.getColumnName(), transform(column.getType(), column.getLength()))
                 .comment(column.getDes());
        }
        table.constraints(DSL.primaryKey(Model.ID)).execute();
    }

    private DataType transform(Class<?> type, int length) {
        if (Integer.class.equals(type) || int.class.equals(type)) {
            return SQLDataType.INTEGER;
        } else if (Long.class.equals(type) || long.class.equals(type)) {
            return SQLDataType.BIGINT;
        } else if (Double.class.equals(type) || double.class.equals(type)) {
            return SQLDataType.DOUBLE;
        } else if (String.class.equals(type)) {
            return SQLDataType.VARCHAR(length);
        } else if (Byte.class.equals(type)) {
            return SQLDataType.TINYINT;
        } else if (Float.class.equals(type) || float.class.equals(type)) {
            return SQLDataType.FLOAT;
        } else {
            throw new IllegalArgumentException("Unsupported data type: " + type.getName());
        }
    }
}
