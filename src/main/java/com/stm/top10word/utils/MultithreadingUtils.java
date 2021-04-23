package com.stm.top10word.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class MultithreadingUtils {

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            log.error("Thread is interrupted", e);
        }
    }

    public static <T> T handleException(String text, String from, T defaultValue, Throwable throwable) {
        log.warn("Text: {}; From: {}", text, from, throwable);
        return defaultValue;
    }

    public static Void handleException(String text, String from, Throwable throwable) {
        log.warn("Text: {}; From: {}", text, from, throwable);
        return null;
    }

    public static <X, T extends CompletableFuture<X>> Function<List<T>, CompletableFuture<List<X>>> joinResult() {
        return ls -> allOf(ls)
                .thenApply(v -> ls
                        .stream()
                        .map(CompletableFuture::join)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
    }

    private static <T extends CompletableFuture<?>> CompletableFuture<Void> allOf(List<T> ls) {
        return CompletableFuture.allOf(ls.toArray(new CompletableFuture[0]));
    }

    public static <T> T getObjectFromAsyncTask(CompletableFuture<T> future) {
        try {
            return future.get();
        } catch (InterruptedException e) {
            log.warn("Thread is interrupted", e);
        } catch (ExecutionException e) {
            log.warn("Error during processing task", e);
        }
        return null;
    }

    public static <T> void awaitCompletableFuture(CompletableFuture<T> future) {
        try {
            future.get();
        } catch (InterruptedException e) {
            log.warn("Thread is interrupted", e);
        } catch (ExecutionException e) {
            log.warn("Error during processing task", e);
        }
    }


}
