package com.example.demo.service;

import com.example.demo.model.Parameter;
import org.springframework.stereotype.Service;
// import java.util.concurrent.ConcurrentHashMap;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

@Service
public class ParameterService {

    protected Cache<String, Parameter> cache = Caffeine.newBuilder()
        .maximumSize(10_000)
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build();
    
    // protected ConcurrentHashMap<String, Parameter> cache = new ConcurrentHashMap<>();

    public Parameter getParameterById(String id) {
        return cache.get(id, key -> fetchParameterFromExternalApi(key));
    }

    protected Parameter fetchParameterFromExternalApi(String id) {
        try {
            // Simulate network latency
            Thread.sleep(100); // 100 milliseconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Simulate fetching from an external API
        return new Parameter(id, "Name from external API");
    }

    // not implemented yet
    public void handleKafkaEvent(Parameter parameter) {
        cache.put(parameter.getId(), parameter);
    }
}
