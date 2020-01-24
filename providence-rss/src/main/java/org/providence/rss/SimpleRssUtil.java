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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleRssUtil {
    private static final Logger logger = LoggerFactory.getLogger(SimpleRssUtil.class);

    public static SyndEntryImpl extractEntry(final Exchange exchange) {
        Object inBody = exchange.getIn().getBody();

        if (logger.isTraceEnabled()) {
            logger.trace("Checking message of type {} with body {}", inBody.getClass(), inBody);
        }

        if (!(inBody instanceof SyndEntryImpl)) {
            logger.debug("Discarding message {} because it's not of the expected type: {}", inBody, inBody.getClass());

            return null;
        }


        return (SyndEntryImpl) inBody;
    }
}
