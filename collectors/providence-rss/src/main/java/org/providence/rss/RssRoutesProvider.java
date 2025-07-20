package org.providence.rss;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.RoutesBuilder;
import org.apache.commons.configuration2.AbstractConfiguration;
import org.providence.common.RoutesBuilderProvider;
import org.providence.rss.normalizer.RssNormalizer;

public class RssRoutesProvider implements RoutesBuilderProvider{
    @Override
    public List<RoutesBuilder> buildForCollector(String name, AbstractConfiguration config) {
        if (!isEnabled(String.format("%s.rss.enabled", name), config)) {
            return List.of();
        }

        List<RoutesBuilder> buildersList = new ArrayList<>();
        String sourceName = config.getString(String.format("%s.rss.name", name));
        String sourceAddress = config.getString(String.format("%s.rss.address", name));
        String normalizerClassName = config.getString(String.format("%s.rss.normalizer", name));
        Class<?> normalizerClass;
        try {
            normalizerClass = Class.forName(normalizerClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            Object o = normalizerClass.newInstance();
            if (o instanceof RssNormalizer normalizer) {
                SimpleRssRoute simpleRssRoute = new SimpleRssRoute(sourceName, sourceAddress, normalizer);

                long interval = config.getLong(String.format("%s.rss.poll.interval", name), 1800000);
                simpleRssRoute.setInterval(interval);

                long initialInterval = config.getLong(String.format("%s.rss.initial.interval", name), 60000);
                simpleRssRoute.setInitialInterval(initialInterval);

                buildersList.add(simpleRssRoute);

                return buildersList;
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("Normalizer class " + normalizerClassName + " does not implement RssNormalizer");
    }
}
