package org.example;

public class ProgressMonitor implements Runnable{
    private int successes = 0;
    private int failures = 0;
    private final int updateInterval;

    private long startTime; // ms

    private int freqOfIntervals;
    public ProgressMonitor(int updateInterval) { // s
        this.updateInterval = updateInterval;
        this.freqOfIntervals = 0;
        this.startTime = System.currentTimeMillis();
    }

    public int getSuccesses() {
        return successes;
    }

    public int getFailures() {
        return failures;
    }

    public synchronized void incrementSuccess() {
        successes++;
    }
    public synchronized void incrementFailures() {
        failures++;
    }

    public synchronized void displayProgress() throws InterruptedException {
        long currentTime = System.currentTimeMillis();
        double averageTime = (double) (currentTime - startTime -  freqOfIntervals * updateInterval * 1000) / (successes + failures);


        System.out.println("-".repeat(30));
        System.out.println("Number of messages sent so far: " + successes);
        System.out.println("Number of messages failed so far: " + failures);
        System.out.println(String.format("Average time per message so far: %.2f s" , averageTime/1000));
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(updateInterval * 1000); // s->ms
                synchronized (this) {
                    displayProgress();
                }
                freqOfIntervals++;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized boolean allMessagesProcessed(int totalMessages) {
        return (this.successes + this.failures) >= totalMessages;
    }
}
