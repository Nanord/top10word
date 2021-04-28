package com.stm.top10word.service.process.impl;

import com.stm.top10word.exception.ProcessStarterException;
import com.stm.top10word.service.process.StarterProcessService;
import com.stm.top10word.utils.BlockingQueueUtils;
import com.stm.top10word.utils.MultithreadingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Service("fileReaderStarterProcess")
@RequiredArgsConstructor
@Slf4j
public class FileReaderStarterProcessService implements StarterProcessService<Path, String> {

    @Qualifier("threadPoolTaskExecutorStarter")
    private final ThreadPoolTaskExecutor threadPoolTaskExecutorStarter;

    @Value("${starter.queue.size:100000}")
    private Integer queueSize;

    @Override
    public BlockingQueue<String> start(Path path, String stopWord) {
        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(queueSize);
        CompletableFuture.runAsync(() -> startReadFile(path, stopWord, blockingQueue), threadPoolTaskExecutorStarter)
                .exceptionally(ex -> MultithreadingUtils.handleException("Error during read file: " + path.getFileName(),
                        this.getClass().getName(),
                        ex));
        return blockingQueue;
    }

    private void startReadFile(Path path, String stopWord, BlockingQueue<String> blockingQueue) {
        log.info("Start Read file: {}", path.getFileName());
        AtomicInteger countLine = new AtomicInteger(0);
        try (LineIterator lineIterator = FileUtils.lineIterator(path.toFile())) {
            lineIterator.forEachRemaining(line -> {
                countLine.incrementAndGet();
                BlockingQueueUtils.putObjectInQueue(line, blockingQueue);
            });
        } catch (IOException e) {
            throw new ProcessStarterException(e);
        } finally {
            BlockingQueueUtils.putObjectInQueue(stopWord, blockingQueue);
        }
    }

}
