package com.aggregator.service;


import com.aggregator.constant.Constants;
import com.aggregator.timer.Timer;
import com.aggregator.utility.UrlUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class PricingService implements ProcessingService {

    private static Logger logger = LoggerFactory.getLogger(PricingService.class);

    @Value("${PRICING_PATH}")
    private String pricingPath;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UrlUtility urlUtility;


    Timer timer = new Timer(this);
    ExecutorService executor = Executors.newSingleThreadExecutor();

    Map<String, Double> result = new HashMap<>();
    boolean notStarted = true;

    public void submit(String input) {
        queue.add(input);
        if (queue.size() == 5) {
            process(new HashSet<>(queue));
            timer.reset();
            queue.clear();
        }
        if (notStarted) {
            executor.submit(timer);
            notStarted = false;
        }

    }


    public void process(Set<String> queue) {
        logger.info("processing started for pricing");
        Map<String,Double> mapResult = new HashMap<>();
        try {
            mapResult = restTemplate.getForObject(new URI(urlUtility.getRequiredUrl(pricingPath, queue)), Map.class);
            logger.debug("Pricing Result: {}", mapResult);
            if(!mapResult.isEmpty()) {
                result.putAll(mapResult);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        logger.info("processing done for pricing");

    }

    public Double getResult(String in) {
        return result.get(in);
    }

}
