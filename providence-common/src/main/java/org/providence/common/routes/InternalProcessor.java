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
        String text = exchange.getIn().getBody(String.class);

        int count = sharedDao.count(text);
        if (count > 0) {
            logger.debug("Message {} refers to a content that is already noted, therefore is marked for discard",
                    exchange.getIn().getMessageId());

            exchange.setProperty(RouteConstants.NEW_CONTENT, false);
            return;
        }

        logger.info("Adding a new record that has recently appeared");
        Shared shared = new Shared();

        shared.setSharedFormat((String) exchange.getProperty(RouteConstants.FORMAT));
        shared.setSharedSource((String) exchange.getProperty(RouteConstants.SOURCE));
        shared.setSharedText(text);

        sharedDao.insert(shared);
        logger.info("Completed adding a new record to the DB");
        exchange.setProperty(RouteConstants.NEW_CONTENT, true);

        if (logger.isDebugEnabled()) {
            if (logger.isTraceEnabled()) {
                logger.trace("Message {} matched all the requirements and is new content", exchange.getIn().getBody());
            }
            else {
                logger.debug("Message {} matched all the requirements and is new content", exchange.getIn().getMessageId());
            }
        }
    }
}
