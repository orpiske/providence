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

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.configuration.AbstractConfiguration;
import org.providence.common.ConfigurationWrapper;
import org.providence.common.RouteConstants;
import org.providence.common.predicate.ContainsPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedditRoute extends RouteBuilder {
    private static final AbstractConfiguration config = ConfigurationWrapper.getConfig();
    private static final Logger logger = LoggerFactory.getLogger(RedditRoute.class);

    private static final String SOURCE_NAME = "Reddit";

    @Override
    public void configure() throws Exception {
        final String username = config.getString("reddit.username");
        final String password = config.getString("reddit.password");
        final String clientId = config.getString("reddit.clientId");
        final String clientSecret = config.getString("reddit.clientSecret");

        final String inRoute = String.format("reddit://subreddit?username=%s&password=%s&clientId=%s&clientSecret=%s&subReddit=java&pageSize=40&delay=3600",
                username, password, clientId, clientSecret);

        logger.info("Created route from: {}", inRoute);

        from(inRoute)
                .filter(new RedditPredicate())
                .process(new RedditProcessor())
                .setProperty(RouteConstants.SOURCE, constant(SOURCE_NAME))
                .to("seda:internal");
    }
}
