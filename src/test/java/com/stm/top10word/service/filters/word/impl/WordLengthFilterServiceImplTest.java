package com.stm.top10word.service.filters.word.impl;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;


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

        assertEquals(false, wordLengthFilterService.check(wordWith2SymbolsBad));
        assertEquals(false, wordLengthFilterService.check(wordWith5SymbolsGood));
        assertEquals(true, wordLengthFilterService.check(wordWith8SymbolsGood));
    }


}
