package com.example.demo.service;

import com.example.demo.model.Parameter;
import org.springframework.stereotype.Service;
// import java.util.concurrent.ConcurrentHashMap;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ParameterService {

    // Create a logger instance for this class
    private static final Logger logger = LoggerFactory.getLogger(ParameterService.class);

    protected Cache<String, Parameter> cache = Caffeine.newBuilder()
        .maximumSize(10_000)
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build();
    
    // protected ConcurrentHashMap<String, Parameter> cache = new ConcurrentHashMap<>();

    public Parameter getParameterById(String id) {
        // Blocks the cache id until the compute method returns
        return cache.get(id, key -> fetchParameterFromExternalApi(key));
    }

    public boolean parameterExists(String id) {
        return cache.getIfPresent(id) != null;
    }

    protected Parameter fetchParameterFromExternalApi(String id) {
        try {
            // Simulate network latency
            Thread.sleep(2000); // 100 milliseconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        logger.info("Responding with external api parameter: " + id);
        // Simulate fetching from an external API
        return new Parameter(id, "Name from external API");
    }

    // not implemented yet
    public void handleKafkaEvent(Parameter parameter) {
        logger.info("Received kafka parameter: " + parameter);
        cache.put(parameter.getId(), parameter);
        logger.info("put kafka message");
        Parameter parameterCached = cache.getIfPresent(parameter.getId());
        logger.info("Paqrameter cached: " + parameterCached.getName());
    }
}
