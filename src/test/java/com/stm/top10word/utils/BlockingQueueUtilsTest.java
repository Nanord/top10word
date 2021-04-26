package com.stm.top10word.utils;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BlockingQueueUtilsTest {

    private BlockingQueue<Object> blockingQueue = new LinkedBlockingQueue<>();

    @Test
    public void putObjectInQueueTest() {
        assertEquals(true, BlockingQueueUtils.putObjectInQueue("firstWord", blockingQueue));
        assertEquals(true, BlockingQueueUtils.putObjectInQueue("secondWord", blockingQueue));
    }


    @Test
    public void takeObjectFromQueueTest() {

    }

}
