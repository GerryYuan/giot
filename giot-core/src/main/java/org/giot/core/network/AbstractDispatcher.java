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

import java.util.List;
import org.giot.core.device.DeviceHeader;
import org.giot.core.device.payload.PayloadConverter;

/**
 * @author yuanguohua on 2021/3/22 17:18
 */
public abstract class AbstractDispatcher implements SourceDispatcher {

    public PayloadConverter getPayloadConvert(DeviceHeader header) throws MsgConverterException {
        for (PayloadConverter converter : converters()) {
            if (converter.supports(header)) {
                return converter;
            }
        }
        throw new MsgConverterException(
            "No adapter for Converter [" + header + "]: The device msg needs to include a PayloadConverter that supports this converter");
    }

    public abstract List<PayloadConverter> converters();
}
