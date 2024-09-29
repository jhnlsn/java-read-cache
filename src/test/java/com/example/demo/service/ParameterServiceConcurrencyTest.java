package com.example.demo.service;

import com.example.demo.model.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ParameterServiceConcurrencyTest {

    private ParameterService parameterService;

    @BeforeEach
    public void setUp() {
        parameterService = new ParameterService();

        // Spy on parameterService to monitor method calls
        parameterService = Mockito.spy(parameterService);

        // Optionally, clear the cache before each test
        parameterService.cache.invalidateAll();
    }

    @Test
    public void testConcurrentGetParameterById() throws InterruptedException {
        // Number of concurrent threads
        int threadCount = 10;

        // Synchronization aids
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        // Shared collection to store results
        ConcurrentLinkedQueue<Parameter> results = new ConcurrentLinkedQueue<>();

        // Create and start threads
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    readyLatch.countDown(); // Signal that this thread is ready
                    startLatch.await();     // Wait for the start signal

                    // Call the method under test
                    Parameter parameter = parameterService.getParameterById("123");
                    results.add(parameter);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();  // Signal that this thread is done
                }
            });
        }

        // Wait until all threads are ready
        readyLatch.await();
        // Start all threads at once
        startLatch.countDown();
        // Wait for all threads to finish
        doneLatch.await();

        // Shutdown the executor service
        executorService.shutdown();

        // Verify that fetchParameterFromExternalApi was called only once
        Mockito.verify(parameterService, times(1)).fetchParameterFromExternalApi("123");

        // Assert that all results are the same instance
        Parameter expectedParameter = results.peek();
        for (Parameter parameter : results) {
            assertSame(expectedParameter, parameter, "All threads should receive the same Parameter instance");
        }
    }
}
