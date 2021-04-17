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

package org.giot.core.eventbus;

import org.giot.core.CoreModule;
import org.giot.core.container.ContainerManager;

/**
 * @author yuanguohua on 2021/4/16 17:10
 */
public class BusInvokerCreator implements InvokerCreator {

    private ContainerManager containerManager;

    private InvokerInstaller invokerInstaller;

    public BusInvokerCreator(final ContainerManager containerManager) {
        this.containerManager = containerManager;
    }

    @Override
    public void create(final Class<? extends BusInvoker> clazz) throws IllegalAccessException, InstantiationException {
        if (invokerInstaller == null) {
            this.invokerInstaller = (InvokerInstaller) containerManager.find(CoreModule.NAME)
                                                                       .getService(InvokerManager.class);
        }
        invokerInstaller.listener(clazz);
    }
}
