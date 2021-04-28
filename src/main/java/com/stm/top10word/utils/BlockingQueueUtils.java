package com.stm.top10word.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Класс со статическими методами для работы с BlockingQueue.
 *
 * @author STMLabs.
 */
@Slf4j
public class BlockingQueueUtils {

    /**
     * Время ожидания записи в очередь в минутах
     */
    private static Integer waitPut = 10;
    /**
     * Время ожидания чтения из очереди в минутах
     */
    private static Integer waitPoll = 10;

    public static <T> boolean putObjectInQueue(T object, BlockingQueue<T> blockingQueue) {
        return putObjectInQueue(object, blockingQueue, waitPut, TimeUnit.MINUTES);
    }

    /**
     * Вставка объекта в очередь
     *
     * @param object - Объект вставляемый в очередь
     * @param blockingQueue - Очередь объектов
     * @param time - время ожидания вставки в очередь
     * @param timeUnit - единица времени ожидания
     * @param <T> - тип объекта вставляемого в очередь
     * @return - успешная или неуспешная вставка.
     */
    public static <T> boolean putObjectInQueue(T object, BlockingQueue<T> blockingQueue, long time, TimeUnit timeUnit) {
        try {
            blockingQueue.offer(object, time, timeUnit);
            return true;
        } catch (InterruptedException e) {}
        log.warn("Queue is full! Not insert element: {}", object);
        return false;
    }

    public static <T> T takeObjectFromQueue(BlockingQueue<T> blockingQueue) {
        return takeObjectFromQueue(blockingQueue, waitPoll, TimeUnit.MINUTES);
    }

    /**
     * Получение объекта из очереди
     * @param blockingQueue - Очередь объектов
     * @param time - время ожидания вставки в очередь
     * @param timeUnit - единица времени ожидания
     * @param <T> - тип объекта вставляемого в очередь
     * @return - Следующий объект из очереди
     */
    public static <T> T takeObjectFromQueue(BlockingQueue<T> blockingQueue, long time, TimeUnit timeUnit) {
        try {
            return blockingQueue.poll(time, timeUnit);
        } catch (InterruptedException e) {
            log.info("Interrupted exception during take object from queue");
            return null;
        }
    }
}
