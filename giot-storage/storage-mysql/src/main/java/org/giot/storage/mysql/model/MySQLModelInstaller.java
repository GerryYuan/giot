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

import com.google.common.base.Joiner;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.giot.core.storage.DBClient;
import org.giot.core.storage.model.IndexDef;
import org.giot.core.storage.model.Model;
import org.giot.core.storage.model.ModelColumn;
import org.giot.core.storage.model.ModelInstaller;
import org.jooq.Constraint;
import org.jooq.CreateIndexIncludeStep;
import org.jooq.CreateTableColumnStep;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.BOOLEAN;
import static org.jooq.impl.SQLDataType.DOUBLE;
import static org.jooq.impl.SQLDataType.FLOAT;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.TINYINT;

/**
 * @author Created by gerry
 * @date 2021-03-07-11:02 PM
 */
@Slf4j
public class MySQLModelInstaller extends ModelInstaller {

    private DBClient dbClient;

    public MySQLModelInstaller(final DBClient dbClient) {
        this.dbClient = dbClient;
    }

    @Override
    public void createTable(final Model model) throws SQLException {
        DSLContext dsl = dbClient.getDSLContext();
        CreateTableColumnStep table = dsl.createTableIfNotExists(model.getName())
                                         .column(Model.ID, BIGINT.length(20).identity(true).nullable(false));
        table.comment(model.getDes());
        for (ModelColumn column : model.getColumns()) {
            table.column(transform(column));
        }
        table.constraints(DSL.primaryKey(Model.ID)).constraints(uniques(model.getColumns()));
        if (log.isDebugEnabled()) {
            log.info("create table {}, sql: {} ", model.getName(), table.getSQL());
        }
        table.execute();
    }

    @Override
    public void createIndexes(String table, List<IndexDef> indexDefs) throws SQLException {
        DSLContext dsl = dbClient.getDSLContext();
        for (IndexDef indexDef : indexDefs) {
            String indexName = table.toUpperCase() + "_" + Joiner.on("_").join(indexDef.getFieldNames()) + "_IDX";
            CreateIndexIncludeStep indexIncludeStep;
            switch (indexDef.getIndexType()) {
                case unique:
                    indexIncludeStep = dsl.createUniqueIndex(indexName).on(table, indexDef.getFieldNames());
                    break;
                default:
                    indexIncludeStep = dsl.createIndex(indexName).on(table, indexDef.getFieldNames());
                    break;
            }
            if (log.isDebugEnabled()) {
                log.info("create index for table {}, sql: {} ", table, indexIncludeStep.getSQL());
            }
            indexIncludeStep.execute();
        }
    }

    @Override
    public boolean isExists(final String table) throws SQLException {
        ResultSet rset = dbClient.getConnection().getMetaData().getTables(null, null, table, null);
        if (rset.next()) {
            return true;
        }
        return false;
    }

    private List<Constraint> uniques(List<ModelColumn> columns) {
        return columns.stream()
                      .filter(c -> c.isUnique())
                      .map(c -> DSL.unique(c.getColumnName()))
                      .collect(Collectors.toList());
    }

    private Field transform(ModelColumn column) {
        Class<?> type = column.getType();
        String columnName = column.getColumnName();
        int length = column.getLength();
        boolean isNull = column.isNull();
        if (Integer.class.equals(type) || int.class.equals(type)) {
            return DSL.field(columnName, INTEGER.length(length).nullable(isNull));
        } else if (Long.class.equals(type) || long.class.equals(type)) {
            return DSL.field(columnName, BIGINT.length(length).nullable(isNull));
        } else if (Double.class.equals(type) || double.class.equals(type)) {
            return DSL.field(columnName, DOUBLE.length(length).nullable(isNull));
        } else if (String.class.equals(type)) {
            return DSL.field(columnName, SQLDataType.VARCHAR(length).nullable(isNull));
        } else if (Byte.class.equals(type)) {
            return DSL.field(columnName, TINYINT.length(length).nullable(isNull));
        } else if (Float.class.equals(type) || float.class.equals(type)) {
            return DSL.field(columnName, FLOAT.length(length).nullable(isNull));
        } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            return DSL.field(columnName, BOOLEAN.length(length).nullable(isNull));
        } else if (type.isEnum()) {
            return DSL.field(columnName, SQLDataType.VARCHAR(length).nullable(isNull));
        } else {
            throw new IllegalArgumentException("Unsupported data type: " + type.getName());
        }
    }

}
