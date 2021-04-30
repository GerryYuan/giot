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

package org.giot.network.http.service;

import org.giot.core.container.ContainerManager;
import org.giot.core.eventbus.BusFractory;
import org.giot.core.eventbus.BusInvoker;
import org.giot.core.eventbus.IInvokerService;
import org.giot.core.eventbus.InvokerAdapter;
import org.giot.core.network.NetworkModule;
import org.giot.network.http.HttpContainer;

/**
 * @author Created by gerry
 * @date 2021-04-17-22:09
 */
public class HttpInvokerService implements IInvokerService {
    private ContainerManager containerManager;

    public HttpInvokerService(final ContainerManager containerManager) {
        this.containerManager = containerManager;
    }

    @Override
    public void register() {
        InvokerAdapter invokerAdapter = containerManager.provider(NetworkModule.NAME, HttpContainer.NAME)
                                                        .getService(InvokerAdapter.class);
        BusFractory busFractory = containerManager.provider(NetworkModule.NAME, HttpContainer.NAME)
                                                  .getService(BusFractory.class);
        for (BusInvoker busInvoker : invokerAdapter.adapters()) {
            busFractory.openBus().register(busInvoker);
        }
    }
}
