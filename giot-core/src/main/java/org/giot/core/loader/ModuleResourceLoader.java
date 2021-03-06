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

package org.giot.core.loader;

import com.google.common.base.Splitter;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.giot.core.module.ModuleConfiguration;
import org.giot.core.utils.EmptyUtils;
import org.yaml.snakeyaml.Yaml;

/**
 * @author yuanguohua on 2021/2/23 16:50
 */
@Slf4j
public class ModuleResourceLoader implements ResourceLoader {

    private String fileName;

    private final Yaml yaml = new Yaml();

    private Map<String, Map<String, Object>> moduleConfig;

    public ModuleResourceLoader(final String fileName) {
        this.fileName = fileName;
    }

    /**
     * load yml file, init module to configuration
     */
    private void loadYaml(String fileName) throws FileNotFoundException {
        Reader reader = read(fileName);
        this.moduleConfig = yaml.loadAs(reader, Map.class);
    }

    /**
     * { "which": "xxx","xxx":{ "a":"aa", "b":"bb" } }
     */
    @Override
    public List<ModuleConfiguration.ContainerDefinition> loadContainerDefs(final Map<String, Object> config) {
        List<ModuleConfiguration.ContainerDefinition> containerDefinitions = new ArrayList<>(1);
        if (EmptyUtils.isEmpty(config)) {
            return containerDefinitions;
        }
        //if not exists which, ignore.
        String which = (String) config.get(ModuleConfiguration.WHICH);
        if (EmptyUtils.isEmpty(which)) {
            return containerDefinitions;
        }
        List<String> whiches = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(which);
        for (String w : whiches) {
            containerDefinitions.add(which(w, config));
        }

        return containerDefinitions;
    }

    @Override
    public ModuleConfiguration load() throws FileNotFoundException {
        loadYaml(fileName);
        ModuleConfiguration moduleConfiguration = new ModuleConfiguration();
        moduleConfig.forEach((k, v) -> {
            //by file read module name, then load module and container
            moduleConfiguration.addModule(k, loadContainerDefs(v));
        });
        return moduleConfiguration;
    }

    private ModuleConfiguration.ContainerDefinition which(final String which, final Map<String, Object> config) {
        Properties properties = new Properties();
        Map<String, ?> propertyConfig = (Map<String, ?>) config.get(which);
        if (EmptyUtils.isNotEmpty(propertyConfig)) {
            propertyConfig.forEach((propertyName, propertyValue) -> {
                if (propertyValue instanceof Map) {
                    Properties subProperties = new Properties();
                    ((Map) propertyValue).forEach((key, value) -> {
                        subProperties.put(key, value);
                    });
                    properties.put(propertyName, subProperties);
                } else {
                    properties.put(propertyName, propertyValue);
                }
            });
        }
        return new ModuleConfiguration.ContainerDefinition(which, properties);
    }

}
