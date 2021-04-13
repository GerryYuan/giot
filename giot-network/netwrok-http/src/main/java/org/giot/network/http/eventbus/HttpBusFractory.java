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

package org.giot.network.http.eventbus;

import com.google.common.eventbus.EventBus;
import org.giot.core.eventbus.Bus;
import org.giot.core.eventbus.BusFractory;
import org.giot.core.eventbus.GuavaBus;
import org.giot.network.http.HttpContainer;

/**
 * @author yuanguohua on 2021/4/13 15:41
 */
public class HttpBusFractory implements BusFractory {

    private Bus bus;

    @Override
    public Bus openBus() {
        if (bus == null) {
            this.bus = new GuavaBus(new EventBus(HttpContainer.NAME));
        }
        return this.bus;
    }
}
