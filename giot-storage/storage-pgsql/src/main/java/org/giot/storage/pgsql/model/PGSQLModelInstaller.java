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

package org.giot.storage.pgsql.model;

import java.sql.SQLException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.giot.core.storage.model.IndexDef;
import org.giot.core.storage.model.Model;
import org.giot.core.storage.model.ModelInstaller;

/**
 * @author Created by gerry
 * @date 2021-03-07-11:02 PM
 */
@Slf4j
public class PGSQLModelInstaller extends ModelInstaller {

    @Override
    public void createTable(final Model model) throws SQLException {
        log.info("pgsql model " + model + " installing....");
    }

    @Override
    public void createIndexes(final String table, final List<IndexDef> indexDefs) throws SQLException {

    }

    @Override
    public boolean isExists(final String table) throws SQLException {
        return false;
    }

}
