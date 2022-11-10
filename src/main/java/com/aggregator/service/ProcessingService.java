package com.aggregator.service;

import java.util.HashSet;
import java.util.Set;

public interface ProcessingService {

    Set<String> queue = new HashSet<>();

    void process(Set<String> queue);

    Set<String> getQueue();
}
