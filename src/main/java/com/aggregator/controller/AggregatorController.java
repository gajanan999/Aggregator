package com.aggregator.controller;


import com.aggregator.service.AggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AggregatorController {


    @Autowired
    AggregatorService aggregatorService;

    @GetMapping("/aggregation")
    public Map<String, Object> getResult(@RequestParam String pricing, @RequestParam String track, @RequestParam String shipments ){
        return aggregatorService.callBulkApis(track,shipments,pricing);
    }

}
