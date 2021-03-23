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

package org.giot.core.device.serializer;

import com.google.gson.Gson;
import java.nio.charset.Charset;
import java.util.Objects;
import lombok.Data;
import org.giot.core.exception.NetworkSerializerException;

@Data
public class GsonSerializer implements Serializer {

    private Gson gson = new Gson();

    @Override
    public byte[] serialize(Object object) {
        try {
            Objects.requireNonNull(object);
            String json = gson.toJson(object);
            return json.getBytes(Charset.defaultCharset());
        } catch (Exception e) {
            throw new NetworkSerializerException("Gson serialize obj to json error", e);
        }
    }

    @Override
    public <T> T deserialize(String content, Class<T> targetClass) {
        try {
            Objects.requireNonNull(content);
            return gson.fromJson(content, targetClass);
        } catch (Exception e) {
            throw new NetworkSerializerException("Gson deserialize string to json error", e);
        }
    }

}
