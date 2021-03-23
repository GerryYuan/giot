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

package org.giot.network.mqtt.dispatcher;

import org.giot.core.network.AbstractDispatcher;
import org.giot.core.network.Source;
import org.giot.core.network.SourceProcessor;

/**
 * @author yuanguohua on 2021/3/22 13:04
 */
public class MqttDispatcher extends AbstractDispatcher {

    private MqttProcessorAdapter processorAdapter;

    public MqttDispatcher(final MqttProcessorAdapter processorAdapter) {
        this.processorAdapter = processorAdapter;
    }

    @Override
    public <T extends Source> void dispatch(final T source) {
        SourceProcessor processor = processorAdapter.supports(source);
        processor.invoke(source);
    }
}