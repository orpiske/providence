package org.providence.main;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.providence.common.ConfigurationWrapper;
import org.providence.common.Constants;
import org.providence.common.LogConfigurator;
import org.providence.twitter.TwitterRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProvidenceCollectorMain {
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
            context.addRoutes(new TwitterRoute());
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
