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

package org.providence.common.routes;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.providence.common.NaiveHash;
import org.providence.common.RouteConstants;
import org.providence.common.dao.SharedDao;
import org.providence.common.dao.dto.Shared;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InternalProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(InternalProcessor.class);
    private static final SharedDao sharedDao = new SharedDao();

    @Override
    public void process(Exchange exchange) {
        final String text = exchange.getIn().getBody(String.class);
        final long naiveHash = NaiveHash.getNaiveHash(text);
        
        int count = sharedDao.count(naiveHash);

        if (count > 0) {
            if (count > 1) {
                logger.warn("There's likely a hash collision: {} records with has {}. Enforcing a full-text search ...",
                        count, naiveHash);
                // Re-check with full text search only if there's a collision in the hashes
                int fullTextCount = sharedDao.count(text);

                if (fullTextCount > 0) {
                    logger.warn("Message {} refers to a content that is already noted {}/{} times (hash count/full text count), therefore is marked for discard",
                            naiveHash, count, fullTextCount);

                    exchange.setProperty(RouteConstants.NEW_CONTENT, false);
                    return;
                }
            } else {
                logger.debug("Message with hash {} refers to a content that is already noted, therefore is marked for discard",
                        naiveHash);

                exchange.setProperty(RouteConstants.NEW_CONTENT, false);
                return;
            }
        }

        Shared shared = new Shared();

        String sourceName = exchange.getProperty(RouteConstants.SOURCE, String.class);
        shared.setSharedFormat(exchange.getProperty(RouteConstants.FORMAT, String.class));
        shared.setSharedSource(sourceName);
        shared.setSharedText(text);
        shared.setSharedHash(naiveHash);

        logger.debug("Adding a new record to the DB");
        sharedDao.insert(shared);
        logger.info("Added a new record with hash {} from {} that has recently appeared", naiveHash, sourceName);

        exchange.setProperty(RouteConstants.NEW_CONTENT, true);

        logger.trace("Message {} matched all the requirements and is new content", text);
    }
}
