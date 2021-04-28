package com.stm.top10word.service.filters.word.impl;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class WordLengthFilterServiceImplTest{

    private WordLengthFilterServiceImpl wordLengthFilterService = new WordLengthFilterServiceImpl();

    @Test
    public void lenghtCheckTest() {

        String wordWith2SymbolsBad = "Hi";
        String wordWith5SymbolsGood = "Hello";
        String wordWith8SymbolsGood = "Longword";

        ReflectionTestUtils.setField(wordLengthFilterService, "wordLength", 5);

        assertFalse(wordLengthFilterService.check(wordWith2SymbolsBad));
        assertFalse(wordLengthFilterService.check(wordWith5SymbolsGood));
        assertTrue(wordLengthFilterService.check(wordWith8SymbolsGood));
    }


}
