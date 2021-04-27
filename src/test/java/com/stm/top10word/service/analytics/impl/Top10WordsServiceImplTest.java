package com.stm.top10word.service.analytics.impl;


import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.Silent.class)
public class Top10WordsServiceImplTest {

    Top10WordsServiceImpl top10WordsService;

    Map<String, Integer> map1;
    Map<String, Integer> map2;
    Map<String, Integer> map3;

    @Before
    public void setUp() {
        top10WordsService = new Top10WordsServiceImpl();

        map1 = new HashMap<String, Integer>() {{
            put("Did", 5);
            put("I", 4);
            put("Ever", 6);
            put("Tell", 3);
            put("You", 2);
            put("What", 6);
            put("The", 8);
            put("Definition", 1);
            put("Of", 7);
            put("Insanity", 3);
            put("Is?", 4);
        }};

        map2 = new HashMap<String, Integer>() {{
            put("Did", 1);
            put("I", 1);
            put("Ever", 1);
            put("Tell", 1);
            put("You", 1);
            put("What", 1);
            put("The", 1);
            put("Definition", 1);
            put("Of", 1);
            put("Insanity", 1);
            put("Is?", 1);
        }};

        map3 = new HashMap<String, Integer>() {{
            put("Did", 2);
            put("I", 2);
            put("Ever", 2);
            put("Tell", 2);
            put("You", 2);
            put("What", 2);
            put("The", 2);
            put("Definition", 2);
            put("Of", 2);
            put("Insanity", 2);
            put("Is?", 2);
            put("Vaas", 4);
        }};
        top10WordsService.combineWordCountMap(map1);
    }

    @Test
    public void resetWordCountMap() {

        Assert.assertEquals(11, top10WordsService.getWordCountMapSize());

        top10WordsService.resetWordCountMap();

        Assert.assertEquals(0, top10WordsService.getWordCountMapSize());

    }

    @Test
    public void combineWordCountMap() {
        Assert.assertEquals(11, top10WordsService.getWordCountMapSize());

        top10WordsService.combineWordCountMap(map3);

        Assert.assertEquals(12, top10WordsService.getWordCountMapSize());
    }

    @Test
    public void receiveTop10Words() {
        top10WordsService.combineWordCountMap(map2);
        top10WordsService.combineWordCountMap(map3);

        List top10 = top10WordsService.receiveTop10Words();

        List<Pair<String, Integer>> expected = new ArrayList<Pair<String, Integer>>(){{
            add(Pair.of("You", 5));
            add(Pair.of("Insanity", 6));
            add(Pair.of("Tell", 6));
            add(Pair.of("I", 7));
            add(Pair.of("Is?", 7));
            add(Pair.of("What", 9));
            add(Pair.of("Did", 8));
            add(Pair.of("The", 11));
            add(Pair.of("Of", 10));
            add(Pair.of("Ever", 9));
        }};
        Assert.assertTrue(top10.equals(expected));
    }
}