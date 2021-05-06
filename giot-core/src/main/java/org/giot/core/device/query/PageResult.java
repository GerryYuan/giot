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

package org.giot.core.device.query;

import java.util.List;

/**
 * @author Created by gerry
 * @date 2021-05-05-10:57
 */
public class PageResult<T> {

    private int total;

    private List<T> data;

    private boolean hasNext;

    public PageResult(final int total, final List<T> data, final boolean hasNext) {
        this.total = total;
        this.data = data;
        this.hasNext = hasNext;
    }

    public static <T> PageResult<T> build(List<T> data, int total) {
        return new PageResult<>(total, data, true);
    }

    public static <T> PageResult<T> build(List<T> data, int total, boolean hasNext) {
        return new PageResult<>(total, data, hasNext);
    }

    public static <T> PageResult<T> build(List<T> data, boolean hasNext) {
        return new PageResult<>(0, data, hasNext);
    }

    public static <T> PageResult<T> build(List<T> data) {
        return new PageResult<>(0, data, true);
    }

}
