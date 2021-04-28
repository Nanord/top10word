package com.stm.top10word.service.process.impl;

import com.stm.top10word.configuration.ExecutorConfiguration;
import com.stm.top10word.exception.ProcessException;
import com.stm.top10word.service.analytics.impl.Top10WordsServiceImpl;
import com.stm.top10word.service.filters.word.WordFilterService;
import com.stm.top10word.service.filters.word.impl.IsNotEmptyFilterServiceImpl;
import com.stm.top10word.service.filters.word.impl.IsNotNumericFilterServiceImpl;
import com.stm.top10word.service.filters.word.impl.WordLengthFilterServiceImpl;
import com.stm.top10word.service.process.StarterProcessService;
import com.stm.top10word.service.process.TerminalProcessService;
import com.stm.top10word.service.worker.QueueWorkerService;
import com.stm.top10word.service.worker.impl.FilterWordMapperQueueWorkerServiceImpl;
import com.stm.top10word.service.worker.impl.FindWordInLineMapperQueueWorkerServiceImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Top10WordProcessRunnerServiceImplTest {

    private ThreadPoolTaskExecutor threadPoolTaskExecutorStarter;

    private StarterProcessService<Path, String> starterProcessService;

    private TerminalProcessService<String, Map<String, Integer>> terminatedProcessService;

    private QueueWorkerService<String, String> findWordInLineWorker;

    private QueueWorkerService<String, String> filterWordWorker;

    private WordCountingProcessBuilderProcessImpl wordCountingProcessBuilderProcess;

    private Top10WordProcessRunnerServiceImpl top10WordProcessRunnerService;

    private Top10WordsServiceImpl top10WordsService;

    @Before
    public void prepare() {

        ExecutorConfiguration executorConfiguration = new ExecutorConfiguration();
        ReflectionTestUtils.setField(executorConfiguration, "threadPoolSizeStarter", 10);
        threadPoolTaskExecutorStarter = executorConfiguration.threadPoolTaskExecutorStarter();
        threadPoolTaskExecutorStarter.initialize();

        starterProcessService = new FileReaderStarterProcessService(threadPoolTaskExecutorStarter);
        ReflectionTestUtils.setField(starterProcessService, "queueSize", 3);

        terminatedProcessService = new WordCountingTerminalProcessServiceImpl(threadPoolTaskExecutorStarter);

        findWordInLineWorker = new FindWordInLineMapperQueueWorkerServiceImpl(threadPoolTaskExecutorStarter);
        ReflectionTestUtils.setField(findWordInLineWorker, "delimiter", "\\s+");
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

        top10WordsService = new Top10WordsServiceImpl();

        top10WordProcessRunnerService = new Top10WordProcessRunnerServiceImpl(wordCountingProcessBuilderProcess, top10WordsService);
    }

    @Test
    public void startProcess() throws ProcessException {
        String filePath = new File("").getAbsolutePath();
        Path path = Paths.get(filePath
                + File.separator + "src"
                + File.separator + "test"
                + File.separator + "java"
                + File.separator + "resources"
                + File.separator + "dirwithtextfiles");

        List<Pair<String, Integer>> top10 = top10WordProcessRunnerService.startProcess(path.toString());
        List<Pair<String, Integer>> expected = new ArrayList<Pair<String, Integer>>(){{
            add(Pair.of("Vestibulum", 6));
            add(Pair.of("augue.", 2));
            add(Pair.of("convallis", 2));
            add(Pair.of("fringilla", 2));
            add(Pair.of("porttitor", 2));
            add(Pair.of("turpis", 2));
        }};

        top10.sort(Comparator.comparing(Pair::getLeft));
        expected.sort(Comparator.comparing(Pair::getLeft));
        Assert.assertEquals(expected, top10);
    }
}