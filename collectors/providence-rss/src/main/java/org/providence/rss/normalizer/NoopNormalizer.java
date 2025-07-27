package org.providence.rss.normalizer;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.rometools.rome.feed.synd.SyndEntryImpl;
import org.apache.camel.Exchange;
import org.providence.common.RouteConstants;

public class NoopNormalizer extends RssNormalizer {
    @Override
    public String normalize(SyndEntryImpl entryBody, Exchange exchange) {
        String reference = URLEncoder.encode(entryBody.getLink(), StandardCharsets.UTF_8);
        exchange.setProperty(RouteConstants.REFERENCE, reference);

        return String.format("<a href=\"%s\">%s</a>", reference,
                entryBody.getTitle());
    }
}
