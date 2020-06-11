package com.suturf;

import io.micronaut.configuration.rabbitmq.annotation.Queue;
import io.micronaut.configuration.rabbitmq.annotation.RabbitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

@RabbitListener
public class EchoSubListener {

    private static final Logger LOG = LoggerFactory.getLogger(EchoSubListener.class);

    @Queue("testsub")
    public void receive(byte[] data) {
        LOG.info("Got Message value: {}",
                new String(data, StandardCharsets.UTF_8));
    }
}