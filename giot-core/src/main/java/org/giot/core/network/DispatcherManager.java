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

import java.util.Collection;
import org.giot.core.exception.NetworkProcessorNotfoundException;
import org.giot.core.utils.EmptyUtils;

/**
 * @author yuanguohua on 2021/3/22 16:51
 */
public class DispatcherManager extends AbstractManagerDispatcher {

    @Override
    public <T extends Source> void dispatch(final T source) {
        return;
    }

    @Override
    public <T extends Source> SourceProcessor getProcessor(final T source) {
        ProcessorInfo processorInfo = ProcessorInfo.builder().procName(source.name()).version(source.version()).build();
        SourceProcessor processor = getProcessorMap().get(processorInfo);
        if (EmptyUtils.isEmpty(processor)) {
            throw new NetworkProcessorNotfoundException(processorInfo + " processor not found.");
        }
        return processor;
    }

    @Override
    public Collection<SourceProcessor> processors() {
        return getProcessorMap().values();
    }
}
