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

package org.giot.core;

import org.giot.core.container.AbstractContainer;
import org.giot.core.container.Container;
import org.giot.core.container.ContainerConfig;
import org.giot.core.module.ModuleDefinition;
import org.giot.core.service.Service;

/**
 * @author Created by gerry
 * @date 2021-02-27-11:21 PM
 */
public class CoreContainer extends AbstractContainer {

    private CoreContainerConfig coreContainerConfig;

    @Override
    public String name() {
        return Container.DEFAULT;
    }

    @Override
    public ModuleDefinition module() {
        return find(CoreModule.NAME);
    }

    @Override
    public ContainerConfig createConfig() {
        this.coreContainerConfig = new CoreContainerConfig();
        return this.coreContainerConfig;
    }

    @Override
    public void prepare() {
        //获取容器管理者，然后获取容器，再根据容器获取服务
        find(CoreModule.NAME, Container.DEFAULT).getService(null);
        System.out.println(coreContainerConfig);
    }

    @Override
    public void start() {
        //每个容器在启动时，需要初始化需要的具体的类
        //比如
    }

    @Override
    public void after() {

    }

    @Override
    public Service[] requireServices() {
        return new Service[0];
    }

}
