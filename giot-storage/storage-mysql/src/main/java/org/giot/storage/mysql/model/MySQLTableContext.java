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

import java.util.List;
import java.util.stream.Collectors;
import org.giot.core.storage.model.Model;
import org.giot.core.storage.model.TableContext;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.DSL;

/**
 * @author yuanguohua on 2021/4/30 14:52
 */
public class MySQLTableContext implements TableContext, MySQLContext {

    private Model model;

    public MySQLTableContext(final Model model) {
        this.model = model;
    }

    @Override
    public Table getTable() {
        return DSL.table(this.model.getName());
    }

    @Override
    public List<Field> getFields() {
        return this.model.getColumns()
                         .stream()
                         .filter(s -> !s.getColumnName().equalsIgnoreCase(Model.ID))
                         .map(s -> DSL.field(s.getColumnName()))
                         .collect(Collectors.toList());
    }

    @Override
    public Field getFiled(final String column) {
        return this.model.getColumns()
                         .stream()
                         .filter(s -> s.getColumnName().equalsIgnoreCase(column))
                         .map(s -> DSL.field(s.getColumnName()))
                         .findFirst()
                         .orElseThrow(NullPointerException::new);
    }
}
