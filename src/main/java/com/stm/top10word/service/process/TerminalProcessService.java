package com.stm.top10word.service.process;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

/**
 * Интерфейс для конечной точки процесса, возвращающей результат
 *
 * @param <I> - Тип входного параметра
 * @param <O> - Тип выходного параметра
 *
 * @author STMLabs.
 */
public interface TerminalProcessService<I, O> {
    /**
     * @param stopWord - Стоп объект для прекращения чтения объектов из очереди
     * @param inputQueue - Очередь объектов от предыдущей части процесса
     * @return - CompletableFuture с результатом процесса.
     */
    CompletableFuture<O> start(String stopWord, BlockingQueue<I> inputQueue);
}
