package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProgressMonitorTest {

    private ProgressMonitor progressMonitor;

    @BeforeEach
    public void setUp() {
        progressMonitor = new ProgressMonitor(1); // Using 1 second interval for simplicity
    }

    @Test
    public void testIncrementSuccess() {
        progressMonitor.incrementSuccess();
        progressMonitor.incrementSuccess();
        progressMonitor.incrementSuccess();

        assertEquals(3, progressMonitor.getSuccesses());
    }

    @Test
    public void testIncrementFailures() {
        progressMonitor.incrementFailures();
        progressMonitor.incrementFailures();

        assertEquals(2, progressMonitor.getFailures());
    }

    @Test
    public void testAllMessagesProcessed() {
        for (int i = 0; i < 50; i++) {
            progressMonitor.incrementSuccess();
        }
        for (int i = 0; i < 50; i++) {
            progressMonitor.incrementFailures();
        }

        assertTrue(progressMonitor.allMessagesProcessed(100));
        assertFalse(progressMonitor.allMessagesProcessed(101));
    }

}
