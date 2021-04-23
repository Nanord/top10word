package com.stm.top10word.service.filters;

public interface FilterService {
    int getPriority();
    boolean check(String t);
}
