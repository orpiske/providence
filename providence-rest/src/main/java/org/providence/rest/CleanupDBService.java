package org.providence.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.providence.common.dao.SharedDao;
import org.providence.common.dao.dto.Shared;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CleanupDBService {
    private static final Logger logger = LoggerFactory.getLogger(AllRecordsService.class);
    private final SharedDao sharedDao = new SharedDao();

    public void convert(Exchange exchange) {
        try {
            List<Shared> allRecords = sharedDao.fetch();
            Set<Long> items = new HashSet<>(allRecords.size());

            Set<Shared> duplicated = allRecords.stream()
                        .filter(n -> !items.add(n.getSharedHash()))
                        .collect(Collectors.toSet());

            duplicated.forEach(r -> logger.info("Marking duplicated item for removal: (id) {} (hash) {} {}",
                    r.getSharedId(), r.getSharedHash(), r.getSharedText()));

            sharedDao.deleteDuplicates(duplicated);

            exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "text/plain");
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
            exchange.getIn().setBody(duplicated.size() + " records converted");
        }
        catch (Exception e) {
            logger.error("Unable to remove records: {}", e.getMessage());

            exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "text/plain");
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
            exchange.getIn().setBody("Unable to remove records");
        }
    }
}
