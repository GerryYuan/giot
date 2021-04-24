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

package org.giot.network.http.dispatcher;

import com.google.common.collect.Lists;
import java.util.List;
import org.giot.core.container.ContainerManager;
import org.giot.core.device.DeviceContext;
import org.giot.core.device.payload.PayloadConverter;
import org.giot.core.network.serializer.GsonSerializer;
import org.giot.core.network.serializer.Serializer;
import org.giot.core.network.AbstractDispatcher;
import org.giot.core.network.ProcessorAdapter;
import org.giot.core.network.SourceProcessor;

/**
 * @author Created by gerry
 * @date 2021-04-01-21:35
 */
public class HttpDispatcher extends AbstractDispatcher {

    private List<PayloadConverter> converters;

    private ProcessorAdapter processorAdapter;

    public HttpDispatcher(final ContainerManager containerManager, final ProcessorAdapter processorAdapter) {
        Serializer serializer = new GsonSerializer();
        this.converters = Lists.newArrayList(
            new HttpPropertiesMsgConverter(containerManager, serializer),
            new HttpConnectedConverter(containerManager, serializer)
        );
        this.processorAdapter = processorAdapter;
    }

    @Override
    public void dispatch(final DeviceContext context) throws Exception {
        //header topic is http uri(/v1/report-properties)
        PayloadConverter converter = getPayloadConvert(context.getHeader());
        SourceProcessor processor = processorAdapter.supports(context);
        processor.invoke(converter.converter(context));
    }

    @Override
    public List<PayloadConverter> converters() {
        return this.converters;
    }
}
