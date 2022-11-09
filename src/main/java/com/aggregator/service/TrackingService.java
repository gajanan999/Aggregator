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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class TrackingService implements  ProcessingService{

    private static Logger logger = LoggerFactory.getLogger(TrackingService.class);

    @Value("${TRACK_PATH}")
    private String trackPath;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UrlUtility urlUtility;

    Set<String> queue = new HashSet<>();
    Map<String, String> result = new HashMap<>();
    ExecutorService executor = Executors.newSingleThreadExecutor();
    boolean notStarted = true;
    Timer timer = new Timer(this);
    public void submit(String input) {
        if (notStarted) {
            executor.submit(timer);
            notStarted = false;
        }
        queue.add(input);
        if(queue.size() == 5)  {
            process(new HashSet<>(queue));
            queue.clear();
        }
    }

    public void process(Set<String> queue) {
        Map<String,String> mapResult = new HashMap<>();
        logger.info("processing started for tracking");
        try {
            mapResult = restTemplate.getForObject(new URI(urlUtility.getRequiredUrl(trackPath,queue)), Map.class);
            logger.debug("Tracking Result: {}", mapResult);
            if(!mapResult.isEmpty()){
                result.putAll(mapResult);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        logger.info("processing done for shipment");
    }

    public String getResult(String in) {
        return result.get(in);
    }

}
