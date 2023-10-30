package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


import javax.print.attribute.standard.RequestingUserName;
import java.util.Random;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class SenderTest {
    private MessageQueue messageQueue;
    private Sender sender;
    @Mock
    private ProgressMonitor monitor;

    private int MEAN_PROCESS_TIME = 200;

    @BeforeEach
    void setUp() {
        monitor = mock(ProgressMonitor.class);
        messageQueue = mock(MessageQueue.class);
    }

    @Test
    public void testConsumeMessage() throws InterruptedException {
        Message mockMessage = new Message("1234567890", "Test Content");
        when(messageQueue.consume()).thenReturn(mockMessage);
        sender = new Sender(messageQueue, monitor, MEAN_PROCESS_TIME, 0.1);
        Message message = sender.consumeMessage();

        assertEquals(mockMessage, message);
    }


    @Test
    public void testCalculateSleepTime() {
        sender = new Sender(messageQueue, monitor, MEAN_PROCESS_TIME, 0.1);
        double sleepTime = sender.calculateSleepTime();
        assertTrue(sleepTime>= 0 && sleepTime <= 2 * MEAN_PROCESS_TIME,
                "Sleep time should be between 0 and twice the mean processing time.");
    }

    @Test
    public void testSimulateMessageSendSuccess() throws InterruptedException {
        Sender successfulSender = new Sender(messageQueue, monitor, MEAN_PROCESS_TIME, 0.0);

        successfulSender.simulateMessageSend();
        verify(monitor, times(1)).incrementSuccess();
        verify(monitor, never()).incrementFailures();
    }


    @Test
    public void testSimulateMessageSendFailure() throws InterruptedException {
        Sender failureSender = new Sender(messageQueue, monitor, MEAN_PROCESS_TIME, 1.0);

        failureSender.simulateMessageSend();

        verify(monitor, times(1)).incrementFailures();
        verify(monitor, never()).incrementSuccess();
    }
}