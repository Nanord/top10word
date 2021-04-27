package com.stm.top10word.service.worker.impl;

import com.stm.top10word.configuration.ExecutorConfiguration;
import com.stm.top10word.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FindWordInLineMapperQueueWorkerServiceImplTest {
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private final String stopWord = "STOPWORD";

    @Before
    public void prepare() {
        ExecutorConfiguration executorConfiguration = new ExecutorConfiguration();
        ReflectionTestUtils.setField(executorConfiguration, "threadPoolSizeReader", 10);
        threadPoolTaskExecutor = executorConfiguration.threadPoolTaskExecutorStarter();
        threadPoolTaskExecutor.initialize();
    }

    @Test
    public void workTest() {
        FindWordInLineMapperQueueWorkerServiceImpl findWordInLineMapperQueueWorkerService =
                new FindWordInLineMapperQueueWorkerServiceImpl(threadPoolTaskExecutor);
        ReflectionTestUtils.setField(findWordInLineMapperQueueWorkerService, "delimiter", "\\\\W+");
        ReflectionTestUtils.setField(findWordInLineMapperQueueWorkerService, "queueSize", 3);

        BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();

        inputQueue.offer("one");
        inputQueue.offer("one");
        inputQueue.offer("verilongword");
        inputQueue.offer("three");
        inputQueue.offer("four");
        inputQueue.offer("five");
        inputQueue.offer("longword");
        inputQueue.offer("longword");
        inputQueue.offer(stopWord);

        BlockingQueue<String> qResult = findWordInLineMapperQueueWorkerService.doWork(stopWord, stopWord , inputQueue);

        Map<String, Integer> result = new HashMap<>();
        TestUtils.queueWatcher(result, qResult, stopWord);
        assertEquals(2, result.get("longword").intValue());
    }
}
