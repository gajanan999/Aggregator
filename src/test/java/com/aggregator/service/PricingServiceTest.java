package com.aggregator.service;

import com.aggregator.utility.UrlUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class PricingServiceTest {
    @InjectMocks
    PricingService pricingService;
    @Mock
    RestTemplate restTemplate ;
    @InjectMocks
    UrlUtility urlUtility;

    private String pricingPath;

    @BeforeEach
    public void init() throws URISyntaxException {
        Map<String, Double> mapResult = new HashMap<>();
        mapResult.put("109347263",98.57359813316681);
        MockitoAnnotations.initMocks(this);
        Mockito.when(restTemplate.getForObject(new URI("http://localhost:8080/pricing?q=109347263,123456891,109347264,123456892,123456893"),Map.class)).
                thenReturn(mapResult);
        ReflectionTestUtils.setField(urlUtility, "baseUrl", "http://localhost:8080");
        ReflectionTestUtils.setField(pricingService,"urlUtility",urlUtility);
        ReflectionTestUtils.setField(pricingService, "pricingPath", "/pricing");
    }

    @Test
    public void testPricing(){
        String prices = "109347263,123456891,109347264,123456892,123456893";
        String[] priceList = prices.split(",");
        Arrays.stream(priceList).forEach(price -> {
            pricingService.submit(price);
        });
        Assertions.assertEquals(pricingService.getResult(priceList[0]),98.57359813316681);
    }
}
