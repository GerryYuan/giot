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
import org.giot.core.exception.NetworkProcessorNotfoundException;
import org.giot.core.network.DispatcherManager;
import org.giot.core.network.ProcessorAdapter;
import org.giot.core.network.ProcessorInfo;
import org.giot.core.network.Source;
import org.giot.core.network.SourceDispatcher;
import org.giot.core.network.SourceProcessor;

/**
 * @author yuanguohua on 2021/3/22 19:47
 */
public class MqttProcessorAdapter implements ProcessorAdapter {

    private ContainerManager containerManager;

    private DispatcherManager dispatcherManager;

    public MqttProcessorAdapter(final ContainerManager containerManager) {
        this.containerManager = containerManager;
    }

    @Override
    public <T extends Source> SourceProcessor supports(final T source) {
        if (dispatcherManager == null) {
            this.dispatcherManager = (DispatcherManager) containerManager.find(CoreModule.NAME)
                                                                         .getService(SourceDispatcher.class);
        }
        for (SourceProcessor sourceProcessor : dispatcherManager.processors()) {
            if (sourceProcessor instanceof MqttProcessor) {
                ProcessorInfo processorInfo = dispatcherManager.getProcessorInfo(sourceProcessor);
                if (processorInfo.getProcName().equals(source.name()) && processorInfo.getVersion()
                                                                                      .equals(source.version())) {
                    return sourceProcessor;
                }
            }
        }
        throw new NetworkProcessorNotfoundException(source + " processor not exists.");
    }

}
