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
import io.netty.handler.codec.mqtt.MqttMessageFactory;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttSubAckMessage;
import io.netty.handler.codec.mqtt.MqttSubscribePayload;
import io.netty.handler.codec.mqtt.MqttTopicSubscription;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.giot.network.mqtt.exception.MqttSubException;

/**
 * @author yuanguohua on 2021/3/15 18:50
 */
public class MqttSubService implements IMqttSubService {

    @Override
    public void sub(final Channel channel, final List<String> topics) throws MqttSubException {
        MqttFixedHeader fixedHeader = new MqttFixedHeader(
            MqttMessageType.SUBSCRIBE, false, MqttQoS.AT_LEAST_ONCE, false, 0);
        MqttSubscribePayload payload = new MqttSubscribePayload(topics.stream()
                                                                      .map(topic -> new MqttTopicSubscription(
                                                                          topic,
                                                                          MqttQoS.AT_MOST_ONCE
                                                                      ))
                                                                      .collect(Collectors.toList()));
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(payload.toString().getBytes());
            DataInputStream dis = new DataInputStream(bais);
            MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(dis.readUnsignedShort());
            dis.close();
            channel.writeAndFlush(MqttMessageFactory.newMessage(fixedHeader, variableHeader, payload));
        } catch (IOException e) {
            throw new MqttSubException(e.getMessage(), e);
        }

    }

    @Override
    public void ack(final Channel channel, final MqttSubAckMessage msg) {
        return;
    }

}
