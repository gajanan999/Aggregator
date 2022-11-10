package com.aggregator.utility;

import com.aggregator.service.PricingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UrlUtility {

    private static Logger logger = LoggerFactory.getLogger(UrlUtility.class);
    @Value("${BASE_URL}")
    private String baseUrl;

    public String getRequiredUrl(String type, Set<String> queue) {
        StringBuilder builder = new StringBuilder();
        builder.append(baseUrl).append(type);
        builder.append("?q=");
        builder.append(queue.stream().collect(Collectors.joining(",")));
        logger.debug(builder.toString());
        return builder.toString();
    }


}
