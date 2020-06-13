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

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;
import org.apache.camel.support.DefaultEndpoint;

@UriEndpoint(
        firstVersion = "0.0.1-SNAPSHOT",
        scheme = "reddit",
        title = "Reddit component",
        syntax = "reddit:name",
        consumerClass = RedditConsumer.class,
        label = "social"
)
public class RedditEndpoint extends DefaultEndpoint {

    @UriPath @Metadata(required = true)
    private String resource;

    @UriParam
    private String clientId;

    @UriParam
    private String clientSecret;

    @UriParam
    private String username;

    @UriParam
    private String password;

    @UriParam
    private String subReddit;

    @UriParam
    private int pageSize;

    @UriParam
    private long delay;

    public RedditEndpoint() {
    }

    public RedditEndpoint(String uri, RedditComponent component) {
        super(uri, component);
    }

    @Override
    public Producer createProducer() {
        throw new UnsupportedOperationException(String.format("You cannot send messages to this endpoint: %s",
                getEndpointUri()));
    }

    @Override
    public Consumer createConsumer(Processor processor) {
        return new RedditConsumer(this, processor);
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubReddit() {
        return subReddit;
    }

    public void setSubReddit(String subReddit) {
        this.subReddit = subReddit;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }
}
