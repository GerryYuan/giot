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

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.giot.core.container.ContainerManager;
import org.giot.core.network.NetworkModule;
import org.giot.network.mqtt.MqttContainer;
import org.giot.network.mqtt.config.MqttConfig;

/**
 * @author Created by gerry
 * @date 2021-03-14-6:33 PM
 */
@Slf4j
public class MqttOpsService implements IMqttOpsService {

    private MqttConfig config;

    private ContainerManager containerManager;

    public MqttOpsService(final MqttConfig config, final ContainerManager containerManager) {
        this.config = config;
        this.containerManager = containerManager;
    }

    private Channel channel;
    private EventLoopGroup group;

    @Override
    public void start() throws InterruptedException {
        group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        MqttClientHandler handler = containerManager.find(NetworkModule.NAME, MqttContainer.NAME)
                                                    .getService(MqttClientHandler.class);
        channel = b.group(group)
                   .channel(NioSocketChannel.class)
                   .option(ChannelOption.SO_REUSEADDR, true)
                   .remoteAddress(config.getHost(), config.getPort())
                   .handler(new ChannelInitializer<SocketChannel>() {
                       @Override
                       protected void initChannel(SocketChannel socketChannel) throws Exception {
                           ChannelPipeline pipeline = socketChannel.pipeline();
                           pipeline.addLast("decoder", new MqttDecoder());
                           pipeline.addLast("encoder", MqttEncoder.INSTANCE);
                           pipeline.addLast(
                               "idleStateHandler",
                               new IdleStateHandler(
                                   config.getKeepAliveTimeSeconds(), config.getKeepAliveTimeSeconds(), 0)
                           );
                           pipeline.addLast("handler", handler);
                       }
                   }).connect().channel();
    }

    @Override
    public Channel channel() {
        return this.channel;
    }

    @Override
    public void shutdown() throws InterruptedException {
        try {
            channel.close().sync();
        } finally {
            group.shutdownGracefully();
        }
        log.info("MQTT Channel closed!");
    }
}
