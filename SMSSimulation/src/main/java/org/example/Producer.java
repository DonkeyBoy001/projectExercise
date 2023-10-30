package org.example;

import java.util.Random;

public class Producer implements Runnable {

    private final MessageQueue queue;
    private final int numOfMessages;
    public static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public Producer(MessageQueue queue, int numMessages) {
        this.queue = queue;
        this.numOfMessages = numMessages;
    }

    public Message produce() {
        return new Message(generatePhoneNumber(), generateContent());
    }

    private String generatePhoneNumber() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    private String generateContent() {
        Random rand = new Random();
        int len = rand.nextInt(100);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; i++) {
            sb.append(CHARACTERS.charAt(rand.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    @Override
    public void run() {
        Random rand = new Random();
        try {
            for (int i = 0; i < numOfMessages; i++) {
                queue.produce(produce());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

}

