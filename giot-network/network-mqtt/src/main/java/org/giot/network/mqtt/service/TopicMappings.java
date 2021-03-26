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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.giot.core.network.MsgVersion;
import org.giot.core.network.RouteUrl;
import org.giot.core.network.URLMappings;

/**
 * @author yuanguohua on 2021/3/25 11:03
 */
public class TopicMappings implements URLMappings {

    @Override
    public MsgVersion version() {
        return MsgVersion.v1;
    }

    @Override
    public List<String> urls() {
        return Arrays.stream(RouteUrl.values()).map(RouteUrl::getRoute).collect(Collectors.toList());
    }

    @Override
    public List<String> mappings() {
        return urls().stream().map(s -> version().toString().toLowerCase().concat(s)).collect(Collectors.toList());
    }

    @Override
    public String mapping(final MsgVersion version, final String url) {
        return version().toString().toLowerCase().concat(url);
    }

}
