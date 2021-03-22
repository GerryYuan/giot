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
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;
import org.giot.core.container.ContainerManager;
import org.giot.core.network.MsgVersion;
import org.giot.core.network.NetworkModule;
import org.giot.core.network.SourceDispatcher;
import org.giot.network.mqtt.MqttContainer;
import org.giot.network.mqtt.exception.MqttStartException;

/**
 * @author yuanguohua on 2021/3/17 19:30
 */
@Slf4j
public class MqttPubService implements IMqttPubService {

    private ContainerManager containerManager;

    private SourceDispatcher sourceDispatcher;

    public MqttPubService(final ContainerManager containerManager) {
        this.containerManager = containerManager;
    }

    @Override
    public void pub(final Channel channel, final MqttPublishMessage msg) throws MqttStartException {
        if (sourceDispatcher == null) {
            this.sourceDispatcher = this.containerManager.find(NetworkModule.NAME, MqttContainer.NAME)
                                                         .getService(SourceDispatcher.class);
        }
        switch (msg.fixedHeader().qosLevel()) {
            case AT_MOST_ONCE:
                sourceDispatcher.dispatch(null);
                String topicName = msg.variableHeader().topicName();
                MsgVersion.supports(topicName);
                System.out.println(msg.payload().toString(Charset.defaultCharset()));
                //                invokeHandlersForIncomingPublish(message);
                break;
            case AT_LEAST_ONCE:
                //                invokeHandlersForIncomingPublish(message);
                if (msg.variableHeader().packetId() != -1) {
                    ack(channel, msg.variableHeader().packetId());
                }
                break;

            case EXACTLY_ONCE:
                if (msg.variableHeader().packetId() != -1) {
                    pubrec(channel, msg.variableHeader().packetId());
                }
                break;
        }
    }

    @Override
    public void ack(final Channel channel, final int msgId) throws MqttStartException {
        MqttFixedHeader fixedHeader = new MqttFixedHeader(
            MqttMessageType.PUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(msgId);
        channel.writeAndFlush(new MqttPubAckMessage(fixedHeader, variableHeader));
    }

    @Override
    public void pubrec(final Channel channel, final int msgId) {
        MqttFixedHeader fixedHeader = new MqttFixedHeader(
            MqttMessageType.PUBREC, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(msgId);
        MqttMessage pubrecMessage = new MqttMessage(fixedHeader, variableHeader);
        channel.writeAndFlush(pubrecMessage);
    }
}
