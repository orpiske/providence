package org.providence.rss.normalizer;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.rometools.rome.feed.synd.SyndEntryImpl;

public class NoopNormalizer extends RssNormalizer {
    @Override
    public String normalize(SyndEntryImpl entryBody) {
        return String.format("<a href=\"%s\">%s</a>", URLEncoder.encode(entryBody.getLink(), StandardCharsets.UTF_8),
                entryBody.getTitle());
    }
}
