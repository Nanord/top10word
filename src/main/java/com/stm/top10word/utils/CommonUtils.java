package com.stm.top10word.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtils {
    public static void printExecutionTime(String desc, long end) {
        long millis = end % 1000;
        long second = (end / 1000) % 60;
        long minute = (end / (1000 * 60)) % 60;
        long hour = (end / (1000 * 60 * 60)) % 24;
        String time = String.format("%02d:%02d:%02d.%d", hour, minute, second, millis);
        log.info("Execution time {}: {}", desc, time);
    }
}
