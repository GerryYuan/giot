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

import com.google.common.collect.Lists;
import java.util.List;
import org.giot.core.container.ContainerManager;
import org.giot.core.device.DeviceContext;
import org.giot.core.device.DeviceHeader;
import org.giot.core.device.payload.PayloadConverter;
import org.giot.core.device.serializer.GsonSerializer;
import org.giot.core.network.AbstractDispatcher;
import org.giot.core.network.ProcessorAdapter;
import org.giot.core.network.SourceProcessor;
import org.giot.network.mqtt.exception.MqttMsgConverterException;

/**
 * @author yuanguohua on 2021/3/22 13:04
 */
public class MqttDispatcher extends AbstractDispatcher {

    private ProcessorAdapter processorAdapter;

    private List<PayloadConverter> converters;

    public MqttDispatcher(final ContainerManager containerManager, final ProcessorAdapter processorAdapter) {
        this.processorAdapter = processorAdapter;
        this.converters = Lists.newArrayList(new MqttPropertiesMsgConverter(containerManager, new GsonSerializer()));
    }

    @Override
    public void dispatch(final DeviceContext context) throws MqttMsgConverterException {
        PayloadConverter converter = getPayloadConvert(context.getHeader());
        SourceProcessor processor = processorAdapter.supports(context);
        processor.invoke(converter.converter(context));
    }

    private PayloadConverter getPayloadConvert(DeviceHeader header) throws MqttMsgConverterException {
        for (PayloadConverter converter : converters) {
            if (converter.supports(header)) {
                return converter;
            }
        }
        throw new MqttMsgConverterException(
            "No adapter for Converter [" + header + "]: The device msg needs to include a PayloadConverter that supports this converter");
    }
}
