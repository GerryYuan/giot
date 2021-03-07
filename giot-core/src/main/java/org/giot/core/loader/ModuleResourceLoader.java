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

import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.LinkedHashMap;
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
     * 加载配置文件，初始化模块，每个模块有各自不通的configuration，
     */
    private void loadYaml(String fileName) throws FileNotFoundException {
        Reader reader = read(fileName);
        this.moduleConfig = yaml.loadAs(reader, Map.class);
    }

    /**
     * { "which": "xxx", "xxx":{ "a":"aa", "b":"bb" } }
     */
    @Override
    public ModuleConfiguration.ContainerDefinition loadContainerDef(final Map<String, Object> config) {
        //如果不存在which，则过滤
        String which = (String) config.get(ModuleConfiguration.WHICH);
        if (EmptyUtils.isEmpty(which)) {
            return null;
        }
        Properties properties = new Properties();
        LinkedHashMap map = (LinkedHashMap) config.get(which);
        if (EmptyUtils.isNotEmpty(map)) {
            properties.putAll(map);
        }
        return new ModuleConfiguration.ContainerDefinition(which, properties);
    }

    @Override
    public ModuleConfiguration load() throws FileNotFoundException {
        loadYaml(fileName);
        ModuleConfiguration moduleConfiguration = new ModuleConfiguration();
        moduleConfig.forEach((k, v) -> {
            //ymal文件读取模块，根据配置文件读取的模块名称去加载模块以及容器
            moduleConfiguration.addModule(k, loadContainerDef(v));
        });
        return moduleConfiguration;
    }

}
