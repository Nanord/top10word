package com.stm.top10word.command;

import com.stm.top10word.service.process.ProcessRunnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PropertiesStarterCommand implements CommandLineRunner {

    private final ProcessRunnerService<String, List<Pair<String, Integer>>> processRunnerService;
    private final ConfigurableApplicationContext configurableApplicationContext;
    @Value("${folder.path}")
    private String folderPath;

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0) {
            folderPath = args[0];
        }
        List<Pair<String, Integer>> top10Words = processRunnerService.startProcess(folderPath);
        String top10 = top10Words.stream()
                .sorted(Comparator.comparing(Pair::getValue, Comparator.reverseOrder()))
                .map(entry -> "\t" + entry.getKey() + " : " + entry.getValue())
                .collect(Collectors.joining("\n"));
        log.info("\n--------------------------------------------\n{}\n------------------------------------------", top10);
        configurableApplicationContext.close();
    }
}
