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

package org.giot.core.scanner;

import java.lang.annotation.Annotation;

/**
 * @author yuanguohua on 2021/3/5 18:48
 */
public interface AnnotationScannerListener {

    /**
     * 扫描后的class进行添加
     *
     * @param clazz
     */
    void addClass(Class<?> clazz);

    /**
     * 匹配该注解的所有class
     *
     * @return
     */
    Class<? extends Annotation> match();

    /**
     * 监听根据条件获取的class，然后进行处理
     */
    void listener() throws Exception;
}
