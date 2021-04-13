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

package org.giot.core.network;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.giot.core.container.ContainerManager;
import org.giot.core.exception.NetworkProcessorNotfoundException;

/**
 * @author yuanguohua on 2021/3/25 10:38
 */
public class SourceProcessorInstaller extends ProcessorInstaller implements ProcessorManager {

    private ContainerManager containerManager;

    public SourceProcessorInstaller(final ContainerManager containerManager) {
        this.containerManager = containerManager;
    }

    private Map<ProcessorDef, SourceProcessor> processorMap = new ConcurrentHashMap<>();

    @Override
    public void createProcessor(final ProcessorDef processorDef) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Constructor<? extends AbstractSourceProcessor> constructor = processorDef.getProcessor()
                                                                                 .getConstructor(
                                                                                     ContainerManager.class);
        processorMap.put(processorDef, constructor.newInstance(containerManager));
    }

    @Override
    public Collection<SourceProcessor> processors() {
        return processorMap.values();
    }

    @Override
    public ProcessorDef getProcessorDef(final SourceProcessor processor) {
        for (Map.Entry<ProcessorDef, SourceProcessor> entrySet : processorMap.entrySet()) {
            if (entrySet.getValue() == processor) {
                return entrySet.getKey();
            }
        }
        throw new NetworkProcessorNotfoundException(processor + " processor not exists.");
    }

    @Override
    public List<ProcessorDef> processorDefs() {
        return processorMap.keySet().stream().collect(Collectors.toList());
    }
}
