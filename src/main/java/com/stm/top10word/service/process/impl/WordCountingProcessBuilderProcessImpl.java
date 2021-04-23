package com.stm.top10word.service.process.impl;

import com.stm.top10word.service.process.BuilderProcessService;
import com.stm.top10word.service.process.StarterProcessService;
import com.stm.top10word.service.process.TerminalProcessService;
import com.stm.top10word.service.worker.QueueWorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Service("wordCountingProcessBuilder")
@Slf4j
@RequiredArgsConstructor
public class WordCountingProcessBuilderProcessImpl implements BuilderProcessService<Path, CompletableFuture<Map<String, Integer>>> {

    @Qualifier("fileReaderStarterProcess")
    private final StarterProcessService<Path, String> starterProcessService;
    @Qualifier("wordCountingTerminalProcess")
    private final TerminalProcessService<String, Map<String, Integer>> terminatedProcessService;
    @Qualifier("findWordInLineWorker")
    private final QueueWorkerService<String, String> findWordInLineWorker;
    @Qualifier("filterWordWorker")
    private final QueueWorkerService<String, String> filterWordWorker;

    @Override
    public CompletableFuture<Map<String, Integer>> build(Path path) {
        String stopWord = UUID.randomUUID().toString();
        return Optional.ofNullable(path)
                .map(file -> starterProcessService.start(path, stopWord))
                .map(line -> findWordInLineWorker.doWork(stopWord, stopWord, line))
                .map(word -> filterWordWorker.doWork(stopWord, stopWord, word))
                .map(word -> terminatedProcessService.start(stopWord, word))
                .orElse(null);
    }
}
