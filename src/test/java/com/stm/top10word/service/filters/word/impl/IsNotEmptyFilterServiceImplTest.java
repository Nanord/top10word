package com.stm.top10word.service.filters.word.impl;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IsNotEmptyFilterServiceImplTest {

    private IsNotEmptyFilterServiceImpl isNotEmptyFilterService = new IsNotEmptyFilterServiceImpl();

    @Test
    public void numbericCheckTest() {
        String v1 = "";
        String v2 = "word";

        assertEquals(false, isNotEmptyFilterService.check(v1));
        assertEquals(true, isNotEmptyFilterService.check(v2));
    }

}

