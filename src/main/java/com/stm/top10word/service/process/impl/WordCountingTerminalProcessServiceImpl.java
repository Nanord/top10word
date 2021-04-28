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
        Map<String, Integer> result = new HashMap<>();
        String word = BlockingQueueUtils.takeObjectFromQueue(inputQueue);
        while (word != null && !Objects.equals(stopWord, word)) {
            putWordToMap(word, result);
            word = BlockingQueueUtils.takeObjectFromQueue(inputQueue);
        }
        return result;
    }

    private void putWordToMap(String s, Map<String, Integer> map) {
        map.compute(s, (key, value) -> Optional
                .ofNullable(value)
                .map(v -> v + 1)
                .orElse(1));
    }
}
