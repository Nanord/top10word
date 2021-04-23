package com.stm.top10word.service.filters.word.impl;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertEquals;


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

        assertEquals(false, isNotNumericFilterService.check(v1));
        assertEquals(true, isNotNumericFilterService.check(v2));
        assertEquals(true, isNotNumericFilterService.check(v3));
        assertEquals(true, isNotNumericFilterService.check(v4));
    }

}
