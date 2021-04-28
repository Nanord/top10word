package com.stm.top10word.service.process.impl;

import com.stm.top10word.configuration.ExecutorConfiguration;
import com.stm.top10word.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FileReaderStarterProcessServiceTest {

    private ThreadPoolTaskExecutor threadPoolTaskExecutorStarter;

    private FileReaderStarterProcessService fileReaderStarterProcessService;

    private final String stopWord = "STOP-WORD";

    @Before
    public void prepare() {
        ExecutorConfiguration executorConfiguration = new ExecutorConfiguration();
        ReflectionTestUtils.setField(executorConfiguration, "threadPoolSizeReader", 1);
        threadPoolTaskExecutorStarter = executorConfiguration.threadPoolTaskExecutorStarter();
        fileReaderStarterProcessService = new FileReaderStarterProcessService(threadPoolTaskExecutorStarter);
        ReflectionTestUtils.setField(fileReaderStarterProcessService, "queueSize", 3);
        threadPoolTaskExecutorStarter.initialize();
    }

    @Test
    public void testReadFile() {
        String filePath = new File("").getAbsolutePath();
        Path path = Paths.get(filePath
                + File.separator + "src"
                + File.separator + "test"
                + File.separator + "java"
                + File.separator + "resources"
                + File.separator + "dirwithtextfiles"
                + File.separator + "file");

        BlockingQueue<String> fQ =  fileReaderStarterProcessService.start(path, stopWord);

        Map<String, Integer> result = new HashMap<>();
        TestUtils.queueWatcher(result, fQ, stopWord);
        assertEquals(3, result.get("Vestibulum").intValue());
    }

}
