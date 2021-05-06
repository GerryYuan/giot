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

package org.giot.core.device.query;

import lombok.Getter;

/**
 * @author Created by gerry
 * @date 2021-05-05-10:49
 */
@Getter
public class Pagination {
    private int pageNum;
    private int pageSize;
    private boolean needTotal;

    public Pagination(final int pageNum, final int pageSize, final boolean needTotal) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.needTotal = needTotal;
    }

    public static Pagination.Page toPage(Pagination paging) {
        int limit = paging.getPageSize();
        int from = paging.getPageSize() * ((paging.getPageNum() == 0 ? 1 : paging.getPageNum()) - 1);

        return new Pagination.Page(from, limit);
    }

    public static class Page {
        private int from;
        private int limit;

        Page(int from, int limit) {
            this.from = from;
            this.limit = limit;
        }

        public int getFrom() {
            return from;
        }

        public int getLimit() {
            return limit;
        }
    }
}
