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

package org.giot.core;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.Map;
import org.giot.core.module.ModuleConfiguration;
import org.yaml.snakeyaml.Yaml;

/**
 * @author yuanguohua on 2021/2/23 16:50
 */
public class YamlResourceLoader implements ResourceLoader {

    private final Yaml yaml = new Yaml();

    private Map<String, Map<String, Object>> moduleConfig;

    /**
     * 加载配置文件，初始化模块，每个模块有各自不通的configuration，
     */
    private void loadYaml(String fileName) throws FileNotFoundException {
        Reader reader = read(fileName);
        this.moduleConfig = yaml.loadAs(reader, Map.class);
    }

    @Override
    public ModuleConfiguration load(final String fileName) throws FileNotFoundException {
        loadYaml(fileName);
        ModuleConfiguration moduleConfiguration = new ModuleConfiguration();
        moduleConfig.forEach((k, v) -> {
            //ymal文件读取模块，根据配置文件读取的模块名称去加载模块
            moduleConfiguration.addModule(k);
        });
        return moduleConfiguration;
    }

}
