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

import com.google.common.collect.Lists;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.giot.core.storage.IndexBuilder;
import org.giot.core.storage.Metadata;
import org.giot.core.storage.MetadataStreamProcessor;
import org.giot.core.storage.annotation.Column;
import org.giot.core.storage.annotation.Stream;
import org.giot.core.storage.model.IndexDef;

/**
 * @author yuanguohua on 2021/5/8 14:56
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Stream(name = "device_property_relation", indexBuilder = DevicePropertyRelation.Builder.class, des = "device property relation", processor = MetadataStreamProcessor.class)
public class DevicePropertyRelation extends Metadata {

    public static final String DEVICE_PROP_DEF_ID = "devicePropDefId";
    public static final String DEVICE_ID = "deviceId";

    @Column(name = "deviceId", length = 255, des = "device id")
    private String deviceId;

    @Column(name = "devicePropDefId", length = 20, des = "device property def id")
    private long devicePropDefId;

    @Column(name = "createTime", length = 20, des = "create time")
    private long createTime;

    @Column(name = "updateTime", length = 20, des = "update time")
    private long updateTime;

    public static class Builder implements IndexBuilder {

        @Override
        public List<IndexDef> builder() {
            return Lists.newArrayList(IndexDef.builder()
                                              .indexType(Stream.IndexType.unique)
                                              .fieldNames(new String[] {
                                                  DEVICE_ID,
                                                  DEVICE_PROP_DEF_ID
                                              }).build());
        }
    }
}
