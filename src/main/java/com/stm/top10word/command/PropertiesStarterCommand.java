package com.stm.top10word.command;

import com.stm.top10word.utils.MultithreadingUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
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
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PropertiesStarterCommand implements CommandLineRunner {

    int charSize = 5;
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
        Path path = Paths.get("/home/maxim/locindex/prj/_test/_files/");

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setCorePoolSize(4);
        threadPoolTaskExecutor.initialize();
        long t1 = System.currentTimeMillis();
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(this::start);
        CompletableFuture<List<Map<String, AtomicInteger>>> collect1 = Files.list(path).map(f -> {
            return CompletableFuture
                    .supplyAsync(() -> {
                        long l = System.currentTimeMillis();
                        var temp = readFileIntoBuffer(f);
                        printExecutionTime("file:" + f.getFileName(), l);
                        return temp;
                    }, threadPoolTaskExecutor);
                    //.thenApplyAsync();
        }).collect(Collectors.collectingAndThen(Collectors.toList(), MultithreadingUtils.joinResult()));
        var res1 = collect1.get();
        //collect1.get();
        //voidCompletableFuture.get();
        printExecutionTime("common work file", t1);
        long t2 = System.currentTimeMillis();
/*        String collect = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparing(AtomicInteger::get, Comparator.reverseOrder())))
                .limit(10)
                .map(entry -> entry.getKey() + " : " + entry.getValue())
                .collect(Collectors.joining("\n"));*/

        Map<String, AtomicInteger> map2 = new HashMap<>();
        String keyName = "";
        for (Map<String, AtomicInteger> stringAtomicIntegerMap : res1) {
            for (Map.Entry<String, AtomicInteger> stringAtomicIntegerEntry : stringAtomicIntegerMap.entrySet()) {
                keyName = stringAtomicIntegerEntry.getKey();
                if(map2.containsKey(keyName)) {
                    map2.get(keyName).set( map2.get(keyName).get() + stringAtomicIntegerEntry.getValue().get());
                }
                else {
                    map2.put(keyName, stringAtomicIntegerEntry.getValue());
                }
            }
        }


        String collect = map2.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparing(AtomicInteger::get, Comparator.reverseOrder())))
                .limit(10)
                .map(entry -> entry.getKey() + " : " + entry.getValue())
                .collect(Collectors.joining("\n"));



        printExecutionTime("find top 10", t2);
        printExecutionTime("all work time", t1);
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
