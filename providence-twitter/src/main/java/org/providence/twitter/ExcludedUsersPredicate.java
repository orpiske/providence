package org.providence.twitter;

import java.util.Collections;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.commons.configuration.AbstractConfiguration;
import org.providence.common.ConfigurationWrapper;
import org.providence.common.predicate.ContainsPredicate;
import org.providence.common.predicate.DefaultMatchEngine;
import org.providence.common.predicate.MatchAction;
import org.providence.common.predicate.MatchEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;


// 102013789
public class ExcludedUsersPredicate implements Predicate {
    private static final Logger logger = LoggerFactory.getLogger(ContainsPredicate.class);
    private static final MatchEngine matchEngine = DefaultMatchEngine.getInstance();

    private static final String[] excludes;

    static {
        AbstractConfiguration config = ConfigurationWrapper.getConfig();

        excludes = config.getStringArray("twitter.excludes");
    }

    @Override
    public boolean matches(Exchange exchange) {
        Status status = exchange.getIn().getBody(Status.class);

        String screenName = status.getUser().getScreenName();
        logger.debug("Checking if user {} is ignored", screenName);

        for (String name : excludes) {
            if (screenName.matches(name)) {
                logger.debug("User {} is ignored", screenName);
                return true;
            }
        }

        return false;
    }
}
