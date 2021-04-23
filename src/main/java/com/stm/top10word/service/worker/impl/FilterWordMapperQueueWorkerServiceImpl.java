package com.stm.top10word.service.worker.impl;

import com.stm.top10word.service.filters.FilterService;
import com.stm.top10word.service.filters.word.WordFilterService;
import com.stm.top10word.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Component("filterWordWorker")
@Slf4j
public class FilterWordMapperQueueWorkerServiceImpl extends AbstractMapperQueueWorkerService<String, String> {

    private final List<WordFilterService> wordFilterService;

    public FilterWordMapperQueueWorkerServiceImpl(ThreadPoolTaskExecutor threadPoolTaskExecutorWorker, List<WordFilterService> wordFilterService) {
        super(threadPoolTaskExecutorWorker);
        wordFilterService.sort(Comparator.comparing(FilterService::getPriority, Integer::compareTo));
        this.wordFilterService = wordFilterService;
    }

    @Override
    protected String work(String word) {
        return filter(word) ? word : null;
    }

    private boolean filter(String word) {
        for (WordFilterService filter : wordFilterService) {
            if (!filter.check(word)) {
                return false;
            }
        }
        return true;
    }
}
