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

package org.giot.core.eventbus;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import org.giot.core.CoreModule;
import org.giot.core.container.ContainerManager;
import org.giot.core.eventbus.annotation.Invoker;
import org.giot.core.scanner.AnnotationScannerListener;
import org.giot.core.utils.EmptyUtils;

/**
 * Invoker scanner by annotation
 * <p>
 * See {@link Invoker}
 * </p>
 *
 * @author Created by gerry
 * @date 2021-03-18-10:18 PM
 */
public class InvokerScannerListener implements AnnotationScannerListener {

    private List<Class<? extends BusInvoker>> classes;

    private ContainerManager containerManager;

    public InvokerScannerListener(final ContainerManager containerManager) {
        this.containerManager = containerManager;
    }

    @Override
    public void addClass(final Class<?> clazz) {
        if (EmptyUtils.isEmpty(classes)) {
            classes = new LinkedList<>();
        }
        classes.add((Class<? extends BusInvoker>) clazz);
    }

    @Override
    public Class<? extends Annotation> match() {
        return Invoker.class;
    }

    @Override
    public void listener() throws Exception {
        InvokerCreator invokerCreator = containerManager.find(CoreModule.NAME)
                                                        .getService(InvokerCreator.class);
        for (Class<? extends BusInvoker> clazz : classes) {
            invokerCreator.create(clazz);
        }
    }
}
