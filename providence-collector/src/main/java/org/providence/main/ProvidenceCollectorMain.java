package org.providence.main;

import org.apache.camel.main.Main;
import org.apache.commons.configuration.AbstractConfiguration;
import org.providence.common.ConfigurationWrapper;
import org.providence.common.Constants;
import org.providence.common.LogConfigurator;
import org.providence.common.routes.InternalRoute;
import org.providence.pushover.PushoverRoute;
import org.providence.reddit.impl.RedditRoute;
import org.providence.reddit.impl.WallpaperPredicate;
import org.providence.rest.ConvertDBService;
import org.providence.rest.CuratedRoute;
import org.providence.rest.AllRecordsService;
import org.providence.rest.TodaySharedService;
import org.providence.rss.SimpleRssRoute;
import org.providence.rss.normalizer.HackerNewsNormalizer;
import org.providence.rss.normalizer.SlashdotNormalizer;
import org.providence.twitter.TwitterRoute;
import org.providence.twitter.TwitterUserListRoute;

public class ProvidenceCollectorMain {

    static {
        LogConfigurator.defaultForDaemons();
    }

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

        Main main = new Main();

        try {
            AbstractConfiguration config = ConfigurationWrapper.getConfig();

            main.configure().addRoutesBuilder(new InternalRoute());

            main.configure().addRoutesBuilder(new TwitterRoute());

            for (String listOwner : config.getStringArray("twitter.user.lists.owner")) {
                for (String list : config.getStringArray("twitter.user.lists." + listOwner)) {
                    System.out.println("Adding list " + list + " by user " + listOwner);
                    main.configure().addRoutesBuilder(new TwitterUserListRoute(listOwner, list));
                }
            }

            main.configure().addRoutesBuilder(new SimpleRssRoute("Hacker News", "https://news.ycombinator.com/rss",
                    new HackerNewsNormalizer()));
            main.configure().addRoutesBuilder(new SimpleRssRoute("Slashdot",
                    "http://rss.slashdot.org/Slashdot/slashdotMain/to", new SlashdotNormalizer()));


            for (String subReddit : config.getStringArray("reddit.subreddits")) {
                main.configure().addRoutesBuilder(new RedditRoute(subReddit, RedditRoute.KEYWORD_PREDICATE));
            }

            for (String subReddit : config.getStringArray("reddit.subreddits.wallpaper")) {
                main.configure().addRoutesBuilder(new RedditRoute(subReddit, new WallpaperPredicate()));
            }

            main.configure().addRoutesBuilder(new PushoverRoute());

            main.bind("all", new AllRecordsService());
            main.bind("today", new TodaySharedService());
            main.bind("convert", new ConvertDBService());
            main.configure().addRoutesBuilder(new CuratedRoute());

            main.run();

        } catch (Exception e) {
            System.err.println("Unable to add route: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
