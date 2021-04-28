package com.stm.top10word.service.analytics;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

public interface Top10WordsService {
    void combineWordCountMap(Map<String, Integer> wordCountMapFromFile);

    void resetWordCountMap();

    int getWordCountMapSize();

    Map<String, Integer> getWordCountMap();

    List<Pair<String, Integer>> receiveTop10Words();
}
