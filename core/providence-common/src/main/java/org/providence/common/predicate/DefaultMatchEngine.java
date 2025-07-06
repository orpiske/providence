/*
 * Copyright 2019 Otavio Rodolfo Piske
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.providence.common.predicate;

import java.util.List;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.lang.StringUtils;
import org.providence.common.ConfigurationWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMatchEngine implements MatchEngine {
    private static final Logger logger = LoggerFactory.getLogger(DefaultMatchEngine.class);
    private static final AbstractConfiguration config = ConfigurationWrapper.getConfig();
    private static final String[] keywords = config.getStringArray("keywords");
    private static final String[] excludes = config.getStringArray("excludes");

    private static DefaultMatchEngine instance = null;

    public synchronized static DefaultMatchEngine getInstance() {
        if (instance == null) {
            instance = new DefaultMatchEngine();
        }

        return instance;
    }



    @Override
    public boolean keywordMatch(String stringBody, List<? extends MatchAction> actions) {
        for (String keyword : keywords) {
            if (StringUtils.containsIgnoreCase(stringBody, keyword)) {

                for (String excluded : excludes) {
                    if  (StringUtils.containsIgnoreCase(stringBody, excluded)) {
                        logger.info("Matched keyword {} for content but it is excluded by {}", keyword, excluded);
                        return false;
                    }
                }

                for (MatchAction action : actions) {
                    action.execute(keyword);
                }

                logger.info("Matched keyword {} for content", keyword);

                return true;
            }
        }
        return false;
    }
}
