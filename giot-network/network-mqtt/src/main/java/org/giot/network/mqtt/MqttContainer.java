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

package org.giot.network.mqtt;

import com.google.common.collect.Lists;
import java.util.List;
import org.giot.core.container.AbstractContainer;
import org.giot.core.container.ContainerConfig;
import org.giot.core.device.payload.PayloadConverter;
import org.giot.core.exception.ContainerStartException;
import org.giot.core.exception.ContainerStopException;
import org.giot.core.network.NetworkModule;
import org.giot.core.network.ProcessorAdapter;
import org.giot.core.network.SourceDispatcher;
import org.giot.core.network.URLMappings;
import org.giot.network.mqtt.config.MqttConfig;
import org.giot.network.mqtt.dispatcher.MqttDispatcher;
import org.giot.network.mqtt.dispatcher.MqttProcessorAdapter;
import org.giot.network.mqtt.service.IMqttConnectService;
import org.giot.network.mqtt.service.IMqttOpsService;
import org.giot.network.mqtt.service.IMqttPingService;
import org.giot.network.mqtt.service.IMqttPubService;
import org.giot.network.mqtt.service.IMqttSubService;
import org.giot.network.mqtt.service.MqttClientHandler;
import org.giot.network.mqtt.service.MqttConnectService;
import org.giot.network.mqtt.service.MqttOpsService;
import org.giot.network.mqtt.service.MqttPingService;
import org.giot.network.mqtt.service.MqttPubService;
import org.giot.network.mqtt.service.MqttSubService;
import org.giot.network.mqtt.service.TopicMappings;

/**
 * @author Created by gerry
 * @date 2021-03-14-5:53 PM
 */
public class MqttContainer extends AbstractContainer {

    public static final String NAME = "mqtt";

    private MqttConfig config;

    private IMqttOpsService mqttOpsService;

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
        this.config = new MqttConfig();
        return config;
    }

    @Override
    public void prepare() {
        super.register(MqttClientHandler.class, new MqttClientHandler(getContainerManager()));
        super.register(IMqttOpsService.class, new MqttOpsService(config, getContainerManager()));
        super.register(IMqttConnectService.class, new MqttConnectService(config, getContainerManager()));
        super.register(IMqttPingService.class, new MqttPingService());
        super.register(IMqttSubService.class, new MqttSubService());
        super.register(IMqttPubService.class, new MqttPubService(getContainerManager()));
        URLMappings urlMappings = new TopicMappings();
        super.register(URLMappings.class, urlMappings);
        ProcessorAdapter processorAdapter = new MqttProcessorAdapter(getContainerManager(), urlMappings);
        super.register(SourceDispatcher.class, new MqttDispatcher(getContainerManager(), processorAdapter));
    }

    @Override
    public void start() throws ContainerStartException {
        try {
            this.mqttOpsService = find(NetworkModule.NAME, NAME).getService(IMqttOpsService.class);
            this.mqttOpsService.start();
        } catch (InterruptedException e) {
            throw new ContainerStartException("Container [" + name() + "] start failure.", e);
        }
    }

    @Override
    public void after() {
    }

    @Override
    public void stop() throws ContainerStopException {
        try {
            this.mqttOpsService.shutdown();
        } catch (InterruptedException e) {
            throw new ContainerStopException("Container [" + name() + "] stop failure.", e);
        }
    }
}
