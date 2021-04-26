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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanMap;
import org.giot.core.exception.InstantException;

/**
 * @author Created by gerry
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanUtils {

    public static <T> T copy(Object source, Class<T> clazz) {
        if (EmptyUtils.isEmpty(source)) {
            return null;
        }
        Objects.requireNonNull(clazz, "The class must not be null");
        T target = instantiate(clazz);
        BeanCopier copier = BeanCopier.create(source.getClass(), clazz, false);
        copier.copy(source, target, null);
        return target;
    }

    /**
     * cglib copy
     *
     * @param source obj
     * @param target obj
     */
    public static void copy(Object source, Object target) {
        if (EmptyUtils.isEmpty(source) || EmptyUtils.isEmpty(target)) {
            return;
        }
        BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), false);
        copier.copy(source, target, null);
    }

    /**
     * cglib copy
     *
     * @param datas source
     * @param clazz target
     */
    public static <T> List<T> copyByList(List<?> datas, Class<T> clazz) {
        if (EmptyUtils.isEmpty(datas)) {
            return new ArrayList<>();
        }
        Objects.requireNonNull(clazz, "The 'class' must not be null!");
        List<T> result = new ArrayList<>(datas.size());
        for (Object data : datas) {
            result.add(copy(data, clazz));
        }
        return result;
    }

    public static <T> Set<T> copyBySet(Collection<?> datas, Class<T> clazz) {
        if (EmptyUtils.isEmpty(datas)) {
            return new HashSet<>(1);
        }
        Objects.requireNonNull(clazz, "The 'class' must not be null!");
        Set<T> result = new HashSet<>(datas.size());
        for (Object data : datas) {
            result.add(copy(data, clazz));
        }
        return result;
    }

    /**
     * class instance
     */
    public static <T> T instantiate(Class<T> clazz) {
        Objects.requireNonNull(clazz, "The class must not be null");
        try {
            return clazz.newInstance();
        } catch (InstantiationException ex) {
            throw new InstantException(clazz + ":Is it an abstract class?", ex);
        } catch (IllegalAccessException ex) {
            throw new InstantException(clazz + ":Is the constructor accessible?", ex);
        }
    }

    public static <T> T mapToBean(Map map, Class<T> clazz) {
        T bean = instantiate(clazz);
        BeanMap beanMap = BeanMap.create(bean);
        for (Object k : map.keySet()) {
            Object v = map.get(k);
            if (v instanceof Map) {
                Object val = mapToBean((Map) v, beanMap.getPropertyType((String) k));
                beanMap.put(k, val);
            } else {
                beanMap.put(k, v);
            }
        }
        return bean;
    }

    public static <T> List<T> mapToBean(List<Map> map, Class<T> clazz) {
        List<T> beans = new ArrayList<>(map.size());
        for (Map m : map) {
            beans.add(mapToBean(m, clazz));
        }
        return beans;
    }

}