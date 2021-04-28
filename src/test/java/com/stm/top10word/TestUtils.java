package com.stm.top10word;

import com.stm.top10word.utils.BlockingQueueUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

public class TestUtils {
    public static <O, BQ extends BlockingQueue<O>> void queueWatcher(Map<O, Integer> result, BQ queue, O stopObject) {
        O object = BlockingQueueUtils.takeObjectFromQueue(queue);
        while (!Objects.equals(stopObject, object)) {
            putObjectToMap(object, result);
            object = BlockingQueueUtils.takeObjectFromQueue(queue);
        }
    }

    private static <O> void putObjectToMap(O s, Map<O, Integer> map) {
        map.compute(s, (key, value) -> Optional
                .ofNullable(value)
                .map(v -> v + 1)
                .orElse(1));
    }
}
