package com.stm.top10word.service.process.impl;

import com.stm.top10word.configuration.ExecutorConfiguration;
import com.stm.top10word.service.process.StarterProcessService;
import com.stm.top10word.service.process.TerminalProcessService;
import com.stm.top10word.service.worker.QueueWorkerService;
import com.stm.top10word.utils.BlockingQueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;

public class WordCountingTerminalProcessServiceImplTest {

    private ThreadPoolTaskExecutor threadPoolTaskExecutorStarter;

    private TerminalProcessService<String, Map<String, Integer>> terminatedProcessService;

    @Before
    public void setUp() {
        ExecutorConfiguration executorConfiguration = new ExecutorConfiguration();
        ReflectionTestUtils.setField(executorConfiguration, "threadPoolSizeReader", 10);
        threadPoolTaskExecutorStarter = executorConfiguration.threadPoolTaskExecutorStarter();
        threadPoolTaskExecutorStarter.initialize();

        terminatedProcessService = new WordCountingTerminalProcessServiceImpl(threadPoolTaskExecutorStarter);
        ReflectionTestUtils.setField(terminatedProcessService, "queueSize", 3);

    }

    @Test
    public void start() throws ExecutionException, InterruptedException {

        String stopWord = UUID.randomUUID().toString();

        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(3);
        BlockingQueueUtils.putObjectInQueue("Vestibulum", blockingQueue);
        BlockingQueueUtils.putObjectInQueue("Endgame", blockingQueue);
        BlockingQueueUtils.putObjectInQueue("Vestibulum", blockingQueue);


        CompletableFuture<Map<String, Integer>> result = terminatedProcessService.start(stopWord, blockingQueue);
        assertEquals(2, result.get().get("Vestibulum").intValue());
    }
}