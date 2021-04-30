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

package org.giot.core.storage.model;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.giot.core.storage.StorageData;
import org.giot.core.storage.annotation.Column;
import org.giot.core.utils.EmptyUtils;

/**
 * @author Created by gerry
 * @date 2021-03-01-10:43 PM
 */
public class StorageModelCreator implements ModelManager, ModelCreator {

    private Map<Class<? extends StorageData>, Model> modelMap = new ConcurrentHashMap<>();

    @Override
    public Model addModel(final String name, final String des,
                          final Class<? extends StorageData> clazz) {
        List<ModelColumn> columns = new LinkedList<>();
        loadColumns(clazz, columns);
        Model model = new Model(name, des, columns);
        modelMap.put(clazz, model);
        return model;
    }

    @Override
    public List<Model> allModels() {
        return this.modelMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public <T extends StorageData> Model getModel(final Class<T> clazz) {
        return modelMap.computeIfAbsent(clazz, k -> null);
    }

    private void loadColumns(Class<?> clazz, List<ModelColumn> columns) {
        if (Objects.nonNull(clazz.getSuperclass())) {
            loadColumns(clazz.getSuperclass(), columns);
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            String name = EmptyUtils.isEmpty(column.name()) ? field.getName() : column.name();
            ModelColumn modelColumn = new ModelColumn(
                name, column.length(), column.des(), field.getType(), field.getGenericType(), column.isNull());
            columns.add(modelColumn);
        }
    }
}
