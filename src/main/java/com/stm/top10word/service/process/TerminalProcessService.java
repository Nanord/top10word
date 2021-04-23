package com.stm.top10word.service.process;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public interface TerminalProcessService<I, O> {
    CompletableFuture<O> start(String stopWord, BlockingQueue<I> word);
}
