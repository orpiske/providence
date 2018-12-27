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
import org.providence.common.predicate.ContainsPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.providence.common.ConfigurationWrapper;

public class TwitterRoute extends RouteBuilder {
    private static final AbstractConfiguration config = ConfigurationWrapper.getConfig();
    private static final Logger logger = LoggerFactory.getLogger(TwitterRoute.class);

    public void configure() {
        final String consumerKey = config.getString("consumerKey");
        final String consumerSecret = config.getString("consumerSecret");
        final String accessToken = config.getString("accessToken");
        final String accessTokenSecret = config.getString("accessTokenSecret");

        final String inRoute = String.format("twitter://timeline/home?type=polling&delay=300000&consumerKey=%s&consumerSecret=%s&accessToken=%s&accessTokenSecret=%s",
                consumerKey, consumerSecret, accessToken, accessTokenSecret);

        logger.info("Created route from: {}", inRoute);

        from(inRoute)
                .filter(new ContainsPredicate())
                // Unused, for now
//                .process(new TwitterProcessor())
                .to("seda:final");
    }
}