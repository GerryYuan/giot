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

import org.giot.core.service.Service;

/**
 * @author yuanguohua on 2021/3/24 18:01
 */
public interface ProcessorCreator extends Service {

    void create(RouteUrl routeUrl,
                MsgVersion version,
                Class<? extends SourceProcessor> clazz) throws IllegalAccessException, InstantiationException;

    interface WhenCompleteListener {
        /**
         * processorInfo被创建完成后，调用Listener
         */
        void listener(ProcessorDef processorDef) throws InstantiationException, IllegalAccessException;
    }
}
