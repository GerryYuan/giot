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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AccessLevel;
import lombok.Getter;
import org.giot.core.exception.NetworkProcessorException;

/**
 * @author yuanguohua on 2021/3/22 16:53
 */
public abstract class AbstractManagerDispatcher implements SourceDispatcher {

    @Getter(value = AccessLevel.PROTECTED)
    private Map<ProcessorInfo, SourceProcessor> processorMap = new ConcurrentHashMap<>();

    public void initProcessor(final ProcessorInfo info, final SourceProcessor processor) {
        if (processorMap.keySet().contains(info)) {
            throw new NetworkProcessorException(info + " processor exists.");
        }
        processorMap.put(info, processor);
    }

    public abstract <T extends Source> SourceProcessor getProcessor(T source);

    public abstract Collection<SourceProcessor> processors();

}
