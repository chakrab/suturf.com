package com.suturf;

import io.micronaut.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Singleton
public class TimePublisher {

    @Inject
    EchoPubProducer epub;

    private static final Logger LOG = LoggerFactory.getLogger(TimePublisher.class);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Scheduled(fixedDelay = "5s", initialDelay = "10s")
    void publishTime() {
        final String tm = ZonedDateTime.now().format(FMT);
        LOG.info("Publishing a message: {}", tm);
        epub.send(tm.getBytes(StandardCharsets.UTF_8));
    }
}
