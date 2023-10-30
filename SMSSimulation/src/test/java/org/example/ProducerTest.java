package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProducerTest {

    private MessageQueue queue;
    private Producer producer;
    private static final int TEST_CAPACITY = 1000;

    @BeforeEach
    public void setup() {
        queue = new MessageQueue(TEST_CAPACITY);
        producer = new Producer(queue, TEST_CAPACITY);
    }

    @Test
    @DisplayName("Test if Producer can produce to the MessageQueue")
    public void testProducerCanProduce() throws InterruptedException {
        Thread producerThread = new Thread(producer);
        producerThread.start();
        producerThread.join();

        assertEquals(TEST_CAPACITY, queue.size(), "Producer should have produced the default number of messages");
    }

    @Test
    @DisplayName("Test if produced message's phone number format is correct")
    public void testPhoneNumberFormat() {
        Message message = producer.produce();
        String phoneNumber = message.getPhoneNumber();

        assertEquals(10, phoneNumber.length());
        assertTrue(phoneNumber.matches("[0-9]{10}"));
    }

    @Test
    @DisplayName("Test if produced message's content format is correct")
    public void testContentFormat() {
        Message message = producer.produce();
        String content = message.getContent();

        assertTrue(content.length() <= 100);
        assertTrue(content.matches("[" + Producer.CHARACTERS + "]*"));
    }

}