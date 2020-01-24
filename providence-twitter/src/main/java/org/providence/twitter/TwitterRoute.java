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

package org.providence.twitter;

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.configuration.AbstractConfiguration;
import org.providence.common.RouteConstants;
import org.providence.common.predicate.ContainsPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.providence.common.ConfigurationWrapper;

public class TwitterRoute extends RouteBuilder {
    private static final AbstractConfiguration config = ConfigurationWrapper.getConfig();
    private static final Logger logger = LoggerFactory.getLogger(TwitterRoute.class);
    private static final String SOURCE_NAME = "Twitter";

    public void configure() {
        final String consumerKey = config.getString("twitter.consumerKey");
        final String consumerSecret = config.getString("twitter.consumerSecret");
        final String accessToken = config.getString("twitter.accessToken");
        final String accessTokenSecret = config.getString("twitter.accessTokenSecret");
        final int interval = config.getInt("twitter.interval", 1900000);

        final String inRoute = String.format("twitter-timeline://user?user=otavio021&type=polling&delay=%d&consumerKey=%s&consumerSecret=%s&accessToken=%s&accessTokenSecret=%s",
                interval, consumerKey, consumerSecret, accessToken, accessTokenSecret);

        logger.info("Created route from: {}", inRoute);

        from(inRoute)
                .filter(new ContainsPredicate(SOURCE_NAME))
                .process(new TwitterProcessor())
                .setProperty(RouteConstants.SOURCE, constant(SOURCE_NAME))
                .to("seda:internal");
    }
}