package com.aggregator.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UrlUtility {


    @Value("${BASE_URL}")
    private String baseUrl;

    public String getRequiredUrl(String type, Set<String> queue){
        StringBuilder builder = new StringBuilder();
        builder.append(baseUrl).append(type);
        builder.append("?q=");
        builder.append(queue.stream().collect(Collectors.joining(",")));
        return builder.toString();
    }


}
