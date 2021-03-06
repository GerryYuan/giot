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

package org.giot.network.http.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.giot.core.container.ContainerConfig;
import org.giot.core.network.RouteUrl;

/**
 * @author Created by gerry
 * @date 2021-03-14-5:55 PM
 */
@ToString
@Data
@EqualsAndHashCode(callSuper = false)
public class HttpConfig extends ContainerConfig {

    private int port;

    private int workThreads = Runtime.getRuntime().availableProcessors();

    private int maxContentLength = 1024 * 1024;

    private int processThreads = RouteUrl.values().length * workThreads;

}
