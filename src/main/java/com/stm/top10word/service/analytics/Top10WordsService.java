package com.stm.top10word.service.analytics;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

/**
 * Класс для подсчета слов во всех файлах и поиска 10 самых часто используемых.
 *
 * @author STMLabs
 */
public interface Top10WordsService {

    /**
     * Объединение использований слов в файле в общий словарь.
     *
     * @param wordCountMapFromFile - Словарь слов и кол-ва их использования.
     */
    void combineWordCountMap(Map<String, Integer> wordCountMapFromFile);

    /**
     * Очистить общий словарь.
     */
    void resetWordCountMap();

    /**
     * Возвращает кол-во уникальных слов со всех файлов.
     *
     * @return Кол-во уникальных слов.
     */
    int getWordCountMapSize();

    /**
     * Для тестирования. Возвращает копию общего словаря.
     *
     * @return копия общего словаря.
     */
    Map<String, Integer> getWordCountMap();

    /**
     * Посчет 10 самых часто использованых слов.
     * @return Список из 10 самых часто использованых слов.
     */
    List<Pair<String, Integer>> receiveTop10Words();
}
