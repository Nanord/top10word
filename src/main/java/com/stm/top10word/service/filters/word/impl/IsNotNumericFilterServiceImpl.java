package com.stm.top10word.service.filters.word.impl;

import com.stm.top10word.service.filters.word.WordFilterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class IsNotNumericFilterServiceImpl implements WordFilterService {

    @Override
    public boolean check(String word) {
        return !StringUtils.isNumeric(word);
    }

    @Override
    public int getPriority() {
        return 1;
    }

}
