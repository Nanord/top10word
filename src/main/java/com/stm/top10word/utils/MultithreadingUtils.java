package com.stm.top10word.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Класс со статичисткими методами работы с CompletableFuture
 *
 * @author STMLabs.
 */
@Slf4j
public class MultithreadingUtils {

    /**
     * Обработка ошибки при использовании CompletableFuture.supplyAsync()
     * @param text  - Текст ошибки
     * @param from - Откуда произошла ошибка
     * @param defaultValue - Возвращаемое значение по умолчанию
     * @param throwable - Ошибка
     * @param <T> - Тип ошибки
     * @return - Возвращаемое значение по умолчанию
     */
    public static <T> T handleException(String text, String from, T defaultValue, Throwable throwable) {
        log.warn("Text: {}; From: {}", text, from, throwable);
        return defaultValue;
    }

    /**
     * Обработка ошибки при использовании CompletableFuture.runAsync()
     * @param text  - Текст ошибки
     * @param from - Откуда произошла ошибка
     * @param throwable - Ошибка
     * @return - Возвращаемое значение по умолчанию
     */
    public static Void handleException(String text, String from, Throwable throwable) {
        log.warn("Text: {}; From: {}", text, from, throwable);
        return null;
    }

    /**
     * Объединяет результаты List< CompletableFuture > в CompletableFuture< List >
     * @return CompletableFuture< List >
     */
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

    /**
     * Ожидает выполнения CompletableFuture< Void >
     *
     * @param future CompletableFuture
     * @param <T> тип объекта в CompletableFuture
     */
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
