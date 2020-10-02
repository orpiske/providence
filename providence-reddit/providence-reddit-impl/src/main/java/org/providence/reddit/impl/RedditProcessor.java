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
import org.apache.camel.Processor;
import org.providence.common.RouteConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedditProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(RedditProcessor.class);



    @Override
    public void process(Exchange exchange) {
        Submission submission = exchange.getIn().getBody(Submission.class);

        exchange.getIn().setBody(submission.getTitle());

        String refLink = String.format("reddit://%s", submission.getPermalink());
        exchange.setProperty("reference", refLink);

        exchange.setProperty(RouteConstants.FORMAT, "raw");

        logger.info("Processing record = {}", exchange.getIn().getBody(String.class));
    }
}
