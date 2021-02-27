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

package org.giot.core.module;

import java.util.Set;

/**
 * 模块管理者
 *
 * @author Created by gerry
 * @date 2021-02-27-10:55 PM
 */
public class ModuleManager implements ModuleHandler {

    private Set<String> modules;

    @Override
    public boolean has(final String moduleName) {
        return modules.contains(moduleName);
    }

    public void init(ModuleConfiguration moduleConfiguration) {
        //prepare, 先把每个模块的Config赋值
        //start
        //after
    }
}
