package com.stm.top10word.service.process.impl;

import com.stm.top10word.configuration.ExecutorConfiguration;
import com.stm.top10word.service.process.TerminalProcessService;
import com.stm.top10word.utils.BlockingQueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.assertEquals;

public class WordCountingTerminalProcessServiceImplTest {

    private ThreadPoolTaskExecutor threadPoolTaskExecutorTerminated;

    private TerminalProcessService<String, Map<String, Integer>> terminatedProcessService;

    @Before
    public void setUp() {
        ExecutorConfiguration executorConfiguration = new ExecutorConfiguration();
        ReflectionTestUtils.setField(executorConfiguration, "threadPoolSizeTerminated", 10);
        threadPoolTaskExecutorTerminated = executorConfiguration.threadPoolTaskExecutorTerminated();
        threadPoolTaskExecutorTerminated.initialize();

        terminatedProcessService = new WordCountingTerminalProcessServiceImpl(threadPoolTaskExecutorTerminated);

    }

    @Test
    public void start() throws ExecutionException, InterruptedException {

        String stopWord = UUID.randomUUID().toString();

        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(4);
        BlockingQueueUtils.putObjectInQueue("Vestibulum", blockingQueue);
        BlockingQueueUtils.putObjectInQueue("Endgame", blockingQueue);
        BlockingQueueUtils.putObjectInQueue("Vestibulum", blockingQueue);
        BlockingQueueUtils.putObjectInQueue(stopWord, blockingQueue);

        CompletableFuture<Map<String, Integer>> result = terminatedProcessService.start(stopWord, blockingQueue);
        assertEquals(2, result.get().get("Vestibulum").intValue());
    }
}