package com.aggregator.timer;

import com.aggregator.service.ProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class Timer implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(Timer.class);

    LocalDateTime end;
    ProcessingService processingService;

    public Timer(ProcessingService processingService) {
        this.processingService = processingService;
        end = LocalDateTime.now().plusSeconds(5);
    }

    @Override
    public void run() {
        while (true) {
            //System.out.println("running thread before timer");
            while (LocalDateTime.now().isAfter(end)) {
                logger.debug("Timer threshold reached " + processingService.getClass().getName());

                processingService.process(processingService.getQueue());

                reset();
                break;
            }
        }
    }

    public void reset() {
        end = LocalDateTime.now().plusSeconds(5);
    }
}
