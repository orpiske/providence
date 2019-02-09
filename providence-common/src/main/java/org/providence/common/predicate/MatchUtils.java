/*
 * Copyright 2018 Otavio R. Piske <angusyoung@gmail.com>
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

package org.providence.common.predicate;

import org.apache.camel.Exchange;
import org.apache.commons.configuration.AbstractConfiguration;
import org.providence.common.ConfigurationWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class MatchUtils {
    private static final Logger logger = LoggerFactory.getLogger(MatchUtils.class);
    private static final AbstractConfiguration config = ConfigurationWrapper.getConfig();

    @Deprecated
    public static boolean keywordMatch(Exchange exchange, String stringBody, String source) {
        MatchEngine matchEngine = DefaultMatchEngine.getInstance();
        MatchAction matchAction = new SourcePropertyAction(exchange, source);

        return matchEngine.keywordMatch(stringBody, Arrays.asList(matchAction));
    }
}
