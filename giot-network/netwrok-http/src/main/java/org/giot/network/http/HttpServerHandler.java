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
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.QueryStringDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Values.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author Created by gerry
 * @date 2021-03-31-22:11
 */
@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            HttpHeaders headers = request.headers();
            String uri = request.uri();
            log.info("http uri: " + uri);
            //            if (uri.equals(FAVICON_ICO)) {
            //                return;
            //            }
            HttpMethod method = request.method();
            if (method.equals(HttpMethod.GET)) {
                QueryStringDecoder queryDecoder = new QueryStringDecoder(uri, StandardCharsets.UTF_8);
                Map<String, List<String>> uriAttributes = queryDecoder.parameters();
                //此处仅打印请求参数（你可以根据业务需求自定义处理）
                for (Map.Entry<String, List<String>> attr : uriAttributes.entrySet()) {
                    for (String attrVal : attr.getValue()) {
                        log.info(attr.getKey() + "=" + attrVal);
                    }
                }
            } else if (method.equals(HttpMethod.POST)) {
                //POST请求,由于你需要从消息体中获取数据,因此有必要把msg转换成FullHttpRequest
                FullHttpRequest fullRequest = (FullHttpRequest) msg;
                //根据不同的Content_Type处理body数据
                //                dealWithContentType();
            }

            //            JSONSerializer jsonSerializer = new JSONSerializer();
            //            byte[] content = jsonSerializer.serialize(user);

            FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, OK, Unpooled.wrappedBuffer("content".getBytes(
                StandardCharsets.UTF_8)));
            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());

            boolean keepAlive = HttpUtil.isKeepAlive(request);
            if (!keepAlive) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(CONNECTION, KEEP_ALIVE);
                ctx.write(response);
            }
        }
    }
}
