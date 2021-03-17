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
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import lombok.extern.slf4j.Slf4j;
import org.giot.network.mqtt.exception.MqttStartException;

/**
 * @author yuanguohua on 2021/3/17 19:30
 */
@Slf4j
public class MqttPubService implements IMqttPubService {

    @Override
    public void pub(final Channel channel, final MqttPublishMessage msg) throws MqttStartException {
        //        switch (message.fixedHeader().qosLevel()) {
        //            case AT_MOST_ONCE:
        //                invokeHandlersForIncomingPublish(message);
        //                break;
        //
        //            case AT_LEAST_ONCE:
        //                invokeHandlersForIncomingPublish(message);
        //                if (message.variableHeader().messageId() != -1) {
        //                    MqttFixedHeader fixedHeader = new MqttFixedHeader(
        //                        MqttMessageType.PUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0);
        //                    MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(
        //                        message.variableHeader().messageId());
        //                    channel.writeAndFlush(new MqttPubAckMessage(fixedHeader, variableHeader));
        //                }
        //                break;
        //
        //            case EXACTLY_ONCE:
        //                if (message.variableHeader().messageId() != -1) {
        //                    MqttFixedHeader fixedHeader = new MqttFixedHeader(
        //                        MqttMessageType.PUBREC, false, MqttQoS.AT_MOST_ONCE, false, 0);
        //                    MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(
        //                        message.variableHeader().messageId());
        //                    MqttMessage pubrecMessage = new MqttMessage(fixedHeader, variableHeader);
        //
        //                    MqttIncomingQos2Publish incomingQos2Publish = new MqttIncomingQos2Publish(message, pubrecMessage);
        //                    this.client.getQos2PendingIncomingPublishes()
        //                               .put(message.variableHeader().messageId(), incomingQos2Publish);
        //                    message.payload().retain();
        //                    incomingQos2Publish.startPubrecRetransmitTimer(
        //                        this.client.getEventLoop().next(), this.client::sendAndFlushPacket);
        //
        //                    channel.writeAndFlush(pubrecMessage);
        //                }
        //                break;
        //        }
    }

    @Override
    public void ack(final Channel channel, final MqttPubAckMessage msg) throws MqttStartException {
        if (log.isDebugEnabled()) {
            log.info("publish msg received ack, msgId is {}", msg.variableHeader().messageId());
        }
    }
}
