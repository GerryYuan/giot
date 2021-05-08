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

package org.giot.core.utils;

import com.google.common.base.Joiner;
import java.util.Random;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author yuanguohua
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GlobalIdUtils {

    private static final String PROCESS_ID = StringUtils.createUUID();

    private static final ThreadLocal<IdContext> THREAD_ID_SEQUENCE = ThreadLocal.withInitial(
        () -> new IdContext(System.currentTimeMillis(), (short) 0));

    /**
     * Generate a new id
     *
     * @return unique id to device
     */
    public static String generate() {
        return Joiner.on('.')
                     .join(PROCESS_ID, String.valueOf(Thread.currentThread().getId()),
                           String.valueOf(THREAD_ID_SEQUENCE.get().nextSeq())
                     );
    }

    private static class IdContext {
        private long lastTimestamp;
        private short threadSeq;
        private long runRandomTimestamp;
        private int lastRandomValue;
        private Random random;

        private IdContext(long lastTimestamp, short threadSeq) {
            this.lastTimestamp = lastTimestamp;
            this.threadSeq = threadSeq;
        }

        private long nextSeq() {
            return timestamp() * 10000 + nextThreadSeq();
        }

        private long timestamp() {
            long currentTimeMillis = System.currentTimeMillis();

            if (currentTimeMillis < lastTimestamp) {
                if (random == null) {
                    random = new Random();
                }
                if (runRandomTimestamp != currentTimeMillis) {
                    lastRandomValue = random.nextInt();
                    runRandomTimestamp = currentTimeMillis;
                }
                return lastRandomValue;
            } else {
                lastTimestamp = currentTimeMillis;
                return lastTimestamp;
            }
        }

        private short nextThreadSeq() {
            if (threadSeq == 10000) {
                threadSeq = 0;
            }
            return threadSeq++;
        }
    }
}
