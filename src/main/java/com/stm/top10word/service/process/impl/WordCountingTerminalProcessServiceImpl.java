package com.stm.top10word.service.process.impl;

import com.stm.top10word.service.process.TerminalProcessService;
import com.stm.top10word.utils.BlockingQueueUtils;
import com.stm.top10word.utils.CommonUtils;
import com.stm.top10word.utils.MultithreadingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

@Service("wordCountingTerminalProcess")
@Slf4j
@RequiredArgsConstructor
public class WordCountingTerminalProcessServiceImpl implements TerminalProcessService<String, Map<String, Integer>> {

    @Qualifier("threadPoolTaskExecutorTerminated")
    private final ThreadPoolTaskExecutor threadPoolTaskExecutorTerminated;
    @Value("${terminated.queue.size}")
    private Integer queueSize;

    @Override
    public CompletableFuture<Map<String, Integer>> start(String stopWord, BlockingQueue<String> inputQueue) {
        return CompletableFuture
                .supplyAsync(() -> combineWordCountMap(stopWord, inputQueue), threadPoolTaskExecutorTerminated)
                .exceptionally(ex -> MultithreadingUtils
                        .handleException("Error during combine wordCount",
                                this.getClass().getName(),
                                Collections.emptyMap(),
                                ex));
    }

    private Map<String, Integer> combineWordCountMap(String stopWord, BlockingQueue<String> inputQueue) {
        log.info("Start terminate");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Map<String, Integer> result = new HashMap<>();
        String word = BlockingQueueUtils.takeObjectFromQueue(inputQueue);
        int i = 0;
        while (word != null && !Objects.equals(stopWord, word)) {
            putWordToMap(word, result);
            i += 1;
            word = BlockingQueueUtils.takeObjectFromQueue(inputQueue);
        }
        if(Objects.equals(stopWord, word)) {
            System.out.println("stopSLOVO ter");
        } else {
            System.out.println("null term(");
        }
        stopWatch.stop();
        CommonUtils.printExecutionTime("Terminal process. " +  i + " words counted",
                stopWatch.getLastTaskTimeMillis());
        return result;
    }

    private void putWordToMap(String s, Map<String, Integer> map) {
        map.compute(s, (key, value) -> Optional
                .ofNullable(value)
                .map(v -> v + 1)
                .orElse(1));
    }
}
