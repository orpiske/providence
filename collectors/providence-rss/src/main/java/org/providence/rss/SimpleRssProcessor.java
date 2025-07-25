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
import org.apache.camel.Processor;
import org.providence.common.RouteConstants;
import org.providence.rss.normalizer.RssNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleRssProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(SimpleRssProcessor.class);
    private final RssNormalizer normalizer;

    public SimpleRssProcessor(RssNormalizer normalizer) {
        this.normalizer = normalizer;
    }

    @Override
    public void process(Exchange exchange) {
        SyndEntryImpl entryBody = SimpleRssUtil.extractEntry(exchange);
        if (entryBody == null) {
            return;
        }

        logger.debug("RSS Content: {}", entryBody);

        String newBody = normalizer.normalize(entryBody);

        exchange.setProperty(RouteConstants.FORMAT, "formatted");
        exchange.getIn().setBody(newBody);
    }
}
