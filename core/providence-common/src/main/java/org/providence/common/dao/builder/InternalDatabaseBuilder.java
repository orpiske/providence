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

package org.providence.common.dao.builder;

import java.io.InputStream;
import java.sql.SQLException;

import org.apache.commons.configuration2.AbstractConfiguration;
import org.apache.commons.dbcp2.BasicDataSource;
import org.providence.common.ConfigurationWrapper;
import org.providence.common.dao.TemplateBuilder;
import org.providence.common.exceptions.ProvidenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;


/**
 * The JDBC template that is used for handling internal databases
 */
public class InternalDatabaseBuilder implements TemplateBuilder {
    protected static volatile BasicDataSource ds;

    @Override
    public JdbcTemplate build() {
        if (ds == null) {
            synchronized (this) {
                if (ds == null) {
                    try {
                        ds = new BasicDataSource();

                        AbstractConfiguration config = ConfigurationWrapper.getConfig();

                        final String driverClassName = config.getString("providence.db.driver", "org.h2.Driver");
                        ds.setDriverClassName(driverClassName);

                        final String url = config.getString("providence.db.datasource.url", "jdbc:h2:~/.providence/providence.db");
                        ds.setUrl(url);

                        ds.setInitialSize(2);

                        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

                        try {
                            InputStream resourceAsStream = this.getClass().getResourceAsStream(
                                    "/org/providence/common/dao/builder/create-db.sql");

                            assert resourceAsStream != null;
                            InputStreamResource resource = new InputStreamResource(resourceAsStream);

                            ScriptUtils.executeSqlScript(ds.getConnection(), resource);
                        } catch (SQLException e) {
                            throw new ProvidenceException("Unable to create the providence DB");
                        }

                        return jdbcTemplate;
                    }
                    catch (Throwable t) {
                        Logger logger = LoggerFactory.getLogger(InternalDatabaseBuilder.class);
                        logger.error("Unable to connect to the providence DB: {}", t.getMessage(), t);

                        throw new ProvidenceException(t);
                    }
                }
            }
        }

        return new JdbcTemplate(ds);
    }
}
