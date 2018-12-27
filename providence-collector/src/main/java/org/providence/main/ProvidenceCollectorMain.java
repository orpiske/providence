package org.providence.main;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.providence.common.ConfigurationWrapper;
import org.providence.common.Constants;
import org.providence.common.LogConfigurator;
import org.providence.common.routes.InternalRoute;
import org.providence.twitter.TwitterRoute;

public class ProvidenceCollectorMain {
    public static void main(String[] args) {
        try {
            ConfigurationWrapper.initConfiguration(Constants.PROVIDENCE_CONFIG_DIR, "providence-twitter.properties");
            ConfigurationWrapper.initConfiguration(Constants.PROVIDENCE_CONFIG_DIR, "providence-common.properties");
        } catch (Exception e) {
            System.err.println("Unable to initialize configuration file: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        LogConfigurator.debug();
        CamelContext context = new DefaultCamelContext();
        try {
            context.addRoutes(new TwitterRoute());
            context.addRoutes(new InternalRoute());
            context.start();
            while (true) {
                try {
                    Thread.sleep(10000);
                }
                catch (InterruptedException ie) {
                    context.stop();
                }
            }

        } catch (Exception e) {
            System.err.println("Unable to add route: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
