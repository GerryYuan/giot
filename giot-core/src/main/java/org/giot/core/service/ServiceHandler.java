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

package org.giot.core.service;

import org.giot.core.container.AbstractContainer;
import org.giot.core.exception.ServiceNotFoundException;

/**
 * @author yuanguohua on 2021/3/2 20:13
 */
public interface ServiceHandler {

    /**
     * 注册容器服务
     *
     * @param serviceType
     * @param service
     * @throws ServiceNotFoundException
     */
    void register(Class<? extends Service> serviceType, Service service) throws ServiceNotFoundException;

    /**
     * 获取容器服务实例
     *
     * @param clazz
     * @param <T>
     * @return
     */
    <T extends Service> T getService(Class<T> clazz);
}
