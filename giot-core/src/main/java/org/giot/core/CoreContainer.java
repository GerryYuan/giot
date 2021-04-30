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

package org.giot.core;

import org.giot.core.container.AbstractContainer;
import org.giot.core.container.Container;
import org.giot.core.container.ContainerConfig;
import org.giot.core.eventbus.BusInvokerCreator;
import org.giot.core.eventbus.BusInvokerInstaller;
import org.giot.core.eventbus.InvokerCreator;
import org.giot.core.exception.ContainerStartException;
import org.giot.core.network.ProcessorCreator;
import org.giot.core.network.ProcessorDefCreator;
import org.giot.core.network.ProcessorManager;
import org.giot.core.network.SourceProcessorInstaller;
import org.giot.core.scanner.AnnotationScanner;
import org.giot.core.scanner.DefaultAnnotationScanner;
import org.giot.core.scanner.ListenerManager;
import org.giot.core.storage.model.ModelCreator;
import org.giot.core.storage.model.StorageModelCreator;

/**
 * @author Created by gerry
 * @date 2021-02-27-11:21 PM
 */
public class CoreContainer extends AbstractContainer {

    private CoreContainerConfig coreContainerConfig;

    public CoreContainer() {
        this.coreContainerConfig = new CoreContainerConfig();
    }

    @Override
    public String name() {
        return Container.DEFAULT;
    }

    @Override
    public String module() {
        return CoreModule.NAME;
    }

    @Override
    public ContainerConfig createConfigIfAbsent() {
        return this.coreContainerConfig;
    }

    @Override
    public void prepare() {
        super.register(ModelCreator.class, new StorageModelCreator());
        super.register(ProcessorCreator.class, new ProcessorDefCreator(getContainerManager()));
        super.register(ProcessorManager.class, new SourceProcessorInstaller(getContainerManager()));
        super.register(InvokerCreator.class, new BusInvokerCreator(getContainerManager()));
        super.register(ListenerManager.class, new BusInvokerInstaller());
        super.register(
            AnnotationScanner.class, new DefaultAnnotationScanner(getContainerManager(), coreContainerConfig));
    }

    @Override
    public void start() throws ContainerStartException {
        try {
            AnnotationScanner scanner = super.provider(CoreModule.NAME).getService(AnnotationScanner.class);
            scanner.scanner();
        } catch (Exception e) {
            throw new ContainerStartException("Container [" + name() + "] start failure.", e);
        }
    }

    @Override
    public void after() {

    }

}
