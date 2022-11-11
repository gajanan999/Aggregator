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
public class TrackingService implements ProcessingService {

    private static Logger logger = LoggerFactory.getLogger(TrackingService.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UrlUtility urlUtility;

    //Set<String> queue = new HashSet<>();
    Map<String, String> result = new HashMap<>();
    Set<String> queue = new LinkedHashSet<>();
    ExecutorService executor = Executors.newSingleThreadExecutor();
    boolean notStarted = true;
    Timer timer = new Timer(this);

    @Value("${TRACK_PATH}")
    private String trackPath;

    public void submit(String input) {
        if (notStarted) {
            executor.submit(timer);
            notStarted = false;
        }
        queue.add(input);
        if (queue.size() == 5) {
            process(new LinkedHashSet<>(queue));
            queue.clear();
            timer.reset();
        }
    }

    /**
     * Requesting the backend service API to fetch the response
     * @param newQueue
     */
    public void process(Set<String> newQueue) {
        Map<String, String> mapResult = new HashMap<>();
        logger.info("processing started for tracking");
        try {
            if(!newQueue.isEmpty()) {
                mapResult = restTemplate.getForObject(new URI(urlUtility.getRequiredUrl(trackPath, newQueue)), Map.class);
                logger.debug("Queue {} Tracking Result: {}",newQueue, mapResult);
            }

            if (mapResult != null && !mapResult.isEmpty()) {
                result.putAll(mapResult);
            }
        } catch (URISyntaxException e) {
            newQueue.forEach(item ->{
                result.put(item, "");
            });
            logger.error(e.getMessage(),e);
        }catch(Exception ex){
            newQueue.forEach(item ->{
                result.put(item, "");
            });
            logger.error(ex.getMessage(),ex);
        }
        logger.info("processing done for Tracking");
    }

    public String getResult(String in) {
        return result.get(in);
    }

    public void clearResult(String[] trackList){
        Arrays.stream(trackList).forEach(track -> {
            result.remove(track);
        });
    }

    public Set<String> getQueue() {
        Set<String> newQueue = new HashSet<>(this.queue);
        this.queue.clear();
        return newQueue;
    }
}
