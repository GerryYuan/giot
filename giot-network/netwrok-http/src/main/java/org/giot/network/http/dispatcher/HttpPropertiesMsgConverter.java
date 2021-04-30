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
import org.giot.core.device.DeviceContext;
import org.giot.core.device.DeviceHeader;
import org.giot.core.device.source.DevicePropertiesMsg;
import org.giot.core.device.payload.PayloadConverter;
import org.giot.core.network.serializer.Serializer;
import org.giot.core.network.NetworkModule;
import org.giot.core.network.RouteUrl;
import org.giot.core.network.URLMappings;
import org.giot.network.http.HttpContainer;

/**
 * http property msg convert
 * <p>
 * { "deviceId": "test001", "properties": { "temperature": 36.5, "hest": "finiot" } }
 * </p>
 *
 * @author yuanguohua on 2021/3/26 11:08
 */
public class HttpPropertiesMsgConverter implements PayloadConverter {

    private Serializer serializer;

    private URLMappings urlMappings;

    private ContainerManager containerManager;

    public HttpPropertiesMsgConverter(final ContainerManager containerManager, final Serializer serializer) {
        this.containerManager = containerManager;
        this.serializer = serializer;
    }

    @Override
    public DevicePropertiesMsg converter(final DeviceContext context) {
        DevicePropertiesMsg propertiesMsg = serializer.deserialize(context.getPayload(), DevicePropertiesMsg.class);
        propertiesMsg.setHeader(context.getHeader());
        return propertiesMsg;
    }

    @Override
    public boolean supports(final DeviceHeader header) {
        if (urlMappings == null) {
            this.urlMappings = containerManager.provider(NetworkModule.NAME, HttpContainer.NAME)
                                               .getService(URLMappings.class);
        }
        String url = urlMappings.mapping(header.getVersion(), RouteUrl.REPORT_PROPERTIES.getRoute());
        return url.equalsIgnoreCase(header.getTopic());
    }
}
