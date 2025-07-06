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

package org.providence.pushover;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.ErrorHandler;
import org.apache.commons.configuration.AbstractConfiguration;
import org.providence.common.ConfigurationWrapper;
import org.providence.common.RouteConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushoverRoute extends RouteBuilder {
    private static final AbstractConfiguration config = ConfigurationWrapper.getConfig();
    private static final Logger logger = LoggerFactory.getLogger(PushoverRoute.class);

    static class PushoverError implements ErrorHandler {
        @Override
        public void process(Exchange exchange) {
            logger.error("Failed to process message {} from {}", exchange.getIn().getMessageId(),
                    exchange.getProperty(RouteConstants.SOURCE));
        }
    }

    @Override
    public void configure() {
        errorHandler(deadLetterChannel("seda:pushoverErrors"));

        final String appToken = config.getString("pushover.appToken");
        final String userToken = config.getString("pushover.userToken");

        final String formattedBody = String.format("token=%s&user=%s&title=${in.headers.title}&message=${in.body}&html=1", appToken, userToken);
        final String rawBody = String.format("token=%s&user=%s&title=${in.headers.title}&message=${in.body}", appToken, userToken);

        errorHandler(defaultErrorHandler().onExceptionOccurred(new PushoverError()));

        int maxMessagesPerSecond = config.getInt("pushover.maxMessagesPerSecond", 2);

        from("seda:final?multipleConsumers=true")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/x-www-form-urlencoded"))
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .choice()
                    .when(exchange -> exchange.getProperty(RouteConstants.FORMAT).equals("raw"))
                        .setBody(simple(rawBody))
                    .otherwise()
                        .setBody(simple(formattedBody))
                    .end()
                .process(new AddReferenceUrlProcessor())
                .throttle(maxMessagesPerSecond).timePeriodMillis(1000)
                .to("https://api.pushover.net/1/messages.json");

        from("seda:pushoverErrors")
                .log("Error writing Pushover data: ${exchangeProperty.CamelExceptionCaught}: ${body}");
    }
}
