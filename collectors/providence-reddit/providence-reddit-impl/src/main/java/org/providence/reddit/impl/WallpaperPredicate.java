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
import org.apache.commons.configuration2.AbstractConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.providence.common.ConfigurationWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WallpaperPredicate implements Predicate {
    private static final Logger logger = LoggerFactory.getLogger(WallpaperPredicate.class);
    private static final AbstractConfiguration config = ConfigurationWrapper.getConfig();
    private final String[] searchTerms = config.getStringArray("reddit.subreddits.wallpaper.includes");

    public WallpaperPredicate() {
        logger.info("Creating a new wallpaper predicate");
    }

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
        String subReddit = inBody.getSubreddit();

        for (String searchTerm : searchTerms) {
            if (StringUtils.containsIgnoreCase(stringBody, searchTerm)) {
                logger.trace("Reddit submission on {} matched {} the keyword", subReddit, searchTerm);

                // default to false for now
                return false;
            }
        }

        return false;
    }
}
