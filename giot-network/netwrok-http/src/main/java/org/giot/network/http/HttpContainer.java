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

import org.giot.core.container.AbstractContainer;
import org.giot.core.container.ContainerConfig;
import org.giot.core.exception.ContainerStartException;
import org.giot.core.network.NetworkModule;
import org.giot.core.network.SourceDispatcher;
import org.giot.core.network.URLMappings;
import org.giot.core.eventbus.BusFractory;
import org.giot.network.http.config.HttpConfig;
import org.giot.network.http.dispatcher.HttpDispatcher;
import org.giot.network.http.dispatcher.HttpProcessorAdapter;
import org.giot.network.http.eventbus.HttpBusFractory;
import org.giot.network.http.service.HttpOpsService;
import org.giot.network.http.service.HttpURLMappings;
import org.giot.network.http.service.IHttpOpsService;

/**
 * @author Created by gerry
 * @date 2021-03-31-18:43
 */
public class HttpContainer extends AbstractContainer {

    public static final String NAME = "http";

    private HttpConfig config;

    private IHttpOpsService httpOpsService;

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String module() {
        return NetworkModule.NAME;
    }

    @Override
    public ContainerConfig createConfig() {
        this.config = new HttpConfig();
        return config;
    }

    @Override
    public void prepare() {
        super.register(IHttpOpsService.class, new HttpOpsService(getContainerManager(), config));
        URLMappings urlMappings = new HttpURLMappings();
        super.register(URLMappings.class, urlMappings);
        super.register(
            SourceDispatcher.class, new HttpDispatcher(
                getContainerManager(),
                new HttpProcessorAdapter(getContainerManager(), urlMappings)
            ));
        super.register(BusFractory.class, new HttpBusFractory());
    }

    @Override
    public void start() throws ContainerStartException {
        httpOpsService = find(NetworkModule.NAME, HttpContainer.NAME).getService(IHttpOpsService.class);
        httpOpsService.start();
    }

    @Override
    public void after() {

    }

    @Override
    public void stop() {
        httpOpsService.shutdown();
    }
}
