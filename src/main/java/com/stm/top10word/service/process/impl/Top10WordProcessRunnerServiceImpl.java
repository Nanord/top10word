package com.stm.top10word.service.process.impl;

import com.stm.top10word.exception.ProcessException;
import com.stm.top10word.service.process.BuilderProcessService;
import com.stm.top10word.service.process.ProcessRunnerService;
import com.stm.top10word.utils.CommonUtils;
import com.stm.top10word.utils.MultithreadingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
@Service
public class Top10WordProcessRunnerServiceImpl implements ProcessRunnerService<String, List<Pair<String, Integer>>> {

    @Qualifier("wordCountingProcessBuilder")
    private final BuilderProcessService<Path, CompletableFuture<Map<String, Integer>>> wordCountingProcessBuilder;

    private Map<String, Integer> wordCountMap = new ConcurrentHashMap<>();
    private Comparator<Pair<String, Integer>> comparator = Comparator.comparingInt(Pair::getValue);

    @Override
    public List<Pair<String, Integer>> startProcess(String pathFolder) throws ProcessException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        CompletableFuture<List<Void>> process = receiveFiles(pathFolder)
                .filter(Files::isReadable)
                .map(wordCountingProcessBuilder::build)
                .filter(Objects::nonNull)
                .map(analysisFuture -> analysisFuture.thenAccept(this::combineWordCountMap))
                .collect(Collectors.collectingAndThen(Collectors.toList(), MultithreadingUtils.joinResult()));
        MultithreadingUtils.awaitCompletableFuture(process);
        log.info("Count word:\n {}", wordCountMap.size());
        List<Pair<String, Integer>> top10Words = receiveTop10Words();
        stopWatch.stop();
        CommonUtils.printExecutionTime("Common work", stopWatch.getLastTaskTimeMillis());
        return top10Words;
    }

    private List<Pair<String, Integer>> receiveTop10Words() {
        PriorityQueue<Pair<String, Integer>> priorityQueue = new PriorityQueue<>(10, comparator);
        for (String word : wordCountMap.keySet()) {
            Pair<String, Integer> tempPair = priorityQueue.peek();
            Pair<String, Integer> newPair = Pair.of(word, wordCountMap.get(word));
            if (priorityQueue.size() < 10) {
                priorityQueue.add(newPair);
                continue;
            }
            if(comparator.compare(newPair, tempPair) > 0) {
                priorityQueue.poll();
                priorityQueue.add(newPair);
            }
            wordCountMap.remove(word);
        }
        return new ArrayList<>(priorityQueue);
    }

    private void combineWordCountMap(Map<String, Integer> wordCountMapFromFile) {
        wordCountMapFromFile.forEach((word, count) -> wordCountMap
                .compute(word, (key, value) -> Optional
                        .ofNullable(value)
                        .map(v -> v + count)
                        .orElse(count)));
    }

    private Stream<Path> receiveFiles(String pathFolder) throws ProcessException {
        Path directory = Paths.get(pathFolder);
        if (!Files.isDirectory(directory)) {
            throw new ProcessException("Path is not directory!");
        }
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new ProcessException(e);
        }
    }
}
