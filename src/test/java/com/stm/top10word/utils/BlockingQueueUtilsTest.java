package com.stm.top10word.utils;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BlockingQueueUtilsTest {

    private BlockingQueue<Object> blockingQueue = new LinkedBlockingQueue<>();

    private final String testObjectWord1 = "fWord1";
    private final String testObjectWord2 = "fWord2";

    @Test
    public void putObjectInQueueTest() {
        assertTrue(BlockingQueueUtils.putObjectInQueue("firstWord", blockingQueue));
        assertTrue(BlockingQueueUtils.putObjectInQueue("secondWord", blockingQueue, 3, TimeUnit.SECONDS));
    }

    @Test
    public void takeObjectFromQueueTest() throws InterruptedException {
        BlockingQueueUtils.putObjectInQueue(testObjectWord1, blockingQueue);
        BlockingQueueUtils.putObjectInQueue(testObjectWord2, blockingQueue, 6, TimeUnit.SECONDS);

        assertEquals(testObjectWord1, BlockingQueueUtils.takeObjectFromQueue(blockingQueue));
        assertEquals(testObjectWord2, BlockingQueueUtils.takeObjectFromQueue(blockingQueue, 3, TimeUnit.SECONDS));
    }

}
