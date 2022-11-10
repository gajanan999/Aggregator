package com.aggregator.service;


import com.aggregator.timer.Timer;
import com.aggregator.utility.UrlUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PricingService implements ProcessingService {

    private static Logger logger = LoggerFactory.getLogger(PricingService.class);
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    UrlUtility urlUtility;



    Timer timer = new Timer(this);

    ExecutorService executor = Executors.newSingleThreadExecutor();

    Map<String, Double> result = new HashMap<>();
    Set<String> queue = new HashSet<>();

    boolean notStarted = true;

    @Value("${PRICING_PATH}")
    private String pricingPath;

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


    public void process(Set<String> newQueue) {
        logger.info("processing started for pricing");
        Map<String, Double> mapResult = new HashMap<>();
        try {
            if(!newQueue.isEmpty()) {
                mapResult = restTemplate.getForObject(new URI(urlUtility.getRequiredUrl(pricingPath, newQueue)), Map.class);
                logger.debug("Queue {}, Pricing Result: {}", newQueue, mapResult);
            }

            if (!mapResult.isEmpty()) {
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

    public void clearResult(String[] priceList){
        Arrays.stream(priceList).forEach(price -> {
            result.remove(price);
        });
    }

    public Set<String> getQueue() {
        Set<String> newQueue = new HashSet<>(this.queue);
        this.queue.clear();
        return newQueue;
    }

}
