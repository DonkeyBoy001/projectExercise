package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageQueueTest {
    private MessageQueue messageQueue;
    private final static int CAPACITY = 1000;

    @BeforeEach
    void setUp() {
        messageQueue = new MessageQueue(CAPACITY);
    }

    @Test
    @DisplayName("Should successfully produce and consume a message")
    void testProduceAndConsume() throws InterruptedException {
        assertEquals(0, messageQueue.size(), "Queue should be empty initially");

        Message testMessage = new Message("0123456789", "Test Message");
        messageQueue.produce(testMessage);
        assertEquals(1, messageQueue.size(), "Queue should have 1 message after producing");

        Message consumedMessage = messageQueue.consume();
        assertEquals(testMessage, consumedMessage);
        assertEquals(0, messageQueue.size(), "Queue should be empty after consuming");
    }

    @Test
    @DisplayName("Should block when trying to produce beyond capacity")
    void testProduceBlocking() throws InterruptedException {
        for (int i = 0; i < CAPACITY; i++) {
            messageQueue.produce(new Message("0123456789", "Message" + i));
        }

        Thread thread = new Thread(() -> {
            try {
                messageQueue.produce(new Message("0123456789", "Blocked Message"));
                fail("Expected an InterruptedException to be thrown");
            } catch (InterruptedException e) {
            }
        });

        thread.start();
        Thread.sleep(500);
        thread.interrupt();

        thread.join();
    }
}