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
import org.giot.core.device.enums.DeviceProperFeildType;
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

    @Column(name = "fieldId", length = 255, des = "device property value")
    private String fieldId;

    @Column(name = "fieldName", length = 255, des = "device property key")
    private String fieldName;

    @Column(name = "fieldType", length = 20, des = "device property type")
    private DeviceProperFeildType fieldType;

    @Column(name = "fieldLength", length = 20, des = "device property length")
    private long fieldLength;

    @Column(name = "fieldUnit", isNull = true, length = 255, des = "device property unit")
    private String fieldUnit;

    @Column(name = "fieldIsRead", length = 2, des = "device property is read")
    private boolean fieldIsRead;

    @Column(name = "fieldIsWrite", length = 2, des = "device property is write")
    private boolean fieldIsWrite;

    @Column(name = "fieldDes", isNull = true, length = 255, des = "device property des")
    private String fieldDes;

    @Column(name = "createTime", length = 20, des = "create time")
    private long createTime;

    @Column(name = "updateTime", length = 20, des = "update time")
    private long updateTime;

}
