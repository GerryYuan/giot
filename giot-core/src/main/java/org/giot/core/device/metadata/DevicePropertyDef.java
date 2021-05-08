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

package org.giot.core.device.metadata;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.giot.core.storage.Metadata;
import org.giot.core.storage.MetadataStreamProcessor;
import org.giot.core.storage.annotation.Column;
import org.giot.core.storage.annotation.Stream;

/**
 * @author yuanguohua on 2021/5/8 14:56
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Stream(name = "device_property_def", des = "device property def", processor = MetadataStreamProcessor.class)
public class DevicePropertyDef extends Metadata {

    @Column(name = "name", length = 255, des = "device property def name")
    private String name;

    @Column(name = "fieldKey", length = 255, des = "device property key")
    private String fieldKey;

    @Column(name = "fieldValue", length = 255, des = "device property value")
    private String fieldValue;

    @Column(name = "fieldType", length = 255, des = "device property type")
    private String fieldType;

    @Column(name = "fieldUnit", length = 255, des = "device property unit")
    private String fieldUnit;

    @Column(name = "fieldLength", length = 255, des = "device property length")
    private String fieldLength;

    @Column(name = "fieldIsRead", length = 2, des = "device property is read")
    private boolean fieldIsRead;

    @Column(name = "fieldDes", length = 255, des = "device property des")
    private String fieldDes;

    @Column(name = "createTime", length = 20, des = "create time")
    private long createTime;

    @Column(name = "updateTime", length = 20, des = "update time")
    private long updateTime;

    @Override
    public Long id() {
        return null;
    }
}
