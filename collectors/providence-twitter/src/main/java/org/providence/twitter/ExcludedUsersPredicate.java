package org.providence.twitter;

import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.commons.configuration2.AbstractConfiguration;
import org.providence.common.ConfigurationWrapper;
import org.providence.common.predicate.ContainsPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.v1.Status;


public class ExcludedUsersPredicate implements Predicate {
    private static final Logger logger = LoggerFactory.getLogger(ContainsPredicate.class);

    private static final Set<String> excludes;

    static {
        AbstractConfiguration config = ConfigurationWrapper.getConfig();

        excludes = Set.of(config.getStringArray("twitter.excluded.users"));
    }

    @Override
    public boolean matches(Exchange exchange) {
        Status status = exchange.getIn().getBody(Status.class);

        String screenName = status.getUser().getScreenName();
        logger.info("Checking if user {} is ignored", screenName);

        return excludes.contains(screenName);
    }
}
