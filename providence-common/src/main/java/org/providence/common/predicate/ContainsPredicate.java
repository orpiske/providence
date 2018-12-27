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

package org.providence.common.predicate;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.lang.StringUtils;
import org.providence.common.ConfigurationWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.camel.builder.Builder.constant;

public class ContainsPredicate implements Predicate {
    private static final Logger logger = LoggerFactory.getLogger(ContainsPredicate.class);
    private static final AbstractConfiguration config = ConfigurationWrapper.getConfig();

    private final String source;

    public ContainsPredicate(final String source) {
        this.source = source;
    }

    @Override
    public boolean matches(Exchange exchange) {
        Object inBody = exchange.getIn().getBody(String.class);

        if (!(inBody instanceof String)) {
            logger.debug("Discarding message {} because it's not an string: {}", inBody, inBody.getClass());

            return false;
        }

        String stringBody = (String) inBody;
        String[] keywords = config.getStringArray("keywords");

        for (String keyword : keywords) {
            if (StringUtils.containsIgnoreCase(stringBody, keyword)) {
                exchange.setProperty("title", constant(String.format("Keyword %s matched on: %s", keyword, source)));
                logger.info("Matched keyword {} for content {}", keyword, stringBody);

                return true;
            }
        }

        if (logger.isTraceEnabled()) {
            logger.trace("Message {} did not contain any of the keywords", stringBody);
        }

        return false;
    }
}
