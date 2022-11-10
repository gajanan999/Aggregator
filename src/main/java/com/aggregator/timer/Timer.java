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
        end = LocalDateTime.now().plusSeconds(30);
    }

    @Override
    public void run() {
        while (true) {

            while (LocalDateTime.now().isAfter(end)) {
                logger.debug("ThreadId: {}",String.valueOf(Thread.currentThread().getId()));
                logger.debug("Timer threshold reached {} " + this.processingService.getClass().getName(), this.processingService);

                this.processingService.process(this.processingService.getQueue());

                reset();
                break;
            }
        }
    }

    public void reset() {
        end = LocalDateTime.now().plusSeconds(30);
    }
}
