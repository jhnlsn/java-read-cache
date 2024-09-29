package com.example.demo.listener;

import com.example.demo.model.Parameter;
import com.example.demo.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ParameterKafkaListener {

    @Autowired
    private ParameterService parameterService;

    @KafkaListener(topics = "parameter-topic", groupId = "parameter-group")
    public void listen(Parameter parameter) {
        // could use AtomicBoolean to check if the parameter exists in the cache
        // could use SETNX in redis to check if the parameter exists in the cache
        parameterService.handleKafkaEvent(parameter);
    }
}
