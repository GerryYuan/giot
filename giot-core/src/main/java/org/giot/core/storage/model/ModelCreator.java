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

package org.giot.core.storage.model;

import java.sql.SQLException;
import java.util.List;
import org.giot.core.container.Service;
import org.giot.core.storage.StorageData;

/**
 * when storage container start, build modelCreateor#creator many models.
 *
 * @author Created by gerry
 * @date 2021-03-01-10:17 PM
 */
public interface ModelCreator extends Service {

    /**
     * add model
     *
     * @param name      table name
     * @param des       table desctiption
     * @param clazz     pojo class
     * @param indexDefs indexs
     * @return
     */
    Model addModel(String name, String des, Class<? extends StorageData> clazz, List<IndexDef> indexDefs);

    interface WhenCompleteListener extends Service {
        /**
         * model created after, execute Listener
         */
        void listener(Model model) throws SQLException;
    }
}
