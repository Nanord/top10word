package com.stm.top10word.service.filters.word.impl;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IsNotNumericFilterServiceImplTest {

    private IsNotNumericFilterServiceImpl isNotNumericFilterService = new IsNotNumericFilterServiceImpl();

    @Test
    public void numbericCheckTest() {
        String v1 = "1";
        String v2 = "1.0";
        String v3 = "1,0";
        String v4 = "word";

        assertFalse(isNotNumericFilterService.check(v1));
        assertTrue(isNotNumericFilterService.check(v2));
        assertTrue(isNotNumericFilterService.check(v3));
        assertTrue(isNotNumericFilterService.check(v4));
    }

}
