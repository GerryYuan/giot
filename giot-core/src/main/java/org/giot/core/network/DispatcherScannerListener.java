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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.giot.core.network.annotation.Dispatcher;
import org.giot.core.scanner.AnnotationScannerListener;
import org.giot.core.utils.EmptyUtils;

/**
 * Dispatcher注解监听者，一些添加了该注解的类，就可以实现通过DispatcherManager知道该数据往哪个processor进行发送。比如：直连device属性消息、gateway属性消息，子设备属性消息
 * <p>
 * See {@link Dispatcher}
 * </p>
 *
 * @author Created by gerry
 * @date 2021-03-18-10:18 PM
 */
public class DispatcherScannerListener implements AnnotationScannerListener {

    private List<Class<? extends ProcessorMapping>> classes;

    private Map<String, ProcessorMapping> processorMap = new ConcurrentHashMap<>();

    @Override
    public void addClass(final Class<?> clazz) {
        if (EmptyUtils.isEmpty(classes)) {
            classes = new LinkedList<>();
        }
        classes.add((Class<? extends ProcessorMapping>) clazz);
    }

    @Override
    public Class<? extends Annotation> match() {
        return Dispatcher.class;
    }

    @Override
    public void listener() throws Exception {
        //初始化ProcessorMapping，ProcessorAdapter
        classes.forEach(aClass -> {

        });
    }
}
