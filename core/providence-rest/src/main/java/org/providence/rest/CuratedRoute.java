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

package org.providence.rest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;


public class CuratedRoute extends RouteBuilder {
    @Override
    public void configure() {
        restConfiguration()
                .component("jetty")
                .contextPath("api")
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .port(9096);

        // define the rest service
        rest("/")
                .get("/curated/all").to("bean:all")
                .get("/curated/today").to("bean:today")
                .get("/curated/yesterday").to("bean:yesterday")
                .get("/curated/last/seven").to("bean:lastSevenDays")
                .get("/convert").to("bean:convert")
                .get("/cleanup").to("bean:cleanup");

    }
}
