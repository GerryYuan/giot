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

package org.giot.core.device;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.giot.core.storage.Metadata;
import org.giot.core.storage.MetadataStreamProcessor;
import org.giot.core.storage.annotation.Column;
import org.giot.core.storage.annotation.Stream;

/**
 * @author yuanguohua on 2021/3/5 16:26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Stream(name = "device_instance", processor = MetadataStreamProcessor.class)
public class DeviceInstance extends Metadata {

    @Column(name = "name", length = 255, des = "设备名称")
    private String name;

    @Override
    public Long id() {
        return null;
    }
}
