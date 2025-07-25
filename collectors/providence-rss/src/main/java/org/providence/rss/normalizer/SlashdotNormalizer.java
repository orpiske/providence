/*
 * Copyright 2018 Otavio Rodolfo Piske
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

package org.providence.rss.normalizer;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.rometools.rome.feed.synd.SyndEntryImpl;

public class SlashdotNormalizer extends RssNormalizer {
    @Override
    public String normalize(SyndEntryImpl entryBody) {
        return String.format("<a href=\"%s\">%s</a>", URLEncoder.encode(entryBody.getLink(), StandardCharsets.UTF_8),
                entryBody.getTitle());

    }
}
