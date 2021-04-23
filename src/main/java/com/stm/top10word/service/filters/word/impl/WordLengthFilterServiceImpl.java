package com.stm.top10word.service.filters.word.impl;

import com.stm.top10word.service.filters.word.WordFilterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WordLengthFilterServiceImpl implements WordFilterService {

    @Value("${word.length}")
    private Integer wordLength;

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public boolean check(String t) {
        return StringUtils.length(t) > wordLength;
    }
}
