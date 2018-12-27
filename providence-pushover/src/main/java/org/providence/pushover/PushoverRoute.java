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
import org.apache.commons.configuration.AbstractConfiguration;
import org.providence.common.ConfigurationWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushoverRoute extends RouteBuilder {
    private static final AbstractConfiguration config = ConfigurationWrapper.getConfig();
    private static final Logger logger = LoggerFactory.getLogger(PushoverRoute.class);

    @Override
    public void configure() throws Exception {
        final String appToken = config.getString("pushover.appToken");
        final String userToken = config.getString("pushover.userToken");
        final String title = "Keyword Match";

        final String body = String.format("token=%s&user=%s&title=%s&message=${in.body}", appToken, userToken, title);


        from("seda:final?multipleConsumers=true")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/x-www-form-urlencoded"))
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setBody(simple(body))
                .to("https://api.pushover.net/1/messages.json");
    }
}
