package com.aggregator.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AggregatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AggregatorService.class);

    @Autowired
    private PricingService pricingService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private TrackingService trackingService;

    public Map<String, Object> callBulkApis(String prices, String tracks, String shipments) {

        Map<String, Object> finalResult = new HashMap<>();

        String[] priceList = prices.split(",");
        String[] trackList = tracks.split(",");
        String[] shipList = shipments.split(",");

        submitAllParameters(priceList, trackList, shipList);

        LOGGER.info("Submitted all requests parameters");
        LOGGER.info("Waiting for all the request to full fill");
        waitForAllRequesttoFullFill(priceList, trackList, shipList);
        LOGGER.info("All request has been full filled");

        // submit
        Map<String, Double> pricingResult = new HashMap<>();
        Arrays.stream(priceList).forEach(price -> {
            pricingResult.put(price, pricingService.getResult(price));
        });

        Map<String, List<String>> shipResult = new HashMap<>();
        Arrays.stream(shipList).forEach(ship -> {
            shipResult.put(ship, shipmentService.getResult(ship));
        });


        Map<String, String> trackResult = new HashMap<>();
        Arrays.stream(trackList).forEach(track -> {
            trackResult.put(track, trackingService.getResult(track));
        });

        LOGGER.info("Combined all the responses");

        pricingService.clearResult(priceList);
        trackingService.clearResult(trackList);
        shipmentService.clearResult(shipList);
        LOGGER.info("Cleared all the responses");
        finalResult.put("pricing", pricingResult);
        finalResult.put("shipments", shipResult);
        finalResult.put("track", trackResult);

        LOGGER.info("Created the final result {}", finalResult);
        return finalResult;

    }

    /**
     * waiting for all the APIs until 5 sec or to fill the queue with 5 request parameters.
     * @param priceList
     * @param trackList
     * @param shipList
     */
    private void waitForAllRequesttoFullFill(String[] priceList, String[] trackList, String[] shipList) {
        for (int i = 0; i < shipList.length; i++) {
            while (shipmentService.getResult(shipList[i]) == null) {
                // Waiting for result
            }
        }

        for (int i = 0; i < priceList.length; i++) {
            while (pricingService.getResult(priceList[i]) == null) {
                // Waiting for result
            }
        }

        for (int i = 0; i < trackList.length; i++) {
            while (trackingService.getResult(trackList[i]) == null) {
                // Waiting for result
            }
        }

    }


    /**
     * submitting all the parameters to the each api service
     * @param priceList
     * @param trackList
     * @param shipList
     */
    private void submitAllParameters(String[] priceList, String[] trackList, String[] shipList) {
        Arrays.stream(priceList).forEach(price -> {
            pricingService.submit(price);
        });

        Arrays.stream(trackList).forEach(track -> {
            trackingService.submit(track);
        });

        Arrays.stream(shipList).forEach(ship -> {
            shipmentService.submit(ship);
        });
    }


}
