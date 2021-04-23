package com.stm.top10word.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BlockingQueueUtils {

    private static Integer waitPut = 10;
    private static Integer waitPoll = 01;

    public static <T> boolean putObjectInQueue(T object, BlockingQueue<T> blockingQueue) {
        return putObjectInQueue(object, blockingQueue, waitPut, TimeUnit.MINUTES);
    }

    public static <T> boolean putObjectInQueue(T object, BlockingQueue<T> blockingQueue, long time, TimeUnit timeUnit) {
        try {
            blockingQueue.offer(object, time, timeUnit);
            return true;
        } catch (InterruptedException e) {
            log.warn("Queue is full! Not insert element: {}", object, e);
        }
        log.warn("Queue is full! Not insert element: {}", object);
        return false;
    }

    public static <T> T takeObjectFromQueue(BlockingQueue<T> blockingQueue) {
        return takeObjectFromQueue(blockingQueue, waitPoll, TimeUnit.MINUTES);
    }

    public static <T> T takeObjectFromQueue(BlockingQueue<T> blockingQueue, long time, TimeUnit timeUnit) {
        try {
            return blockingQueue.poll(time, timeUnit);
        } catch (InterruptedException e) {
            log.info("Interrupted exception during take object from queue");
            return null;
        }
    }
}
