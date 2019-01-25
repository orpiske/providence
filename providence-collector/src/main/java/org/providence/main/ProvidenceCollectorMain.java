package org.providence.main;

import org.apache.camel.main.Main;
import org.providence.common.ConfigurationWrapper;
import org.providence.common.Constants;
import org.providence.common.LogConfigurator;
import org.providence.common.routes.InternalRoute;
import org.providence.pushover.PushoverRoute;
import org.providence.reddit.impl.RedditRoute;
import org.providence.rss.SimpleRssRoute;
import org.providence.rss.normalizer.HackerNewsNormalizer;
import org.providence.rss.normalizer.SlashdotNormalizer;
import org.providence.twitter.TwitterRoute;

public class ProvidenceCollectorMain {
    public static void main(String[] args) {
        try {
            ConfigurationWrapper.initConfiguration(Constants.PROVIDENCE_CONFIG_DIR, "providence-twitter.properties");
            ConfigurationWrapper.initConfiguration(Constants.PROVIDENCE_CONFIG_DIR, "providence-pushover.properties");
            ConfigurationWrapper.initConfiguration(Constants.PROVIDENCE_CONFIG_DIR, "providence-reddit.properties");
            ConfigurationWrapper.initConfiguration(Constants.PROVIDENCE_CONFIG_DIR, "providence-common.properties");
        } catch (Exception e) {
            System.err.println("Unable to initialize configuration file: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        LogConfigurator.defaultForDaemons();
        Main main = new Main();

        try {
            main.addRouteBuilder(new TwitterRoute());
            main.addRouteBuilder(new SimpleRssRoute("Hacker News", "https://news.ycombinator.com/rss",
                    new HackerNewsNormalizer()));
            main.addRouteBuilder(new SimpleRssRoute("Slashdot",
                    "http://rss.slashdot.org/Slashdot/slashdotMain/to", new SlashdotNormalizer()));
            main.addRouteBuilder(new RedditRoute("java"));
            main.addRouteBuilder(new RedditRoute("brdev"));
            main.addRouteBuilder(new RedditRoute("jboss"));
            main.addRouteBuilder(new RedditRoute("redhat"));
            main.addRouteBuilder(new RedditRoute("programming"));

            main.addRouteBuilder(new InternalRoute());
            main.addRouteBuilder(new PushoverRoute());

            main.run();

        } catch (Exception e) {
            System.err.println("Unable to add route: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
