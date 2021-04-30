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

package org.giot.network.mqtt.dispatcher;

import org.giot.core.CoreModule;
import org.giot.core.container.ContainerManager;
import org.giot.core.device.DeviceContext;
import org.giot.core.exception.NetworkProcessorNotfoundException;
import org.giot.core.network.ProcessorAdapter;
import org.giot.core.network.ProcessorDef;
import org.giot.core.network.ProcessorManager;
import org.giot.core.network.SourceProcessor;
import org.giot.core.network.URLMappings;

/**
 * @author yuanguohua on 2021/3/22 19:47
 */
public class MqttProcessorAdapter implements ProcessorAdapter {

    private ContainerManager containerManager;

    private ProcessorManager processorManager;

    private URLMappings urlMappings;

    public MqttProcessorAdapter(final ContainerManager containerManager, final URLMappings urlMappings) {
        this.containerManager = containerManager;
        this.urlMappings = urlMappings;
    }

    @Override
    public SourceProcessor supports(final DeviceContext context) {
        if (processorManager == null) {
            this.processorManager = containerManager.provider(CoreModule.NAME).getService(ProcessorManager.class);
        }
        for (SourceProcessor sourceProcessor : processorManager.processors()) {
            if (sourceProcessor instanceof MqttProcessor) {
                ProcessorDef processorDef = processorManager.getProcessorDef(sourceProcessor);
                String url = urlMappings.mapping(processorDef.getVersion(), processorDef.getProcName());
                if (url.equalsIgnoreCase(context.getHeader().getTopic())) {
                    return sourceProcessor;
                }
            }
        }
        throw new NetworkProcessorNotfoundException(context.getHeader() + " adapter processor not support.");
    }

}
