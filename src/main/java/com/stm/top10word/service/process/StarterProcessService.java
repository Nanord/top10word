package com.stm.top10word.service.process;


import com.stm.top10word.exception.ReaderException;

import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

public interface StarterProcessService<I, O> {
    BlockingQueue<O> start(I input, O stopWord);
}
