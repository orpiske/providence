/*
 * Copyright 2018 Otavio R. Piske <angusyoung@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.configuration.AbstractConfiguration;
import org.providence.common.ConfigurationWrapper;
import org.providence.common.Constants;
import org.providence.common.LogConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProvidenceTwitterMain {
    private static final AbstractConfiguration config = ConfigurationWrapper.getConfig();
    private static final Logger logger = LoggerFactory.getLogger(ProvidenceTwitterMain.class);

    public static class TwitterProcessor implements Processor {
        public void process(Exchange exchange) throws Exception {
            System.out.println("Twit: -> " + exchange.getIn().getBody());
        }
    }

    public static class TwitterRouter extends RouteBuilder {
        public void configure() throws Exception {
            final String consumerKey = config.getString("consumerKey");
            final String consumerSecret = config.getString("consumerSecret");
            final String accessToken = config.getString("accessToken");
            final String accessTokenSecret = config.getString("accessTokenSecret");

            final String inRoute = String.format("twitter://timeline/home?type=polling&delay=60000&consumerKey=%s&consumerSecret=%s&accessToken=%s&accessTokenSecret=%s",
                    consumerKey, consumerSecret, accessToken, accessTokenSecret);

            logger.info("Created route from: {}", inRoute);

            from(inRoute)
                    .process(new TwitterProcessor())
                    .to("websocket:camel-tweet?sendToAll=true");
        }
    }

    public static void main(String[] args) {
        try {
            ConfigurationWrapper.initConfiguration(Constants.PROVIDENCE_CONFIG_DIR, "providence-twitter.properties");
        } catch (Exception e) {
            System.err.println("Unable to initialize configuration file: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        LogConfigurator.verbose();
        CamelContext context = new DefaultCamelContext();
        try {
            context.addRoutes(new TwitterRouter());
            context.start();
            while (true) {
                Thread.sleep(10000);
            }

        } catch (Exception e) {
            System.err.println("Unable to add route: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        finally {
//            context.stop();
        }



    }

}
