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

package org.giot.core.utils;

import java.util.Collection;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * EmptyUtils
 *
 * @author yuanguohua
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmptyUtils {

    /**
     * coll->null->true coll-> coll.size() == 0 -> true
     */
    public static <T> boolean isEmpty(Collection<T> coll) {
        return coll == null || coll.isEmpty();
    }

    public static <T> boolean isNotEmpty(Collection<T> coll) {
        return !isEmpty(coll);
    }

    /**
     * map is empty
     */
    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    /**
     * map is not empty
     */
    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
        return !isEmpty(map);
    }

    /**
     * obj is empty
     */
    public static <T> boolean isEmpty(T t) {
        if (t == null) {
            return true;
        }
        if (t.toString() == null || "".equals(t.toString())) {
            return true;
        }
        return false;
    }

    /**
     * array is not empty
     */
    public static <T> boolean isNotEmpty(T[] datas) {
        return !isEmpty(datas);
    }

    /**
     * array is empty
     */
    public static <T> boolean isEmpty(T[] datas) {
        return datas == null || datas.length == 0;
    }

    /**
     * obj is not empty
     */
    public static <T> boolean isNotEmpty(T t) {
        return !isEmpty(t);
    }

}
