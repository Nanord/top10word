package com.stm.top10word.service.worker.impl;

import com.stm.top10word.utils.BlockingQueueUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;

public abstract class AbstractMapperQueueWorkerService<I, O> extends AbstractQueueWorkerService<I, O> {

    public AbstractMapperQueueWorkerService(ThreadPoolTaskExecutor threadPoolTaskExecutorWorker) {
        super(threadPoolTaskExecutorWorker);
    }

    @Override
    protected void run(I stopWord, O nextStopWord, BlockingQueue<I> inputQueue, BlockingQueue<O> outputQueue) {
        I input = BlockingQueueUtils.takeObjectFromQueue(inputQueue);
        while (input != null && !Objects.equals(stopWord, input)) {
            O output = work(input);
            if (Objects.nonNull(output)) {
                BlockingQueueUtils.putObjectInQueue(output, outputQueue);
            }
            input = BlockingQueueUtils.takeObjectFromQueue(inputQueue);
        }
        BlockingQueueUtils.putObjectInQueue(nextStopWord, outputQueue);
    }

    abstract protected O work(I input);
}
