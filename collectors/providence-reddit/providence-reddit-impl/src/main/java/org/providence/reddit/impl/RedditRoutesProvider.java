package org.providence.reddit.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.RoutesBuilder;
import org.apache.commons.configuration.AbstractConfiguration;
import org.providence.common.RoutesBuilderProvider;

public class RedditRoutesProvider implements RoutesBuilderProvider {
    @Override
    public List<RoutesBuilder> buildForCollector(String name, AbstractConfiguration config) {
        if (!isEnabled("reddit.enabled", config)) {
            return List.of();
        }

        List<RoutesBuilder> buildersList = new ArrayList<>();
         for (String subReddit : config.getStringArray("reddit.subreddits")) {
             // This disables specific subreddits
             if (config.getBoolean(String.format("reddit.subreddits.%s.disabled", subReddit), false)) {
                 continue;
             }

             buildersList.add(new RedditRoute(subReddit, RedditRoute.KEYWORD_PREDICATE));
         }

        for (String subReddit : config.getStringArray("reddit.subreddits.wallpaper")) {
            if (!isEnabled(String.format("reddit.subreddits.%s.enabled", subReddit), config)) {
                continue;
            }

            buildersList.add(new RedditRoute(subReddit, new WallpaperPredicate()));
        }

        return buildersList;
    }
}
