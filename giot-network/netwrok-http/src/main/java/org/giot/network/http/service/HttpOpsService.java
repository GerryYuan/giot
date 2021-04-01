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

package org.giot.network.http.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.giot.core.container.ContainerManager;
import org.giot.network.http.HttpServerInitializer;
import org.giot.network.http.config.HttpConfig;

/**
 * @author Created by gerry
 * @date 2021-03-31-21:32
 */
@Slf4j
public class HttpOpsService implements IHttpOpsService {

    private HttpConfig config;

    private Channel channel;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private ContainerManager containerManager;

    public HttpOpsService(final ContainerManager containerManager, final HttpConfig config) {
        this.containerManager = containerManager;
        this.config = config;
    }

    @Override
    public void start() throws InterruptedException {
        bossGroup = new NioEventLoopGroup(config.getBossThreadCounts());
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap().option(ChannelOption.SO_BACKLOG, 1024)
                                                 .childOption(ChannelOption.TCP_NODELAY, true)
                                                 .childOption(ChannelOption.SO_KEEPALIVE, true)
                                                 .group(bossGroup, workerGroup)
                                                 .channel(NioServerSocketChannel.class)
                                                 .handler(new LoggingHandler(LogLevel.INFO))
                                                 .childHandler(new HttpServerInitializer(containerManager, config));

        this.channel = b.bind(config.getPort()).sync().channel();
        log.info("Netty http server listening on port " + config.getPort());
    }

    @Override
    public void stop() throws InterruptedException {
        try {
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
        log.info("HTTP Channel closed!");
    }
}
