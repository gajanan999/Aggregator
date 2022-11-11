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
import java.util.*;

public class ShipmentServiceTest {

    @InjectMocks
    ShipmentService shipmentService;

    @Mock
    RestTemplate restTemplate ;

    @InjectMocks
    UrlUtility urlUtility;

    private String shipmentPath;

    @BeforeEach
    public void init() throws URISyntaxException {
        Map<String, List<String>> mapResult = new HashMap<>();
        mapResult.put("109347263",Arrays.asList("pallet"));
        mapResult.put("123456891", Arrays.asList("envelope"));
        mapResult.put("109347264", Arrays.asList("envelope"));
        mapResult.put("123456892", Arrays.asList("envelope"));
        mapResult.put("123456893", Arrays.asList("pallet"));
        MockitoAnnotations.initMocks(this);

        Mockito.when(restTemplate.getForObject(new URI("http://localhost:8080/shipments?q=109347263,123456891,109347264,123456892,123456893"),Map.class)).
                thenReturn(mapResult);

        ReflectionTestUtils.setField(urlUtility, "baseUrl", "http://localhost:8080");
        ReflectionTestUtils.setField(shipmentService,"urlUtility",urlUtility);
        ReflectionTestUtils.setField(shipmentService, "shipmentPath", "/shipments");
    }

    @Test
    public void testShipments(){
        String ships = "109347263,123456891,109347264,123456892,123456893";
        String[] shipsList = ships.split(",");
        Arrays.stream(shipsList).forEach(ship -> {
            shipmentService.submit(ship);
        });
        Assertions.assertEquals(shipmentService.getResult(shipsList[0]).get(0),"pallet");
    }
}
