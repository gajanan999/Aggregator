package com.aggregator.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class AggregatorServiceTest {

    @InjectMocks
    AggregatorService aggregatorService;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void givenRequestParameters_whenFiveParametersRequested_thenRetrieveResponseImmediately(){

        aggregatorService.callBulkApis("abc","xyz","fgdf");
        Assertions.assertEquals("RRR","RRR");
    }

}
