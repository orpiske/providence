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

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.configuration.AbstractConfiguration;
import org.providence.common.ConfigurationWrapper;
import org.providence.common.RouteConstants;
import org.providence.rss.normalizer.RssNormalizer;

public class SimpleRssRoute extends RouteBuilder {
    private static final AbstractConfiguration config = ConfigurationWrapper.getConfig();

    private final String address;
    private final String name;
    private final RssNormalizer rssNormalizer;
    private long interval;
    private long initialInterval;

    public SimpleRssRoute(final String name, final String address, final RssNormalizer rssNormalizer) {
        this.name = name;
        this.address = address;
        this.rssNormalizer = rssNormalizer;
    }

    public SimpleRssRoute setInterval(long interval) {
        this.interval = interval;
        return this;
    }

    public SimpleRssRoute setInitialInterval(long initialInterval) {
        this.initialInterval = initialInterval;
        return this;
    }

    @Override
    public void configure() {
        String dlc = String.format("seda:rssErrors%s", name);

        errorHandler(deadLetterChannel(dlc));

        fromF("rss:%s?splitEntries=false&delay=%d&initialDelay=%d", address, interval, initialInterval)
                .split()
                    .method(SimpleRssSplitter.class, "splitEntries")
                .filter(new SimpleRssPredicate(name))
                .process(new SimpleRssProcessor(rssNormalizer))
                .setProperty(RouteConstants.SOURCE, constant(name))
                .to("seda:internal");

        from(dlc)
                .log("Error reading RSS data: ${exchangeProperty.CamelExceptionCaught}: ${body}");
    }
}
