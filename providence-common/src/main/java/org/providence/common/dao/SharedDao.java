/*
 * Copyright 2018 Otavio Rodolfo Piske
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

package org.providence.common.dao;

import org.providence.common.dao.dto.Shared;
import org.providence.common.dao.exceptions.DataNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.util.List;

public class SharedDao extends AbstractDao {
    private static final Logger logger = LoggerFactory.getLogger(SharedDao.class);

    public int count(final String text) {
        try {
            Integer ret = runQuery("select count(*) from shared where shared_text = ?",
                    new SingleColumnRowMapper<>(Integer.class),
                    text);

            if (ret == null) {
                return -1;
            }

            return ret.intValue();
        }
        catch (DataNotFoundException e) {
            return 0;
        }
    }

    public void insert(final Shared shared) {
        runEmptyInsert("insert into shared(shared_source, shared_format, shared_text) " +
                "values(:sharedSource, :sharedFormat, :sharedText)", shared);
    }

    public List<Shared> fetch() throws DataNotFoundException {
        return runQueryMany("select * from shared", new BeanPropertyRowMapper<>(Shared.class));
    }

    public List<Shared> today() throws DataNotFoundException {
        return runQueryMany("select * from shared where shared_date >= CURRENT_DATE()", new BeanPropertyRowMapper<>(Shared.class));
    }
}
