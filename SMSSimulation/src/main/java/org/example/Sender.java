package org.example;

import java.util.Random;


public class Sender implements Runnable {
    private final MessageQueue messageQueue;
    private final ProgressMonitor monitor;
    private final int meanProcessingTime;
    private final double failureRate;

    private final Random random;
    public Sender(MessageQueue messageQueue,  ProgressMonitor monitor, int meanProcessingTime, double failureRate) {
        this.messageQueue = messageQueue;
        this.monitor = monitor;
        this.meanProcessingTime = meanProcessingTime;
        this.failureRate = failureRate;
        this.random = new Random();
    }

    Message consumeMessage() throws InterruptedException {
        return messageQueue.consume();
    }


    Double calculateSleepTime() {

        // uniform distribution
        Double sleepTime = 2 * meanProcessingTime * random.nextDouble();
        return sleepTime;
    }


    void simulateMessageSend() throws InterruptedException {
        int sleepInteger = (int) Math.round(calculateSleepTime());
        Thread.sleep(sleepInteger);
        if (random.nextDouble() < failureRate) {
            monitor.incrementFailures();
        } else {
            monitor.incrementSuccess();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message message = consumeMessage();
                if (message != null) {
                    simulateMessageSend();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }



}
