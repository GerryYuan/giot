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

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuanguohua on 2021/4/23 11:02
 */
class BusInvokerContext {

    /**
     * invokers need impl cas, because 'create' and 'all' method is async
     */
    private List<BusInvoker> invokers = new ArrayList<>(5);

    public void addInvoker(BusInvoker busInvoker) {
        invokers.add(busInvoker);
    }

    public List<BusInvoker> invokers() {
        return this.invokers;
    }
}
