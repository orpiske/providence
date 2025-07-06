package org.providence.twitter;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.RoutesBuilder;
import org.apache.commons.configuration.AbstractConfiguration;
import org.providence.common.RoutesBuilderProvider;

public class TwitterRoutesProvider implements RoutesBuilderProvider {
    @Override
    public List<RoutesBuilder> buildForCollector(String name, AbstractConfiguration config) {
        if (!isEnabled("twitter.enabled", config)) {
            return List.of();
        }

        List<RoutesBuilder> buildersList = new ArrayList<>();
        buildersList.add(new TwitterRoute());

        for (String listOwner : config.getStringArray("twitter.user.lists.owner")) {
            for (String list : config.getStringArray("twitter.user.lists." + listOwner)) {
                System.out.println("Adding list " + list + " by user " + listOwner);
                buildersList.add(new TwitterUserListRoute(listOwner, list));
            }
        }

        return buildersList;
    }
}
