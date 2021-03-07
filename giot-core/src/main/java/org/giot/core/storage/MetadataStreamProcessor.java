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

package org.giot.core.storage;

import org.giot.core.CoreContainer;
import org.giot.core.CoreModule;
import org.giot.core.container.ContainerManager;
import org.giot.core.storage.model.Model;
import org.giot.core.storage.model.ModelCreator;

/**
 * @author yuanguohua on 2021/3/5 17:44
 */
public class MetadataStreamProcessor implements StreamProcessor {

    @Override
    public <T extends StorageData> void create(final ContainerManager containerManager,
                                               final String name,
                                               final Class<T> clazz) {
        ModelCreator creator = containerManager.find(CoreModule.NAME, CoreContainer.DEFAULT)
                                               .getService(ModelCreator.class);
        Model model = creator.addModel(name, clazz, true, true);
        //处理model，TODO （很重要！！！！）不清楚到底是哪个容器实现了该接口
        ModelCreator.WhenCompleteListener listener = containerManager.find(StorageModule.NAME, CoreContainer.DEFAULT)
                                                                     .getService(
                                                                         ModelCreator.WhenCompleteListener.class);
        listener.listener(model);
    }
}
