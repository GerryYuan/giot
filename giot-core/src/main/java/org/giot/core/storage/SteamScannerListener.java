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

package org.giot.core.storage;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.giot.core.CoreContainerConfig;
import org.giot.core.container.ContainerManager;
import org.giot.core.scanner.AnnotationScannerListener;
import org.giot.core.storage.annotation.Stream;
import org.giot.core.utils.EmptyUtils;

/**
 * @author Created by gerry
 * @date 2021-03-06-10:39 PM
 */
public class SteamScannerListener implements AnnotationScannerListener {

    private List<Class<? extends StorageData>> classes;

    private ContainerManager containerManager;

    private CoreContainerConfig coreContainerConfig;

    public SteamScannerListener(final ContainerManager containerManager, final CoreContainerConfig coreContainerConfig) {
        this.containerManager = containerManager;
        this.coreContainerConfig = coreContainerConfig;
    }

    @Override
    public void addClass(final Class<?> clazz) {
        if (EmptyUtils.isEmpty(classes)) {
            classes = new LinkedList<>();
        }
        classes.add((Class<? extends StorageData>) clazz);
    }

    @Override
    public Class<? extends Annotation> match() {
        return Stream.class;
    }

    @Override
    public void listener() {
        Map<Class<StreamProcessor>, StreamProcessor> processorMap = new ConcurrentHashMap<>(5);
        for (Class<? extends StorageData> clazz : classes) {
            Stream stream = (Stream) clazz.getAnnotation(match());
            Class<StreamProcessor> classProcessor = (Class<StreamProcessor>) stream.processor();
            processorMap.computeIfAbsent(classProcessor, key -> {
                try {
                    Constructor<?> constructor = classProcessor.getConstructor(CoreContainerConfig.class);
                    return (StreamProcessor) constructor.newInstance(coreContainerConfig);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }).create(containerManager, stream.name(), clazz);
        }
    }

}
