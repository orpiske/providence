package org.providence.main;

import org.apache.camel.main.Main;
import org.apache.camel.main.MainConfigurationProperties;
import org.apache.commons.configuration2.AbstractConfiguration;
import org.providence.common.ConfigurationWrapper;
import org.providence.common.Constants;
import org.providence.common.LogConfigurator;
import org.providence.common.routes.InternalRoute;
import org.providence.pushover.PushoverRoute;
import org.providence.reddit.impl.RedditRoutesProvider;
import org.providence.rest.AllRecordsService;
import org.providence.rest.CleanupDBService;
import org.providence.rest.ConvertDBService;
import org.providence.rest.CuratedRoute;
import org.providence.rest.Last7DaysSharedService;
import org.providence.rest.TodaySharedService;
import org.providence.rest.YesterdaySharedService;
import org.providence.rss.RssRoutesProvider;
import org.providence.twitter.TwitterRoutesProvider;

public class ProvidenceCollectorMain {

    static {
        LogConfigurator.verbose();
    }

    public static void main(String[] args) {
        try {
            ConfigurationWrapper.initConfiguration(Constants.PROVIDENCE_CONFIG_DIR, "providence-twitter.properties");
            ConfigurationWrapper.initConfiguration(Constants.PROVIDENCE_CONFIG_DIR, "providence-pushover.properties");
            ConfigurationWrapper.initConfiguration(Constants.PROVIDENCE_CONFIG_DIR, "providence-reddit.properties");
            ConfigurationWrapper.initConfiguration(Constants.PROVIDENCE_CONFIG_DIR, "providence-rss.properties");
            ConfigurationWrapper.initConfiguration(Constants.PROVIDENCE_CONFIG_DIR, "providence-common.properties");
        } catch (Exception e) {
            System.err.println("Unable to initialize configuration file: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        Main main = new Main();

        try {
            AbstractConfiguration config = ConfigurationWrapper.getConfig();

            try (MainConfigurationProperties mainConfigurationProperties = main.configure()) {
                mainConfigurationProperties.addRoutesBuilder(new InternalRoute());

                TwitterRoutesProvider twitterRoutesProvider = new TwitterRoutesProvider();
                twitterRoutesProvider.buildForCollector("twitter", config).forEach(mainConfigurationProperties::addRoutesBuilder);

                String[] feeds = config.getStringArray("rss.feeds");

                RssRoutesProvider rssRoutesProvider = new RssRoutesProvider();
                for (String feed : feeds) {
                    System.out.println("Adding feed: " + feed);
                    rssRoutesProvider.buildForCollector(feed, config).forEach(mainConfigurationProperties::addRoutesBuilder);
                }


                RedditRoutesProvider redditRoutesProvider = new RedditRoutesProvider();
                redditRoutesProvider.buildForCollector("reddit", config).forEach(mainConfigurationProperties::addRoutesBuilder);

                mainConfigurationProperties.addRoutesBuilder(new PushoverRoute());

                main.bind("all", new AllRecordsService());
                main.bind("today", new TodaySharedService());
                main.bind("yesterday", new YesterdaySharedService());
                main.bind("lastSevenDays", new Last7DaysSharedService());
                main.bind("convert", new ConvertDBService());
                main.bind("cleanup", new CleanupDBService());
                mainConfigurationProperties.addRoutesBuilder(new CuratedRoute());

                main.run();
            }
        } catch (Exception e) {
            System.err.println("Unable to add route: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
