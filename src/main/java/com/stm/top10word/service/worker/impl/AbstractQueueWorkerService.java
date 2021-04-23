package com.stm.top10word.service.worker.impl;

import com.stm.top10word.service.worker.QueueWorkerService;
import com.stm.top10word.utils.MultithreadingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

@RequiredArgsConstructor
public abstract class AbstractQueueWorkerService<I, O> implements QueueWorkerService<I, O> {
    @Value("${worker.queue.size}")
    private Integer queueSize;

    @Qualifier("threadPoolTaskExecutorWorker")
    private final ThreadPoolTaskExecutor threadPoolTaskExecutorWorker;

    @Override
    public BlockingQueue<O> doWork(I stopWord, O nextStopWord, BlockingQueue<I> inputQueue) {
        BlockingQueue<O> outputQueue = new LinkedBlockingQueue<>(queueSize);
        CompletableFuture.runAsync(() -> run(stopWord, nextStopWord, inputQueue, outputQueue), threadPoolTaskExecutorWorker)
                .exceptionally(ex -> MultithreadingUtils.handleException("Error during work", this.getClass().getName(), ex));
        return outputQueue;
    }

    abstract protected void run(I stopWord, O nextStopWord, BlockingQueue<I> inputQueue, BlockingQueue<O> outputQueue);
}
