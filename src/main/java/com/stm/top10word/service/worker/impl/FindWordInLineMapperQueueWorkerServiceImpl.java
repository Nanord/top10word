package com.stm.top10word.service.worker.impl;

import com.stm.top10word.utils.BlockingQueueUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

@Component("findWordInLineWorker")
@Slf4j
public class FindWordInLineMapperQueueWorkerServiceImpl extends AbstractQueueWorkerService<String, String> {

    @Value("${delimiter.regExp}")
    private String delimiter;

    public FindWordInLineMapperQueueWorkerServiceImpl(ThreadPoolTaskExecutor threadPoolTaskExecutorWorker) {
        super(threadPoolTaskExecutorWorker);
    }

    @Override
    protected void run(String stopWord, String nextStopWord, BlockingQueue<String> inputQueue, BlockingQueue<String> outputQueue) {
        String input = BlockingQueueUtils.takeObjectFromQueue(inputQueue);
        while (input != null && !Objects.equals(stopWord, input)) {
            Stream<String> outputStream = work(input);
            outputStream.forEach(output -> BlockingQueueUtils.putObjectInQueue(output, outputQueue));
            input = BlockingQueueUtils.takeObjectFromQueue(inputQueue);
        }
        BlockingQueueUtils.putObjectInQueue(nextStopWord, outputQueue);
    }

    private Stream<String> work(String input) {
        if(StringUtils.isEmpty(input)) {
            return Stream.empty();
        }
        String[] split = input.split(delimiter);
        return Arrays.stream(split);
    }
}
