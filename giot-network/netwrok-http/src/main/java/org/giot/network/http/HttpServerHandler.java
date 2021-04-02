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

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.giot.core.container.ContainerManager;
import org.giot.core.device.DeviceContext;
import org.giot.core.device.DeviceHeader;
import org.giot.core.network.MsgVersion;
import org.giot.core.network.NetworkModule;
import org.giot.core.network.SourceDispatcher;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpResponseStatus.METHOD_NOT_ALLOWED;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author Created by gerry
 * @date 2021-03-31-22:11
 */
@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    private ContainerManager containerManager;

    private SourceDispatcher sourceDispatcher;

    public HttpServerHandler(final ContainerManager containerManager) {
        this.containerManager = containerManager;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final HttpObject msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            HttpMethod method = request.method();
            HttpHeaders headers = request.headers();
            if (!method.equals(HttpMethod.POST)) {
                ctx.writeAndFlush(response(METHOD_NOT_ALLOWED, headers, HttpUtil.isKeepAlive(request)));
                return;
            }
            if (sourceDispatcher == null) {
                this.sourceDispatcher = this.containerManager.find(NetworkModule.NAME, HttpContainer.NAME)
                                                             .getService(SourceDispatcher.class);
            }
            DeviceContext context = DeviceContext.builder()
                                                 .header(DeviceHeader.builder()
                                                                     .topic(request.uri())
                                                                     .version(MsgVersion.v1)
                                                                     .time(System.currentTimeMillis()).build())
                                                 .payload(request.content().toString(Charset.defaultCharset()))
                                                 .build();
            try {
                sourceDispatcher.dispatch(context);
                ctx.writeAndFlush(response(OK, headers, HttpUtil.isKeepAlive(request)));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                ctx.writeAndFlush(response(INTERNAL_SERVER_ERROR, headers, HttpUtil.isKeepAlive(request)));
            }

        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        log.error(cause.toString(), cause);
        ctx.close();
    }

    private FullHttpResponse response(HttpResponseStatus status, HttpHeaders headers, boolean isKeeplive) {
        FullHttpResponse response = new DefaultFullHttpResponse(
            HTTP_1_1, status, Unpooled.wrappedBuffer(status.toString().getBytes(
            StandardCharsets.UTF_8)));
        response.headers().add(headers);
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        if (isKeeplive) {
            response.headers().set(CONNECTION, KEEP_ALIVE);
        }
        return response;
    }
}
