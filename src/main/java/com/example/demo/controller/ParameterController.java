package com.example.demo.controller;

import com.example.demo.model.Parameter;
import com.example.demo.service.ParameterService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parameters")
public class ParameterController {

    @Autowired
    private ParameterService parameterService;

    @Autowired
    private KafkaTemplate<String, Parameter> kafkaTemplate;

    @GetMapping("/{id}")
    public Parameter getParameter(@PathVariable String id) {
        
        // Try to get the parameter from cache
        CompletableFuture<Parameter> parameterFuture = CompletableFuture.supplyAsync(() -> parameterService.getParameterById(id));

        // Send out a kafka message that will populate the cache with the parameter
        // The first request to this controller will be populated by the compute method.  Subsequent requests will be populated by the kafka message
        // in the cache
        CompletableFuture.runAsync(() -> {
            try {
                // Simulate a delay to overlap with getParameterById
                
                // Create a Parameter object to send
                Parameter kafkaParameter = new Parameter(id, "Value from Kafka");
                
                // Check if the parameter already exists in the cache
                if (!parameterService.parameterExists(id)) {
                    // Send the parameter to the Kafka topic
                    kafkaTemplate.send("parameter-topic", kafkaParameter);
                };
            } catch (Exception e) {
                // Handle exceptions appropriately (e.g., log the error)
                e.printStackTrace();
            }
        });

        // return parameterService.getParameterById(id);
        // Wait for the parameter to be retrieved
        try {
            // Get the parameter result
            Parameter parameter = parameterFuture.get();
            return parameter;
        } catch (InterruptedException | ExecutionException e) {
            // Handle exceptions appropriately
            throw new RuntimeException("Error retrieving parameter", e);
        }
    }
}
