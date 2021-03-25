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

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttSubAckMessage;
import io.netty.handler.timeout.IdleStateEvent;
import java.io.IOException;
import org.giot.core.container.ContainerManager;
import org.giot.core.network.NetworkModule;
import org.giot.core.service.Service;
import org.giot.network.mqtt.MqttContainer;

/**
 * @author Created by gerry
 * @date 2021-03-14-8:24 PM
 */
@ChannelHandler.Sharable
public class MqttClientHandler extends SimpleChannelInboundHandler<MqttMessage> implements Service {

    public MqttClientHandler(final ContainerManager containerManager) {
        this.containerManager = containerManager;
    }

    private ContainerManager containerManager;

    private IMqttConnectService connectService;

    private IMqttPubService pubService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        connectService = containerManager.find(NetworkModule.NAME, MqttContainer.NAME)
                                         .getService(IMqttConnectService.class);
        pubService = containerManager.find(NetworkModule.NAME, MqttContainer.NAME).getService(IMqttPubService.class);
        connectService.connect(ctx.channel());
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext channelHandlerContext,
                                final MqttMessage msg) throws Exception {
        IMqttPingService pingService = containerManager.find(NetworkModule.NAME, MqttContainer.NAME)
                                                       .getService(IMqttPingService.class);
        switch (msg.fixedHeader().messageType()) {
            case CONNACK:
                connectService.ack(channelHandlerContext.channel(), (MqttConnAckMessage) msg);
                break;
            case SUBACK:
                IMqttSubService subService = containerManager.find(NetworkModule.NAME, MqttContainer.NAME)
                                                             .getService(IMqttSubService.class);
                subService.ack(channelHandlerContext.channel(), (MqttSubAckMessage) msg);
                break;
            case PINGRESP:
                pingService.ack(channelHandlerContext.channel(), msg);
                break;
            case PUBLISH:
                pubService.pub(channelHandlerContext.channel(), (MqttPublishMessage) msg);
                break;
            case UNSUBACK:
                break;
            case PUBACK:
                break;
            case PUBREC:
                pubService.pubrec(channelHandlerContext.channel(), (MqttMessageIdVariableHeader) msg.variableHeader());
                break;
            case PUBREL:
                pubService.pubrel(channelHandlerContext.channel(), (MqttMessageIdVariableHeader) msg.variableHeader());
                break;
            case PUBCOMP:
                //                handlePubcomp(msg);
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
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()) {
                case READER_IDLE:
                    break;
                case WRITER_IDLE:
                    IMqttPingService pingService = containerManager.find(NetworkModule.NAME, MqttContainer.NAME)
                                                                   .getService(IMqttPingService.class);
                    pingService.ping(ctx.channel());
                    break;
            }
        }
    }

}
