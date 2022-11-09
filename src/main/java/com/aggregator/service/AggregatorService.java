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

    public Map<String, Object> callBulkApis(String tracks, String ships, String prices) {

        Map<String, Object> finalResult = new HashMap<>();
        // R1 -- track, ship, price
        String[] shipList = ships.split(",");
        Arrays.stream(shipList).forEach(ship -> {
            shipmentService.submit(ship);
        });

        String[] priceList = prices.split(",");
        Arrays.stream(priceList).forEach(price -> {
            pricingService.submit(price);
        });

        String[] trackList = tracks.split(",");
        Arrays.stream(trackList).forEach(track -> {
            trackingService.submit(track);
        });

        LOGGER.info("Submitted all requests");


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
        finalResult.put("pricing", pricingResult);
        finalResult.put("shipments", shipResult);
        finalResult.put("track", trackResult);
        return finalResult;

    }
}
