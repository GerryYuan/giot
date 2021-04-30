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
import org.giot.core.device.enums.DeviceType;
import org.giot.core.storage.IStorageDAO;

/**
 * @author yuanguohua on 2021/4/30 10:20
 */
public interface IDeviceStorageService extends IStorageDAO {

    /**
     * get device is exists.
     *
     * @param deviceId uuid
     * @return
     */
    boolean isExists(String deviceId) throws SQLException;

    /**
     * create device
     *
     * @param name       device name
     * @param des        device description
     * @param deviceType device type {@link DeviceType}
     * @return
     */
    boolean createDevice(String name, String des, DeviceType deviceType) throws SQLException;

    /**
     * create device
     *
     * @param deviceId   uuid
     * @param name       device name
     * @param des        device description
     * @param deviceType device type {@link DeviceType}
     * @return
     */
    boolean createDevice(String deviceId, String name, String des, DeviceType deviceType) throws SQLException;

    /**
     * online device
     *
     * @param deviceId uuid
     * @return
     */
    boolean onlineDevice(String deviceId) throws SQLException;

}
