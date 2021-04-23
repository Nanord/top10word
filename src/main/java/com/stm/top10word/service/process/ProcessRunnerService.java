package com.stm.top10word.service.process;

import com.stm.top10word.exception.ProcessException;

public interface ProcessRunnerService<IN, OUT> {
    OUT startProcess(IN input) throws ProcessException;
}
