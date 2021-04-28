package com.stm.top10word.service.process;


import java.util.concurrent.BlockingQueue;

public interface StarterProcessService<I, O> {
    BlockingQueue<O> start(I input, O stopWord);
}
