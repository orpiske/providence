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

import com.sun.syndication.feed.synd.SyndEntryImpl;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class SimpleRssProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        SyndEntryImpl entryBody = SimpleRssUtil.extractEntry(exchange);
        if (entryBody == null) {
            return;
        }

        String newBody = entryBody.getTitle() + " at " + entryBody.getLink() + ". Comments at " +
                entryBody.getDescription().getValue();

        exchange.getIn().setBody(newBody);
    }
}
