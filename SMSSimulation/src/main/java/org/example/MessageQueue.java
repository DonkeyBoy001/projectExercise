package org.example;

import java.util.concurrent.LinkedBlockingQueue;

public class MessageQueue {
    private final LinkedBlockingQueue<Message> queue;

    public MessageQueue(int capacity) {
        this.queue = new LinkedBlockingQueue<>(capacity);
    }
    public void produce(Message message) throws InterruptedException {
        queue.put(message);
    }
    public Message consume() throws InterruptedException {
        return queue.take();
    }

    public int size(){
        return queue.size();
    }
}
