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

import java.util.List;
import org.giot.core.scanner.ListenerManager;

/**
 * @author yuanguohua on 2021/4/16 17:34
 */
public class BusInvokerInstaller extends InvokerInstaller implements ListenerManager<BusInvoker> {

    private volatile BusInvokerContext context = new BusInvokerContext();

    @Override
    public void createInvoker(final Class<? extends BusInvoker> clazz) throws IllegalAccessException, InstantiationException {
        synchronized (context) {
            context.addInvoker(clazz.newInstance());
        }
    }

    @Override
    public List<BusInvoker> all() {
        synchronized (context) {
            return context.invokers();
        }
    }
}
