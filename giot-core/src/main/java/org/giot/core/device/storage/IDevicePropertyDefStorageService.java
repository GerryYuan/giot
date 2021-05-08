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

package org.giot.core.device.storage;

import java.sql.SQLException;
import org.giot.core.device.metadata.DevicePropertyDef;
import org.giot.core.device.storage.req.DevicePropDefContext;
import org.giot.core.storage.IStorageDAO;

/**
 * @author yuanguohua on 2021/5/8 15:33
 */
public interface IDevicePropertyDefStorageService extends IStorageDAO {

    /**
     * create device property def
     *
     * @param devicePropDefContext context
     * @return true or false
     */
    boolean createDevPropDef(DevicePropDefContext devicePropDefContext) throws SQLException, IllegalAccessException;
}
