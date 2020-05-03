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

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.configuration.AbstractConfiguration;
import org.providence.common.ConfigurationWrapper;
import org.providence.common.RouteConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;

public class TwitterProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(TwitterProcessor.class);
    private static final AbstractConfiguration config = ConfigurationWrapper.getConfig();

    public void process(Exchange exchange) {
        Status status = exchange.getIn().getBody(Status.class);

        if (logger.isTraceEnabled()) {
            logger.trace("Content: {}", status);
        }

        exchange.setProperty(RouteConstants.FORMAT, "raw");

        String twitterHandler = config.getString("twitter.handler", "twitter");
        String refLink = String.format("%s://status?id=%d", twitterHandler, status.getId());
        exchange.setProperty("reference", refLink);
    }
}