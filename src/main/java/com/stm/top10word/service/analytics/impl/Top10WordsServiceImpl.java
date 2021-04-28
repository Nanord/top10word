package com.stm.top10word.service.analytics.impl;

import com.google.common.collect.ImmutableMap;
import com.stm.top10word.service.analytics.Top10WordsService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class Top10WordsServiceImpl implements Top10WordsService {

    private Map<String, Integer> wordCountMap = new ConcurrentHashMap<>();
    private Comparator<Pair<String, Integer>> comparator = Comparator.comparingInt(Pair::getValue);

    @Override
    public void resetWordCountMap() {
        wordCountMap.clear();
    }

    @Override
    public void combineWordCountMap(Map<String, Integer> wordCountMapFromFile) {
        wordCountMapFromFile.forEach((word, count) -> wordCountMap
                .compute(word, (key, value) -> Optional
                        .ofNullable(value)
                        .map(v -> v + count)
                        .orElse(count)));
    }

    @Override
    public int getWordCountMapSize() {
        return wordCountMap.size();
    }
    @Override
    public Map<String, Integer> getWordCountMap() {
        return ImmutableMap.copyOf(wordCountMap);
    }

    @Override
    public List<Pair<String, Integer>> receiveTop10Words() {
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
}
