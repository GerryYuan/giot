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

package org.giot.core.device.storage.req;

import java.util.List;
import lombok.Data;
import org.giot.core.device.enums.DeviceProperFeildType;

/**
 * @author yuanguohua on 2021/5/8 17:32
 */
@Data
public class DevicePropDefContext {

    private String name;

    private List<PropDef> propDefs;

    @Data
    public static class PropDef {
        private String id;

        private String name;

        private String des;

        private VauleType valueType;

        private Expands expands;
    }

    @Data
    public static class VauleType {

        private DeviceProperFeildType type;

        private String unit;

        private long length;
    }

    @Data
    public static class Expands {

        private boolean readOnly = true;

        private boolean report = true;

    }

}
