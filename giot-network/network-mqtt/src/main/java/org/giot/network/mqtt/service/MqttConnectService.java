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
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import lombok.AllArgsConstructor;
import org.giot.core.container.ContainerManager;
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
    public void connect(final Channel channel) {
        //channel在初始化时，进行跟mqtt broker进行连接操作
        //        MqttFixedHeader fixedHeader =
        //            new MqttFixedHeader(MqttMessageType.CONNECT
        //                , mqttConnectOptions.isDup()
        //                , mqttConnectOptions.getMqttQoS()
        //                , mqttConnectOptions.isRetain()
        //                , mqttConnectOptions.getRemainingLength());
        //        MqttConnectVariableHeader variableHeader =
        //            new MqttConnectVariableHeader(mqttConfig.getVersion().protocolName()
        //                , mqttConfig.getVersion().protocolLevel()
        //                , true
        //                , true
        //                , mqttConnectOptions.isWillRetain()
        //                , mqttConnectOptions.getWillQos()
        //                , mqttConnectOptions.isWillFlag()
        //                , mqttConnectOptions.isCleanSession()
        //                , mqttConfig.getKeepAliveTimeSeconds());
        //        MqttConnectPayload payload = new MqttConnectPayload(mqttConfig.getClientId()
        //            , mqttConnectOptions.getWillTopic()
        //            , mqttConnectOptions.getWillMessage()
        //            , mqttConfig.getUsername()
        //            , mqttConfig.getPassword());
        //        MqttConnectMessage connectMessage = new MqttConnectMessage(fixedHeader, variableHeader, payload);
        //        ctx.writeAndFlush(connectMessage);
        //        System.out.println("Sent CONNECT");
        //        mqttSession.initChannel(ctx.channel());
    }

    @Override
    public void ack(final Channel channel, final MqttConnAckMessage msg) throws MqttStartException {
        if (msg.variableHeader().connectReturnCode().equals(MqttConnectReturnCode.CONNECTION_ACCEPTED)) {
            // sub topic
            //            mqttSession.subscribe("$msg/up", "$msg/down");
            return;
        }
        throw new MqttStartException("mqtt connect ack error:" + msg.variableHeader().connectReturnCode());

    }
}
