/*
 * Copyright 2019 Otavio R. Piske <angusyoung@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.providence.reddit.impl;

import net.dean.jraw.models.Submission;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.lang.StringUtils;
import org.providence.common.ConfigurationWrapper;
import org.providence.common.predicate.MatchUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RedditPredicate implements Predicate {
    private static final Logger logger = LoggerFactory.getLogger(RedditPredicate.class);
    private static final AbstractConfiguration config = ConfigurationWrapper.getConfig();

    private boolean excludesMatch(String stringBody, String subReddit, String[] keywords) {
        for (String keyword : keywords) {
            if (StringUtils.containsIgnoreCase(stringBody, keyword)) {

                logger.info("Ignoring keyword {} for content on {}", keyword, subReddit);

                return true;
            }
        }
        return false;
    }

    @Override
    public boolean matches(Exchange exchange) {
        Submission inBody = exchange.getIn().getBody(Submission.class);

        if (inBody == null) {
            logger.debug("Discarding match because the body is null");
            return false;
        }

        String stringBody = inBody.getTitle();
        if (MatchUtils.keywordMatch(exchange, stringBody, "Reddit")) {
            String subReddit = inBody.getSubreddit();
            String[] excludes = config.getStringArray("reddit." + subReddit + ".excludes");

            if (!excludesMatch(stringBody, subReddit, excludes)) {
                return true;
            }
        }

        if (logger.isTraceEnabled()) {
            logger.trace("Message {} did not contain any of the keywords", stringBody);
        }

        return false;
    }
}
