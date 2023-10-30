package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class App {
    public static void main( String[] args ) {
        int numOfMessages = 100;
        int numOfSenders = 5;
        int meanProcessingTime = 200; // ms
        double failureRate = 0.1;
        int updateInterval = 10; // s
        int capacity = 1000;

        MessageQueue messageQueue = new MessageQueue(capacity); // default 1000

        Producer producer = new Producer(messageQueue, numOfMessages);
        Thread producerThread = new Thread(producer);
        producerThread.start();

        ProgressMonitor monitor = new ProgressMonitor(updateInterval);

        ExecutorService senderExecutor = Executors.newFixedThreadPool(numOfSenders);
        for (int i = 0; i < numOfSenders; i++) {
            senderExecutor.execute(new Sender(messageQueue, monitor, meanProcessingTime, failureRate));
        }

        ScheduledExecutorService progressMonitorExecutor = Executors.newSingleThreadScheduledExecutor();
        progressMonitorExecutor.scheduleAtFixedRate(monitor, 0, updateInterval, TimeUnit.MILLISECONDS);

        ScheduledExecutorService shutdownExecutor = Executors.newSingleThreadScheduledExecutor();

        shutdownExecutor.scheduleAtFixedRate(() -> {
            if (monitor.allMessagesProcessed(numOfMessages)) {
                shutdownExecutor.shutdown();
                try {
                    if (!shutdownExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                        shutdownExecutor.shutdownNow();
                        if (!shutdownExecutor.awaitTermination(30, TimeUnit.SECONDS))
                            System.err.println("ScheduledExecutorService did not terminate");
                    }
                } catch (InterruptedException ie) {
                    shutdownExecutor.shutdownNow();
                    Thread.currentThread().interrupt();
                }
                System.exit(0);
            }
        }, updateInterval, updateInterval, TimeUnit.MILLISECONDS);
    }
}
