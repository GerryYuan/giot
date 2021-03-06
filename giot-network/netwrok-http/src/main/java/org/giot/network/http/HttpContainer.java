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
import org.giot.core.eventbus.BusFractory;
import org.giot.core.eventbus.IInvokerService;
import org.giot.core.eventbus.InvokerAdapter;
import org.giot.core.exception.ContainerStartException;
import org.giot.core.network.NetworkModule;
import org.giot.core.network.SourceDispatcher;
import org.giot.core.network.URLMappings;
import org.giot.network.http.config.HttpConfig;
import org.giot.network.http.dispatcher.HttpDispatcher;
import org.giot.network.http.dispatcher.HttpProcessorAdapter;
import org.giot.network.http.eventbus.HttpAsyncBusFractory;
import org.giot.network.http.eventbus.HttpInvokerAdapter;
import org.giot.network.http.service.HttpInvokerService;
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

    public HttpContainer() {
        this.config = new HttpConfig();
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String module() {
        return NetworkModule.NAME;
    }

    @Override
    public ContainerConfig createConfigIfAbsent() {
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
        super.register(BusFractory.class, new HttpAsyncBusFractory(config.getProcessThreads()));
        super.register(InvokerAdapter.class, new HttpInvokerAdapter(getContainerManager()));
        super.register(IInvokerService.class, new HttpInvokerService(getContainerManager()));
    }

    @Override
    public void start() throws ContainerStartException {
        httpOpsService = super.provider(NetworkModule.NAME, HttpContainer.NAME).getService(IHttpOpsService.class);
        httpOpsService.start();
    }

    @Override
    public void after() {
        getContainerManager().provider(NetworkModule.NAME, HttpContainer.NAME)
                             .getService(IInvokerService.class)
                             .register();
    }

    @Override
    public void stop() {
        httpOpsService.shutdown();
    }
}
