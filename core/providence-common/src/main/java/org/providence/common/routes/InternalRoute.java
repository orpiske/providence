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

import org.apache.camel.builder.RouteBuilder;
import org.providence.common.RouteConstants;

public class InternalRoute extends RouteBuilder {
    @Override
    public void configure() {
        from("seda:internal?multipleConsumers=true")
                .process(new InternalProcessor())
                .choice()
                    .when(exchange -> exchange.getProperty(RouteConstants.NEW_CONTENT, Boolean.class))
                    .to("seda:final")
                .end();
    }
}
