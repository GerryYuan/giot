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

package org.giot.network.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import org.giot.core.container.ContainerManager;
import org.giot.network.http.config.HttpConfig;

/**
 * @author Created by gerry
 * @date 2021-03-31-22:05
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private HttpConfig config;

    private ContainerManager containerManager;

    public HttpServerInitializer(final ContainerManager containerManager, final HttpConfig config) {
        this.containerManager = containerManager;
        this.config = config;
    }

    @Override
    protected void initChannel(final SocketChannel ch) throws Exception {
        ch.pipeline()
          .addLast(new HttpServerCodec())
          .addLast(new HttpObjectAggregator(config.getMaxContentLength()))
          .addLast(new HttpServerExpectContinueHandler()).addLast(new HttpServerHandler(containerManager));
    }
}
