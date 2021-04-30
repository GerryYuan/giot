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

package org.giot.network.mqtt.eventbus;

import java.util.List;
import java.util.stream.Collectors;
import org.giot.core.CoreModule;
import org.giot.core.container.ContainerManager;
import org.giot.core.eventbus.BusInvoker;
import org.giot.core.eventbus.InvokerAdapter;
import org.giot.core.scanner.ListenerManager;

/**
 * @author Created by gerry
 * @date 2021-04-17-21:49
 */
public class MqttInvokerAdapter implements InvokerAdapter {

    private ContainerManager containerManager;

    private ListenerManager<BusInvoker> listenerManager;

    public MqttInvokerAdapter(final ContainerManager containerManager) {
        this.containerManager = containerManager;
    }

    @Override
    public List<BusInvoker> adapters() {
        if (listenerManager == null) {
            this.listenerManager = containerManager.provider(CoreModule.NAME).getService(ListenerManager.class);
        }
        return listenerManager.all().stream().filter(busInvoker -> supports(busInvoker)).collect(
            Collectors.toList());
    }

    @Override
    public boolean supports(final BusInvoker busInvoker) {
        return busInvoker instanceof MqttBusInvoker;
    }
}
