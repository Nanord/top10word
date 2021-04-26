package com.stm.top10word.service.process.impl;

import com.stm.top10word.configuration.ExecutorConfiguration;
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
import java.util.concurrent.BlockingQueue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Deprecated
public class FileReaderStarterProcessServiceTest {

    private ThreadPoolTaskExecutor threadPoolTaskExecutorStarter;

    private FileReaderStarterProcessService fileReaderStarterProcessService;

    @Before
    public void prepare() {
        ExecutorConfiguration executorConfiguration = new ExecutorConfiguration();
        ReflectionTestUtils.setField(executorConfiguration, "threadPoolSizeReader", 6);
        threadPoolTaskExecutorStarter = executorConfiguration.threadPoolTaskExecutorStarter();
        fileReaderStarterProcessService = new FileReaderStarterProcessService(threadPoolTaskExecutorStarter);
        ReflectionTestUtils.setField(fileReaderStarterProcessService, "queueSize", 6);
        threadPoolTaskExecutorStarter.initialize();
        //TODO ?????
        //ReflectionTestUtils.setField(fileReaderStarterProcessService, "threadPoolTaskExecutorStarter", threadPoolTaskExecutorStarter);

    }

    @Test
    public void testReadFile() {
        String filePath = new File("").getAbsolutePath();
        Path path = Paths.get(filePath + "test"
                + File.separator+ "java"
                + File.separator + "resources"
                + File.separator + "dirwithtextfiles"
                + File.separator + "first.txt");

        BlockingQueue<String> fQ =  fileReaderStarterProcessService.start(path, "hello");

        System.out.println(fQ);
    }
}
