package com.stm.top10word.service;

public interface ProcessService<IN, OUT> {
    OUT startProcess(IN input);
}
