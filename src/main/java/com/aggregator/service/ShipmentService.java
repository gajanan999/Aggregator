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
public class ShipmentService implements ProcessingService {

    private static Logger logger = LoggerFactory.getLogger(ShipmentService.class);
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    UrlUtility urlUtility;

    //Set<String> queue = new HashSet<>();

    Map<String, List<String>> result = new HashMap<>();
    Set<String> queue = new HashSet<>();

    Timer timer = new Timer(this);

    ExecutorService executor = Executors.newSingleThreadExecutor();

    boolean notStarted = true;

    @Value("${SHIPMENT_PATH}")
    private String shipmentPath;

    public void submit(String input) {
        if (notStarted) {
            executor.submit(timer);
            notStarted = false;
        }
        queue.add(input);
        if (queue.size() == 5) {

            process(new HashSet<>(queue));
            timer.reset();
            queue.clear();
        }
    }

    public void process(Set<String> newQueue) {
        Map<String, List<String>> mapResult = new HashMap<>();
        logger.info("processing started for shipment");
        try {
            if(!newQueue.isEmpty()) {
                mapResult = restTemplate.getForObject(new URI(urlUtility.getRequiredUrl(shipmentPath, newQueue)), Map.class);
                logger.debug("Queue {}, Shipment Result: {}",newQueue,  mapResult);
            }

            if (!mapResult.isEmpty()) {
                result.putAll(mapResult);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        logger.info("processing done for shipment");
    }

    public List<String> getResult(String in) {
        return result.get(in);
    }

    public void clearResult(String[] shipList){
        Arrays.stream(shipList).forEach(ship -> {
            result.remove(ship);
        });
    }

    public Set<String> getQueue() {
        Set<String> newQueue = new HashSet<>(this.queue);
        this.queue.clear();
        return newQueue;
    }
}
