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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.giot.core.module.ModuleConfiguration;

/**
 * 资源文件加载模块
 */
public interface ResourceLoader {

    List<ModuleConfiguration.ComponentConfiguration> loadComponents(Map<String, Object> config);

    ModuleConfiguration load() throws FileNotFoundException;

    void which(Map<String, Object> config, List<ModuleConfiguration.ComponentConfiguration> components);

    default Reader read(String fileName) throws FileNotFoundException {
        return new InputStreamReader(readToStream(fileName));
    }

    default InputStream readToStream(String fileName) throws FileNotFoundException {
        URL url = ResourceLoader.class.getClassLoader().getResource(fileName);
        if (url == null) {
            throw new FileNotFoundException("file not found: " + fileName);
        }
        return ResourceLoader.class.getClassLoader().getResourceAsStream(fileName);
    }

    default File[] getPathFiles(String path) throws FileNotFoundException {
        URL url = ResourceLoader.class.getClassLoader().getResource(path);
        if (url == null) {
            throw new FileNotFoundException("path not found: " + path);
        }
        return Objects.requireNonNull(new File(url.getPath()).listFiles(), "No files in " + path);
    }

    default File[] getPathFiles(String parentPath, String[] fileNames) throws FileNotFoundException {
        URL url = ResourceLoader.class.getClassLoader().getResource(parentPath);
        if (url == null) {
            throw new FileNotFoundException("path not found: " + parentPath);
        }
        final Set<String> nameSet = Arrays.stream(fileNames).collect(Collectors.toSet());
        final File[] listFiles = Objects.requireNonNull(
            new File(url.getPath())
                .listFiles((dir, name) -> nameSet.contains(name)), "No files in " + parentPath);

        if (listFiles.length == 0) {
            throw new FileNotFoundException("files not found:" + nameSet);
        }
        return listFiles;
    }
}
