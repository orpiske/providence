/*
 * Copyright 2019 Otavio Rodolfo Piske
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.providence.rest;

import org.providence.common.dao.SharedDao;
import org.providence.common.dao.dto.Shared;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AllRecordsService {
    private static final Logger logger = LoggerFactory.getLogger(AllRecordsService.class);
    private final SharedDao sharedDao = new SharedDao();

    public List<Shared> all() {
        try {
            return sharedDao.fetch();
        }
        catch (Exception e) {
            logger.error("Unable to fetch records: {}", e.getMessage());

            return null;
        }
    }
}
