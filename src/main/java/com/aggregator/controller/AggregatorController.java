package com.aggregator.controller;


import com.aggregator.service.AggregatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AggregatorController {


    private static final Logger LOGGER = LoggerFactory.getLogger(AggregatorController.class);

    @Autowired
    private AggregatorService aggregatorService;

    @GetMapping("/aggregation")
    public Map<String, Object> getResult(@RequestParam String pricing, @RequestParam String track, @RequestParam String shipments) {
        LOGGER.debug("Incoming request pricing {}, track {}, shipments {}", pricing, track, shipments);
        return aggregatorService.callBulkApis(pricing, track, shipments);
    }

}
