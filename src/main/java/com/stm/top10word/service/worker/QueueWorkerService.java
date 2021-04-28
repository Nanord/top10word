package com.stm.top10word.service.worker;

import java.util.concurrent.BlockingQueue;

/**
 * Интерфейс для части процесса по преобразованию и отсеиванию данных
 *
 * @param <I> - Тип входного параметра
 * @param <O> - Тип выходного параметра
 *
 * @author STMLabs.
 */
public interface QueueWorkerService<I, O> {
    /**
     * @param stopWord - Стоп объект для прекращения чтения объектов из очереди
     * @param nextStopWord -  -Стоп объект для обозначения следущей работе о завершении своей части процесса
     * @param inputQueue - Очередь объектов от предыдущей части процесса
     * @return - Очередь объектов, из которую читает следующая часть процесса
     */
    BlockingQueue<O> doWork(I stopWord, O nextStopWord, BlockingQueue<I> inputQueue);
}
