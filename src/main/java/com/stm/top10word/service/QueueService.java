package com.stm.top10word.service;

public interface QueueService<T> {
    void put(T T);
    T pull();
}
