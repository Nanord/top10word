package com.stm.top10word.service.process;

import java.util.concurrent.CompletableFuture;

public interface BuilderProcessService<I, O> {
    O build(I input);
}
