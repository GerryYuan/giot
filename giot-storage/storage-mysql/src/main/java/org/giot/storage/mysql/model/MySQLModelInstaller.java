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
import org.jooq.Field;
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
            table.column(transform(column));
        }
        table.constraints(DSL.primaryKey(Model.ID)).execute();
    }

    private Field transform(ModelColumn column) {
        Class<?> type = column.getType();
        String columnName = column.getColumnName();
        String des = column.getDes();
        int length = column.getLength();
        boolean isNull = column.isNull();
        if (Integer.class.equals(type) || int.class.equals(type)) {
            return DSL.field(columnName, SQLDataType.INTEGER.nullable(isNull), DSL.comment(des));
        } else if (Long.class.equals(type) || long.class.equals(type)) {
            return DSL.field(columnName, SQLDataType.BIGINT.nullable(isNull), DSL.comment(des));
        } else if (Double.class.equals(type) || double.class.equals(type)) {
            return DSL.field(columnName, SQLDataType.DOUBLE.nullable(isNull), DSL.comment(des));
        } else if (String.class.equals(type)) {
            return DSL.field(columnName, SQLDataType.VARCHAR(length).nullable(isNull), DSL.comment(des));
        } else if (Byte.class.equals(type)) {
            return DSL.field(columnName, SQLDataType.TINYINT.nullable(isNull), DSL.comment(des));
        } else if (Float.class.equals(type) || float.class.equals(type)) {
            return DSL.field(columnName, SQLDataType.FLOAT.nullable(isNull), DSL.comment(des));
        } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            return DSL.field(columnName, SQLDataType.BOOLEAN.nullable(isNull), DSL.comment(des));
        } else {
            throw new IllegalArgumentException("Unsupported data type: " + type.getName());
        }
    }
}
