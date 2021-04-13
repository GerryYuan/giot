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

import java.lang.reflect.InvocationTargetException;
import org.giot.core.CoreModule;
import org.giot.core.container.ContainerManager;

/**
 * @author yuanguohua on 2021/3/24 18:06
 */
public class ProcessorDefCreator implements ProcessorCreator {

    private ContainerManager containerManager;

    public ProcessorDefCreator(final ContainerManager containerManager) {
        this.containerManager = containerManager;
    }

    @Override
    public void create(final RouteUrl routeUrl,
                       final MsgVersion version,
                       final Class<? extends AbstractSourceProcessor> clazz) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        ProcessorDef processorDef = ProcessorDef.builder()
                                                .procName(routeUrl.getRoute())
                                                .version(version)
                                                .processor(clazz)
                                                .build();
        SourceProcessorInstaller installer = (SourceProcessorInstaller) containerManager.find(CoreModule.NAME)
                                                                                        .getService(
                                                                                            ProcessorManager.class);
        installer.listener(processorDef);
    }

}
