package com.stm.top10word.service.filters.word.impl;

import com.stm.top10word.service.filters.word.WordFilterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class IsNotEmptyFilterServiceImpl implements WordFilterService {

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean check(String word) {
        return StringUtils.isNotEmpty(word);
    }
}
