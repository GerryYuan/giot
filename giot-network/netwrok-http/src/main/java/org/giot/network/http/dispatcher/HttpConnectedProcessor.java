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

package org.giot.network.http.dispatcher;

import org.giot.core.container.ContainerManager;
import org.giot.core.device.source.DeviceStatus;
import org.giot.core.eventbus.BusFractory;
import org.giot.core.network.AbstractSourceProcessor;
import org.giot.core.network.MsgVersion;
import org.giot.core.network.NetworkModule;
import org.giot.core.network.RouteUrl;
import org.giot.core.network.annotation.Processor;
import org.giot.network.http.HttpContainer;

/**
 * @author yuanguohua on 2021/3/22 15:46
 */
@Processor(route = RouteUrl.CONNECTED, version = MsgVersion.v1)
public class HttpConnectedProcessor extends AbstractSourceProcessor<DeviceStatus> implements HttpProcessor<DeviceStatus> {

    public HttpConnectedProcessor(final ContainerManager containerManager) {
        super(containerManager);
    }

    @Override
    public void invoke(final DeviceStatus deviceStatus) {
        BusFractory busFractory = getContainerManager().provider(NetworkModule.NAME, HttpContainer.NAME)
                                                       .getService(BusFractory.class);
        busFractory.openBus().post(deviceStatus);
    }
}
