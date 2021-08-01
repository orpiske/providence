package org.providence.rest;

import java.util.List;

import org.apache.camel.Exchange;
import org.providence.common.NaiveHash;
import org.providence.common.dao.SharedDao;
import org.providence.common.dao.dto.Shared;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvertDBService {
    private static final Logger logger = LoggerFactory.getLogger(AllRecordsService.class);
    private final SharedDao sharedDao = new SharedDao();

    public void convert(Exchange exchange) {
        try {
            List<Shared> convertableRecords = sharedDao.convertableRecords();

            convertableRecords.forEach(r -> r.setSharedHash(NaiveHash.getNaiveHash(r)));

            sharedDao.updateForConvert(convertableRecords);

            exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "text/plain");
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
            exchange.getIn().setBody(convertableRecords.size() + " records converted");

        }
        catch (Exception e) {
            logger.error("Unable to fetch records: {}", e.getMessage());

            exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "text/plain");
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
            exchange.getIn().setBody("Unable to fetch records:");
        }
    }


}
