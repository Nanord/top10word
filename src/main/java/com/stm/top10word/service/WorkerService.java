package com.stm.top10word.service;

public interface WorkerService<IN, OUT> {
    OUT doWork(IN input);
}
