/*
 * Copyright 2019 Otavio R. Piske <angusyoung@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.providence.camel.reddit;

import java.util.concurrent.TimeUnit;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.support.ScheduledPollConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedditConsumer extends ScheduledPollConsumer {
    private static final Logger logger = LoggerFactory.getLogger(RedditConsumer.class);
    private final UserAgent userAgent;
    private final RedditEndpoint endpoint;

    private final Credentials credentials;

    private final NetworkAdapter adapter;

    private final RedditClient reddit;

    public RedditConsumer(RedditEndpoint endpoint, Processor processor, String userName) {
        super(endpoint, processor);

        this.endpoint = endpoint;
        this.userAgent = new UserAgent("any", "ProvidenceOSS", "v0.1", userName);

        logger.debug("Reading records every {} seconds", TimeUnit.SECONDS.toMillis(endpoint.getDelay()));
        super.setDelay(TimeUnit.SECONDS.toMillis(endpoint.getDelay()));

        logger.info("Setting up Reddit credentials");
        credentials = Credentials.script(endpoint.getUsername(), endpoint.getPassword(),
                endpoint.getClientId(), endpoint.getClientSecret());

        logger.info("Creating the Reddit network adapter");
        adapter = new OkHttpNetworkAdapter(userAgent);

        logger.info("Creating the Reddit client");
        reddit = OAuthHelper.automatic(adapter, credentials);
    }

    protected void doProcess(Submission submission)  {
        Exchange exchange = endpoint.createExchange() ;
        try {
            exchange.getIn().setBody(submission);
            getProcessor().process(exchange);
        }
        catch (Exception e) {
            exchange.setException(e);
        }
        finally {
            if (exchange.getException() != null) {
                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
            }
        }
    }

    @Override
    protected int poll() {
        DefaultPaginator<Submission> pgn = reddit.subreddit(endpoint.getSubReddit())
                .posts()
                .limit(30)
                .sorting(SubredditSort.NEW)
                .build();

        Listing<Submission> firstPage = pgn.next();

        firstPage.forEach(this::doProcess);

        return 0;
    }
}
