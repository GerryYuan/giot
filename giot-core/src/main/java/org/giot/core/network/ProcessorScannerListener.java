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

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import org.giot.core.CoreModule;
import org.giot.core.container.ContainerManager;
import org.giot.core.network.annotation.Processor;
import org.giot.core.scanner.AnnotationScannerListener;
import org.giot.core.utils.EmptyUtils;

/**
 * Processor注解监听者，一些添加了该注解的类，就可以实现通过DispatcherManager知道该数据往哪个processor进行发送。比如：直连device属性消息、gateway属性消息，子设备属性消息
 * <p>
 * See {@link Processor}
 * </p>
 *
 * @author Created by gerry
 * @date 2021-03-18-10:18 PM
 */
public class ProcessorScannerListener implements AnnotationScannerListener {

    private List<Class<? extends AbstractSourceProcessor>> classes;

    private ContainerManager containerManager;

    public ProcessorScannerListener(final ContainerManager containerManager) {
        this.containerManager = containerManager;
    }

    @Override
    public void addClass(final Class<?> clazz) {
        if (EmptyUtils.isEmpty(classes)) {
            classes = new LinkedList<>();
        }
        classes.add((Class<? extends AbstractSourceProcessor>) clazz);
    }

    @Override
    public Class<? extends Annotation> match() {
        return Processor.class;
    }

    @Override
    public void listener() throws Exception {
        ProcessorCreator processorCreator = containerManager.find(CoreModule.NAME)
                                                            .getService(ProcessorCreator.class);
        for (Class<? extends AbstractSourceProcessor> clazz : classes) {
            Processor processor = (Processor) clazz.getAnnotation(match());
            processorCreator.create(processor.route(), processor.version(), clazz);
        }
    }
}
