package com.stm.top10word.service.process;


import java.util.concurrent.BlockingQueue;

/**
 * Интерфейс для начальной точки запуска процесса
 *
 * @param <I> - Тип входного параметра
 * @param <O> - Тип выходного параметра
 *
 * @author STMLabs.
 */
public interface StarterProcessService<I, O> {
    /**
     * Стартовая точнка запуска процесса
     *
     * @param input - Входной объект для старата процесса
     * @param stopWord - Стоп объект для обозначения следущей работе о завершении
     * @return - Очередь объектов, из которой читает следующая часть процесса
     */
    BlockingQueue<O> start(I input, O stopWord);
}
