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

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * @author yuanguohua on 2021/5/8 17:12
 */
public final class ReflectionUtils {

    public static <T> List<?> getValues(T obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        List<Object> values = new LinkedList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            values.add(field.get(obj));
        }
        return values;
    }

    public static <T> List<?> getValues(T obj, TypeConvert typeConvert) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        List<Object> values = new LinkedList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (typeConvert.support(value)) {
                value = typeConvert.convert(value);
            }
            values.add(value);
        }
        return values;
    }

    public static class EnumTypeConvert implements TypeConvert {

        @Override
        public boolean support(Object value) {
            return value instanceof Enum;
        }

        @Override
        public String convert(Object value) {
            return ((Enum) value).name();
        }
    }
}
