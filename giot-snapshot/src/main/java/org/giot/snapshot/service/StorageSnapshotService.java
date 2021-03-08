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

package org.giot.snapshot.service;

import lombok.AllArgsConstructor;
import org.giot.core.container.ContainerManager;
import org.giot.core.service.Service;
import org.giot.core.storage.StorageModule;
import org.giot.snapshot.SnapshotConfig;

/**
 * @author Created by gerry
 * @date 2021-03-08-10:52 PM
 */
@AllArgsConstructor
public class StorageSnapshotService implements IStorageSnapshotService {

    private SnapshotConfig snapshotConfig;

    private ContainerManager containerManager;

    @Override
    public <T extends Service> T getService(final Class<T> clazz) {
        //如果从默认的storage模块中找到容器，则直接返回
        boolean has = containerManager.has(StorageModule.NAME, snapshotConfig.getStorage());
        if (has) {
            return containerManager.find(StorageModule.NAME).getService(clazz);
        } else {
            //如果当前默认存储模块找不到，则需要注册一个快照容器，然后返回
            //TODO 注册一个快照模块
            return null;
        }
    }
}
