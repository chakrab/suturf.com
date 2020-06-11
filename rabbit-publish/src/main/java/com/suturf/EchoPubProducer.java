package com.suturf;

import io.micronaut.configuration.rabbitmq.annotation.Binding;
import io.micronaut.configuration.rabbitmq.annotation.RabbitClient;

@RabbitClient("testpub")
public interface EchoPubProducer {

    @Binding("testkey")
    void send(byte[] data);
}