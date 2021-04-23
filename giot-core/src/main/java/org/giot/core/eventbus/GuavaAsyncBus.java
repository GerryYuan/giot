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

import com.google.common.eventbus.AsyncEventBus;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yuanguohua on 2021/4/13 17:03
 */
@Slf4j
public class GuavaAsyncBus implements Bus {

    private AsyncEventBus eventBus;

    public GuavaAsyncBus(final AsyncEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public <T extends BusInvoker> void register(final T value) {
        eventBus.register(value);
        log.info("Register async eventbus invoker [{}]", value);
    }

    @Override
    public <T> void post(final T value) {
        eventBus.post(value);
    }
}
