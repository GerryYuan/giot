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
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttConnAckVariableHeader;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttIdentifierRejectedException;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageFactory;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttUnacceptableProtocolVersionException;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.giot.core.container.ContainerManager;
import org.giot.core.network.NetworkModule;
import org.giot.core.service.Service;
import org.giot.network.mqtt.MqttContainer;
import org.giot.network.mqtt.config.MqttConfig;

import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION;

/**
 * @author Created by gerry
 * @date 2021-03-14-8:24 PM
 */
@AllArgsConstructor
@ChannelHandler.Sharable
public class MqttClientHandler extends SimpleChannelInboundHandler<MqttMessage> implements Service {

    private MqttConfig mqttConfig;

    private ContainerManager containerManager;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        IMqttConnectService connectService = containerManager.find(NetworkModule.NAME, MqttContainer.NAME)
                                                             .getService(IMqttConnectService.class);
        connectService.connect(ctx);
    }

    @Override
    protected void messageReceived(final ChannelHandlerContext ctx,
                                   final MqttMessage msg) throws Exception {
        if (msg.decoderResult().isFailure()) {
            Throwable cause = msg.decoderResult().cause();
            MqttFixedHeader fixedHeader = new MqttFixedHeader(
                MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0);
            MqttConnAckVariableHeader variableHeader = null;
            if (cause instanceof MqttUnacceptableProtocolVersionException) {
                variableHeader = new MqttConnAckVariableHeader(
                    CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION);
                MqttMessage mqttMessage = MqttMessageFactory.newMessage(fixedHeader, variableHeader, null);
                ctx.writeAndFlush(mqttMessage);
            } else if (cause instanceof MqttIdentifierRejectedException) {
                variableHeader = new MqttConnAckVariableHeader(
                    MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED);
                ctx.writeAndFlush(MqttMessageFactory.newMessage(fixedHeader, variableHeader, null));
            }
            ctx.close();
            return;
        }

        switch (msg.fixedHeader().messageType()) {
            //这个是客户端连接到当前broker
            case CONNECT:
                //                protocolProcess.ack().processConnect(ctx.channel(), (MqttConnectMessage) msg);
                break;
            case CONNACK://当前是客户端，服务器返回ack
                break;
            case PUBLISH://当前是客户端，向服务器发送消息
                //                protocolProcess.publish().processPublish(ctx.channel(), (MqttPublishMessage) msg);
                break;
            case PUBACK://当前是客户端，服务器返回ack消息
                //                protocolProcess.pubAck()
                //                               .processPubAck(ctx.channel(), (MqttMessageIdVariableHeader) msg.variableHeader());
                break;
            case PUBREC:
                //                protocolProcess.pubRec()
                //                               .processPubRec(ctx.channel(), (MqttMessageIdVariableHeader) msg.variableHeader());
                break;
            case PUBREL:
                //                protocolProcess.pubRel()
                //                               .processPubRel(ctx.channel(), (MqttMessageIdVariableHeader) msg.variableHeader());
                break;
            case PUBCOMP:
                //                protocolProcess.pubComp()
                //                               .processPubComp(ctx.channel(), (MqttMessageIdVariableHeader) msg.variableHeader());
                break;
            case SUBSCRIBE:
                //                protocolProcess.subscribe().processSubscribe(ctx.channel(), (MqttSubscribeMessage) msg);
                break;
            case SUBACK:
                break;
            case UNSUBSCRIBE:
                //                protocolProcess.unSubscribe().processUnSubscribe(ctx.channel(), (MqttUnsubscribeMessage) msg);
                break;
            case UNSUBACK:
                break;
            case PINGREQ:
                //                protocolProcess.pingReq().processPingReq(ctx.channel(), msg);
                break;
            case PINGRESP:
                break;
            case DISCONNECT:
                //                protocolProcess.disConnect().processDisConnect(ctx.channel(), msg);
                break;
            default:
                break;

        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof IOException) {
            // 远程主机强迫关闭了一个现有的连接的异常
            ctx.close();
        } else {
            super.exceptionCaught(ctx, cause);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.ALL_IDLE) {
                Channel channel = ctx.channel();
                String clientId = (String) channel.attr(AttributeKey.valueOf("clientId")).get();
                // 发送遗嘱消息
                //                if (this.protocolProcess.getGrozaSessionStoreService().containsKey(clientId)) {
                //                    SessionStore sessionStore = this.protocolProcess.getGrozaSessionStoreService().get(clientId);
                //                    if (sessionStore.getWillMessage() != null) {
                //                        this.protocolProcess.publish().processPublish(ctx.channel(), sessionStore.getWillMessage());
                //                    }
                //                }
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
