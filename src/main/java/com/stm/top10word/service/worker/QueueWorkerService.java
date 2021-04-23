package com.stm.top10word.service.worker;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public interface QueueWorkerService<I, O> {
    BlockingQueue<O> doWork(I stopWord, O nextStopWord, BlockingQueue<I> start);
}
