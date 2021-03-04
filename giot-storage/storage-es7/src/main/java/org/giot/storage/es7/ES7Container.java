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

package org.giot.storage.es7;

import org.giot.core.container.AbstractContainer;
import org.giot.core.container.ContainerConfig;
import org.giot.core.module.ModuleDefinition;
import org.giot.core.storage.StorageModule;

/**
 * @author yuanguohua on 2021/3/4 19:48
 */
public class ES7Container extends AbstractContainer {

    private String ES7 = "elasticsearch7";

    @Override
    public String name() {
        return ES7;
    }

    @Override
    public ModuleDefinition module() {
        return find(StorageModule.NAME);
    }

    @Override
    public ContainerConfig createConfig() {
        return null;
    }

    @Override
    public void prepare() {

    }

    @Override
    public void start() {

    }

    @Override
    public void after() {

    }
}
