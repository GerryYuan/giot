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

package test.org.giot.starter;

import java.io.FileNotFoundException;
import org.giot.core.exception.ContainerConfigException;
import org.giot.core.exception.ContainerStartException;
import org.giot.core.loader.ModuleResourceLoader;
import org.giot.core.loader.ResourceLoader;
import org.giot.core.module.ModuleConfiguration;
import org.giot.core.module.ModuleManager;
import org.junit.Test;

/**
 * @author yuanguohua on 2021/4/30 15:29
 */
public class GiotStarterTest {
    @Test
    public void start() throws FileNotFoundException, ContainerStartException, ContainerConfigException {
        ResourceLoader resourceLoader = new ModuleResourceLoader("application.yml");
        ModuleConfiguration moduleConfiguration = resourceLoader.load();
        ModuleManager moduleManager = new ModuleManager();
        moduleManager.start(moduleConfiguration);
    }

}
