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

import org.giot.core.CoreModule;
import org.giot.core.container.ContainerManager;
import org.giot.core.storage.StorageData;
import org.giot.core.storage.model.Model;
import org.giot.core.storage.model.ModelCreator;
import org.giot.core.storage.model.ModelManager;
import org.giot.core.storage.model.ModelOperate;
import org.giot.core.storage.model.TableContext;

/**
 * @author yuanguohua on 2021/4/30 14:29
 */
public class MySQLModelOperate implements ModelOperate {

    private ContainerManager containerManager;

    private ModelManager modelManager;

    public MySQLModelOperate(final ContainerManager containerManager) {
        this.containerManager = containerManager;
    }

    private ModelManager getModelManager() {
        if (this.modelManager == null) {
            this.modelManager = (ModelManager) containerManager.provider(CoreModule.NAME)
                                                               .getService(ModelCreator.class);
        }
        return this.modelManager;
    }

    @Override
    public <T extends StorageData, R extends TableContext> R getTable(final Class<T> clazz) {
        Model model = getModelManager().getModel(clazz);
        return (R) new MySQLTableContext(model);
    }
}
