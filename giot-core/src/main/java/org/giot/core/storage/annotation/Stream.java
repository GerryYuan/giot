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

package org.giot.core.storage.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.giot.core.storage.StreamProcessor;

/**
 * Stream注解 提供存储数据定义，比如创建设备的消息，比如设备上传的消息，包含Key-Values
 * <p>
 * See {@link StreamProcessor}
 * </p>
 *
 * @author yuanguohua
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Stream {
    /**
     * 数据名称，当前数据属于哪个业务，比如设备表，或者产品表，或者用户表等等
     *
     * @return
     */
    String name();

    /**
     * 数据处理器，比如Metadata数据处理器和消息的数据处理器不一样
     *
     * @return
     */
    Class<? extends StreamProcessor> processor();

}
