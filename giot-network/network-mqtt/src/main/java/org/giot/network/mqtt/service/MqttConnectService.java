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

package org.giot.network.mqtt.service;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import io.netty.handler.codec.mqtt.MqttConnectPayload;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttConnectVariableHeader;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessageFactory;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttQoS;
import java.nio.charset.Charset;
import lombok.AllArgsConstructor;
import org.giot.core.container.ContainerManager;
import org.giot.core.network.NetworkModule;
import org.giot.network.mqtt.MqttContainer;
import org.giot.network.mqtt.config.MqttConfig;
import org.giot.network.mqtt.exception.MqttStartException;

/**
 * @author Created by gerry
 * @date 2021-03-14-9:28 PM
 */
@AllArgsConstructor
public class MqttConnectService implements IMqttConnectService {

    private MqttConfig config;

    private ContainerManager containerManager;

    @Override
    public void disConnect(final Channel channel) {
        //重连
        channel.close();
        //todo 负责通知下游
    }

    @Override
    public void connect(final Channel channel) {
        MqttFixedHeader fixedHeader = new MqttFixedHeader(
            MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttConnectVariableHeader variableHeader = new MqttConnectVariableHeader(
            config.getVersion().protocolName(),
            config.getVersion().protocolLevel(),
            true,
            true,
            false,
            0,
            false,
            config.isCleanSession(),
            config.getKeepAliveTimeSeconds()
        );
        MqttConnectPayload payload = new MqttConnectPayload(
            config.getClientId(), null, null,
            config.getUserName(),
            config.getPassword().getBytes(Charset.defaultCharset())
        );
        channel.writeAndFlush(MqttMessageFactory.newMessage(fixedHeader, variableHeader, payload));
    }

    @Override
    public void ack(final Channel channel, final MqttConnAckMessage msg) throws MqttStartException {
        if (msg.variableHeader().connectReturnCode().equals(MqttConnectReturnCode.CONNECTION_ACCEPTED)) {
            IMqttSubService subService = containerManager.find(NetworkModule.NAME, MqttContainer.NAME)
                                                         .getService(IMqttSubService.class);
            subService.sub(channel);
            return;
        }
        throw new MqttStartException("mqtt connect ack error: " + msg.variableHeader().connectReturnCode());

    }
}
