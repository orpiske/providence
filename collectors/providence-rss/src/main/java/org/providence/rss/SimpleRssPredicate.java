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

package org.providence.rss;

import com.rometools.rome.feed.synd.SyndEntryImpl;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.commons.configuration2.AbstractConfiguration;
import org.providence.common.ConfigurationWrapper;
import org.providence.common.predicate.ContainsPredicate;
import org.providence.common.predicate.MatchUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleRssPredicate implements Predicate {
    private static final Logger logger = LoggerFactory.getLogger(ContainsPredicate.class);
    private static final AbstractConfiguration config = ConfigurationWrapper.getConfig();

    private final String source;

    public SimpleRssPredicate(String source) {
        this.source = source;
    }

    @Override
    public boolean matches(final Exchange exchange) {
        SyndEntryImpl entryBody = SimpleRssUtil.extractEntry(exchange);
        if (entryBody == null) {
            return false;
        }

        final String title = entryBody.getTitle();
        if (MatchUtils.keywordMatch(exchange, title, source)) {
            return true;
        }

        if (logger.isTraceEnabled()) {
            logger.trace("Message {} did not contain any of the keywords", entryBody);
        }

        return false;
    }
}
