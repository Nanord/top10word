package com.stm.top10word.command;

import com.stm.top10word.utils.MultithreadingUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PropertiesStarterCommand implements CommandLineRunner {

    int charSize = 10;
    String delimiter = "\\W+";
    BlockingQueue<String> queue = new LinkedBlockingDeque<>(10000);
    Map<String, AtomicInteger> map = new ConcurrentHashMap<>();

    public static String printExecutionTime(String desc, long start) {
        long end = System.currentTimeMillis() - start;
        long millis = end % 1000;
        long second = (end / 1000) % 60;
        long minute = (end / (1000 * 60)) % 60;
        long hour = (end / (1000 * 60 * 60)) % 24;
        String time = String.format("%02d:%02d:%02d.%d", hour, minute, second, millis);
        log.info("Execution time {}: {}", desc, time);
        return time;
    }

    public static <T> T takeObjectFromQueue(BlockingQueue<T> blockingQueue, T defaultValue) {
        try {
            T poll = blockingQueue.poll(10, TimeUnit.MINUTES);
            if (poll == null) {
                return defaultValue;
            }
            return poll;
        } catch (InterruptedException e) {
            return defaultValue;
        }
    }

    @Override
    public void run(String... args) throws Exception {
        Path path = Paths.get("C:\\awork\\dealer\\tmp\\");

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setCorePoolSize(8);
        threadPoolTaskExecutor.initialize();
        long t1 = System.currentTimeMillis();
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(this::start);
        CompletableFuture<List<Void>> collect1 = Files.list(path).map(f -> {
            return CompletableFuture
                    .supplyAsync(() -> {
                        long l = System.currentTimeMillis();
                        readFileIntoBuffer(f);
                        printExecutionTime("file:" + f.getFileName(), l);
                    }, threadPoolTaskExecutor)
                    .thenApplyAsync();
        }).collect(Collectors.collectingAndThen(Collectors.toList(), MultithreadingUtils.joinResult()));
        collect1.get();
        //voidCompletableFuture.get();
        printExecutionTime("common work file", t1);
        long t2 = System.currentTimeMillis();
        String collect = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparing(AtomicInteger::get, Comparator.reverseOrder())))
                .limit(10)
                .map(entry -> entry.getKey() + " : " + entry.getValue())
                .collect(Collectors.joining("\n"));
        printExecutionTime("find top 10", t2);
        log.info(collect);
    }

    private void start() {
        String s = MultithreadingUtils.takeObjectFromQueue(queue, null);
        while (StringUtils.isNotEmpty(s)) {
            //putWordToMap(s);
            s = MultithreadingUtils.takeObjectFromQueue(queue, null);
        }
    }

    private void putWordToMap(String s, Map<String, AtomicInteger> map) {
        //map.putIfAbsent(s, new AtomicInteger(1));
        map.compute(s, (key, value) -> {
            if (value == null) {
                return new AtomicInteger(1);
            }
            value.incrementAndGet();
            return value;
        });
    }

    @SneakyThrows
    private Map<String, AtomicInteger> readFileIntoBuffer(Path file) {
        HashMap<String, AtomicInteger> stringAtomicIntegerHashMap = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(file.toFile())) {
            FileChannel channel = fis.getChannel();
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            Scanner scanner = new Scanner(StandardCharsets.UTF_8.newDecoder().decode(map));
            scanner.useLocale(Locale.getDefault());
            scanner.useDelimiter(delimiter);
            while (scanner.hasNext()) {
                String next = scanner.next();
                if (StringUtils.isEmpty(next)) {
                    continue;
                }
                if (StringUtils.length(next) < charSize) {
                    continue;
                }
                putWordToMap(next, stringAtomicIntegerHashMap);
            }
            return stringAtomicIntegerHashMap;
        }
    }
}
