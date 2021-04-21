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

package org.giot.network.mqtt.dispatcher;

import org.giot.core.container.ContainerManager;
import org.giot.core.device.DeviceContext;
import org.giot.core.device.DeviceHeader;
import org.giot.core.device.DeviceStatus;
import org.giot.core.device.payload.PayloadConverter;
import org.giot.core.device.serializer.Serializer;
import org.giot.core.network.NetworkModule;
import org.giot.core.network.RouteUrl;
import org.giot.core.network.URLMappings;
import org.giot.network.mqtt.MqttContainer;

/**
 * 属性结构转换
 * <p>
 * { "deviceId": "test001", "isConnected": false }
 * </p>
 *
 * @author yuanguohua on 2021/3/26 11:08
 */
public class MqttConnectedConverter implements PayloadConverter {

    private Serializer serializer;

    private URLMappings urlMappings;

    private ContainerManager containerManager;

    public MqttConnectedConverter(final ContainerManager containerManager, final Serializer serializer) {
        this.containerManager = containerManager;
        this.serializer = serializer;
    }

    @Override
    public DeviceStatus converter(final DeviceContext context) {
        DeviceStatus deviceStatus = serializer.deserialize(context.getPayload(), DeviceStatus.class);
        deviceStatus.setHeader(context.getHeader());
        return deviceStatus;
    }

    @Override
    public boolean supports(final DeviceHeader header) {
        if (urlMappings == null) {
            this.urlMappings = containerManager.find(NetworkModule.NAME, MqttContainer.NAME)
                                               .getService(URLMappings.class);
        }
        String url = urlMappings.mapping(header.getVersion(), RouteUrl.CONNECTED.getRoute());
        return url.equalsIgnoreCase(header.getTopic());
    }
}
