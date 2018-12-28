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
import org.apache.camel.Processor;

public class AddReferenceUrlProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);

        Object referenceUrl = exchange.getProperty("reference");

        if (referenceUrl != null) {
            String newBody = String.format("%s&url=%s&url_title=View", body, referenceUrl);
            exchange.getIn().setBody(newBody);
        }
    }
}
