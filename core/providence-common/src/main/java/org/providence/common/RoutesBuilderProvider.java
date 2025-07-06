package org.providence.common;

import java.util.List;

import org.apache.camel.RoutesBuilder;
import org.apache.commons.configuration.AbstractConfiguration;

public interface RoutesBuilderProvider {

    default boolean isEnabled(String key, AbstractConfiguration config) {
        if (config.getBoolean(key, false)) {
            return true;
        }

        return false;
    }

    List<RoutesBuilder> buildForCollector(String name, AbstractConfiguration config);
}
