package com.stm.top10word.service.filters.word.impl;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IsNotEmptyFilterServiceImplTest {

    private IsNotEmptyFilterServiceImpl isNotEmptyFilterService = new IsNotEmptyFilterServiceImpl();

    @Test
    public void numbericCheckTest() {
        String v1 = "";
        String v2 = "word";

        assertFalse(isNotEmptyFilterService.check(v1));
        assertTrue(isNotEmptyFilterService.check(v2));
    }

}

