package com.aggregator.service;

import com.aggregator.utility.UrlUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TrackingServiceTest {

    @InjectMocks
    TrackingService trackingService;

    @Mock
    RestTemplate restTemplate ;

    @InjectMocks
    UrlUtility urlUtility;

    private String trackPath;


    @BeforeEach
    public void init() throws URISyntaxException {
        Map<String, String> mapResult = new HashMap<>();
        mapResult.put("109347263","NEW");
        mapResult.put("123456891", "DELIVERING");
        mapResult.put("109347264", "NEW");
        mapResult.put("123456892", "DELIVERING");
        mapResult.put("123456893", "NEW");
        MockitoAnnotations.initMocks(this);

        Mockito.when(restTemplate.getForObject(new URI("http://localhost:8080/track?q=109347263,123456891,109347264,123456892,123456893"),Map.class)).
                thenReturn(mapResult);

        ReflectionTestUtils.setField(urlUtility, "baseUrl", "http://localhost:8080");
        ReflectionTestUtils.setField(trackingService,"urlUtility",urlUtility);
        ReflectionTestUtils.setField(trackingService, "trackPath", "/track");
    }

    @Test
    public void testTracking(){
        String tracks = "109347263,123456891,109347264,123456892,123456893";
        String[] trackList = tracks.split(",");
        Arrays.stream(trackList).forEach(track -> {
            trackingService.submit(track);
        });
        Assertions.assertEquals(trackingService.getResult(trackList[0]),"NEW");
    }
}
