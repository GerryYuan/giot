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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.util.ResourceLeakDetector;
import lombok.extern.slf4j.Slf4j;
import org.giot.network.mqtt.config.MqttConfig;

/**
 * @author Created by gerry
 * @date 2021-03-14-6:33 PM
 */
@Slf4j
public class MqttOpsService implements IMqttOpsService {

    private MqttConfig config;
    private MqttConfig.NettyConfig netty;

    public MqttOpsService(final MqttConfig config) {
        this.config = config;
        this.netty = config.getNetty();
    }

    private Channel serverChannel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @Override
    public void connect() throws InterruptedException {
        String leakDetectorLevel = netty.getLeakDetectorLevel();
        log.info("Setting resource leak detector level to {}", leakDetectorLevel);
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.valueOf(leakDetectorLevel.toUpperCase()));
        bossGroup = new NioEventLoopGroup(netty.getBossGroupThreadCount());
        workerGroup = new NioEventLoopGroup(netty.getWorkerGroupThreadCount());
        ServerBootstrap sb = new ServerBootstrap();
        sb.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .childHandler(new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel socketChannel) throws Exception {
                  ChannelPipeline pipeline = socketChannel.pipeline();
                  pipeline.addLast("decoder", new MqttDecoder(netty.getMaxPayloadSize()));
                  pipeline.addLast("encoder", MqttEncoder.DEFAUL_ENCODER);
                  //pipeline.addLast("idleStateHandler", new IdleStateHandler(10,2,12, TimeUnit.SECONDS));
                  //                  MqttTransportHandler handler = new MqttTransportHandler(protocolProcess);
                  //                  pipeline.addLast(handler);
              }
          });
        serverChannel = sb.bind(config.getHost(), config.getPort()).sync().channel();
    }

    @Override
    public void shutdown() throws InterruptedException {
        log.info("Stopping MQTT transport!");
        try {
            serverChannel.close().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
        log.info("MQTT transport stopped!");
    }
}