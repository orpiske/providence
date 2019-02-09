/*
 * Copyright 2019 Otavio Rodolfo Piske
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

package org.providence.common.predicate;

import org.apache.camel.Exchange;

public class SourcePropertyAction implements MatchAction {
    private final Exchange exchange;
    private final String source;

    public SourcePropertyAction(Exchange exchange, String source) {
        this.exchange = exchange;
        this.source = source;
    }

    @Override
    public void execute(String keyword) {
        exchange.setProperty("title", String.format("Keyword %s matched on %s", keyword, source));
    }
}
