package com.stm.top10word.service.process.impl;

import com.stm.top10word.configuration.ExecutorConfiguration;
import com.stm.top10word.service.filters.word.WordFilterService;
import com.stm.top10word.service.filters.word.impl.IsNotEmptyFilterServiceImpl;
import com.stm.top10word.service.filters.word.impl.IsNotNumericFilterServiceImpl;
import com.stm.top10word.service.filters.word.impl.WordLengthFilterServiceImpl;
import com.stm.top10word.service.process.StarterProcessService;
import com.stm.top10word.service.process.TerminalProcessService;
import com.stm.top10word.service.worker.QueueWorkerService;
import com.stm.top10word.service.worker.impl.FilterWordMapperQueueWorkerServiceImpl;
import com.stm.top10word.service.worker.impl.FindWordInLineMapperQueueWorkerServiceImpl;
import com.stm.top10word.utils.TestUtils;
import lombok.var;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class WordCountingProcessBuilderProcessImplTest {

    private ThreadPoolTaskExecutor threadPoolTaskExecutorStarter;

    private StarterProcessService<Path, String> starterProcessService;

    private TerminalProcessService<String, Map<String, Integer>> terminatedProcessService;

    private QueueWorkerService<String, String> findWordInLineWorker;

    private QueueWorkerService<String, String> filterWordWorker;

    private WordCountingProcessBuilderProcessImpl wordCountingProcessBuilderProcess;

    @Before
    public void prepare() {

        ExecutorConfiguration executorConfiguration = new ExecutorConfiguration();
        ReflectionTestUtils.setField(executorConfiguration, "threadPoolSizeReader", 10);
        threadPoolTaskExecutorStarter = executorConfiguration.threadPoolTaskExecutorStarter();
        threadPoolTaskExecutorStarter.initialize();

        starterProcessService = new FileReaderStarterProcessService(threadPoolTaskExecutorStarter);
        ReflectionTestUtils.setField(starterProcessService, "queueSize", 3);

        terminatedProcessService = new WordCountingTerminalProcessServiceImpl(threadPoolTaskExecutorStarter);
        ReflectionTestUtils.setField(terminatedProcessService, "queueSize", 3);

        findWordInLineWorker = new FindWordInLineMapperQueueWorkerServiceImpl(threadPoolTaskExecutorStarter);
        ReflectionTestUtils.setField(findWordInLineWorker, "delimiter", "\\\\W+");
        ReflectionTestUtils.setField(findWordInLineWorker, "queueSize", 3);

        List<WordFilterService> wordFilterServiceList = new ArrayList<>();
        wordFilterServiceList.add(new IsNotEmptyFilterServiceImpl());
        wordFilterServiceList.add(new IsNotNumericFilterServiceImpl());
        WordLengthFilterServiceImpl wordLengthFilterService = new WordLengthFilterServiceImpl();
        ReflectionTestUtils.setField(wordLengthFilterService, "wordLength", 5);

        wordFilterServiceList.add(wordLengthFilterService);

        filterWordWorker = new FilterWordMapperQueueWorkerServiceImpl(threadPoolTaskExecutorStarter, wordFilterServiceList);
        ReflectionTestUtils.setField(filterWordWorker, "queueSize", 3);

        wordCountingProcessBuilderProcess =
                new WordCountingProcessBuilderProcessImpl(starterProcessService,
                        terminatedProcessService,
                        findWordInLineWorker,
                        filterWordWorker);
    }

    @Test
    public void buildTest() throws ExecutionException, InterruptedException {
        String filePath = new File("").getAbsolutePath();
        Path path = Paths.get(filePath
                + File.separator + "src"
                + File.separator + "test"
                + File.separator + "java"
                + File.separator + "resources"
                + File.separator + "dirwithtextfiles"
                + File.separator + "file");

        CompletableFuture<Map<String, Integer>> result = wordCountingProcessBuilderProcess.build(path);
        assertEquals(3, result.get().get("Vestibulum").intValue());
    }

}
