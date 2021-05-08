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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.giot.core.container.ContainerManager;
import org.giot.core.scanner.ListenerManager;

/**
 * @author yuanguohua on 2021/4/16 17:34
 */
public class BusInvokerInstaller extends InvokerInstaller implements ListenerManager<BusInvoker> {

    private ContainerManager containerManager;

    public BusInvokerInstaller(final ContainerManager containerManager) {
        this.containerManager = containerManager;
    }

    private volatile BusInvokerContext context = new BusInvokerContext();

    /**
     * {@link this#createInvoker} and {@link this#all} cannot guarantee the order of exec. Add the synchronized method
     * area to confirm the order of exec, otherwise {@link this#context} may be empty
     *
     * @param clazz need create invoker
     * @throws IllegalAccessException    if this {@code Constructor} object is enforcing Java language access control
     *                                   and the underlying constructor is inaccessible.
     * @throws InstantiationException    if the class that declares the underlying constructor represents an abstract
     *                                   class.
     * @throws NoSuchMethodException     if a matching method is not found.
     * @throws InvocationTargetException if the underlying constructor throws an exception.
     */
    @Override
    public void createInvoker(final Class<? extends BusInvoker> clazz) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        synchronized (context) {
            Constructor<? extends BusInvoker> constructor = clazz.getConstructor(ContainerManager.class);
            context.addInvoker(constructor.newInstance(containerManager));
        }
    }

    /**
     * see {@link this#createInvoker} method description
     *
     * @return
     */
    @Override
    public List<BusInvoker> all() {
        synchronized (context) {
            return context.invokers();
        }
    }
}
