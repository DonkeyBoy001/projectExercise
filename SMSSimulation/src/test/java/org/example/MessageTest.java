package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    private static final String TEST_PHONE_NUMBER = "1234567890";
    private static final String TEST_CONTENT = "Hello, World!";
    private Message message;

    @BeforeEach
    public void setUp() {
        message = new Message(TEST_PHONE_NUMBER, TEST_CONTENT);
    }

    @Test
    @DisplayName("Should return the correct phone number when getPhoneNumber is called")
    void testGetPhoneNumber() {
        assertEquals(TEST_PHONE_NUMBER, message.getPhoneNumber());
    }

    @Test
    @DisplayName("Should return the correct content when getContent is called")
    void testGetContent() {
        assertEquals(TEST_CONTENT, message.getContent());
    }
}