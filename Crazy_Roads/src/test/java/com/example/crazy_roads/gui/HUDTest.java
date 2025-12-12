package com.example.crazy_roads.gui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import javafx.application.Platform;

class HUDTest {

    @BeforeAll
    static void initToolkit() {
        // Initialize JavaFX toolkit
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit already initialized
        }
    }

    @Test
    void testHUDScoreOperations() {
        Platform.runLater(() -> {
            HUD hud = new HUD();
            
            assertEquals(0, hud.getCurrentScore(), "Initial score should be 0");
            
            hud.addScore(10);
            assertEquals(10, hud.getCurrentScore(), "Score should be 10 after adding 10");
            
            hud.addScore(5);
            assertEquals(15, hud.getCurrentScore(), "Score should be 15 after adding 5 more");
            
            hud.setScore(100);
            assertEquals(100, hud.getCurrentScore(), "Score should be 100 after setting");
            
            hud.stop();
        });
    }

    @Test
    void testHUDLivesOperations() {
        Platform.runLater(() -> {
            HUD hud = new HUD();
            
            assertEquals(3, hud.getLives(), "Initial lives should be 3");
            
            hud.setLives(2);
            assertEquals(2, hud.getLives(), "Lives should be 2 after setting");
            
            hud.setLives(0);
            assertEquals(0, hud.getLives(), "Lives should be 0 after setting");
            
            hud.stop();
        });
    }

    @Test
    void testHUDBoostOperations() {
        Platform.runLater(() -> {
            HUD hud = new HUD();
            
            assertFalse(hud.isBoostActive(), "Boost should be inactive initially");
            
            hud.setBoostActive(true);
            assertTrue(hud.isBoostActive(), "Boost should be active after setting");
            
            hud.setBoostActive(false);
            assertFalse(hud.isBoostActive(), "Boost should be inactive after setting");
            
            hud.stop();
        });
    }

    @Test
    void testHUDBestScoreOperations() {
        Platform.runLater(() -> {
            HUD hud = new HUD();
            
            assertTrue(hud.getBestScore() >= 0, "Best score should be non-negative");
            
            hud.setBestScore(50);
            assertEquals(50, hud.getBestScore(), "Best score should be 50 after setting");
            
            hud.stop();
        });
    }

    @Test
    void testHUDGameModeOperations() {
        Platform.runLater(() -> {
            HUD hud = new HUD();
            
            assertNotNull(hud.getCurrentGameModeKey(), "Game mode key should not be null");
            
            hud.setInfiniteMode();
            assertEquals("COURSE_INFINIE", hud.getCurrentGameModeKey(), 
                        "Game mode should be COURSE_INFINIE");
            
            hud.setTimeAttackMode();
            assertEquals("CONTRE_LA_MONTRE", hud.getCurrentGameModeKey(), 
                        "Game mode should be CONTRE_LA_MONTRE");
            
            hud.stop();
        });
    }

    @Test
    void testHUDTimerOperations() {
        Platform.runLater(() -> {
            HUD hud = new HUD();
            
            hud.resetTimer();
            
            try {
                Thread.sleep(1100);
            } catch (InterruptedException e) {
                fail("Thread interrupted");
            }
            
            long elapsed = hud.getElapsedTimeSeconds();
            assertTrue(elapsed >= 1, "At least 1 second should have elapsed");
            
            hud.stop();
        });
    }

    @Test
    void testHUDNewRecordDetection() {
        Platform.runLater(() -> {
            HUD hud = new HUD();
            
            hud.setBestScore(10);
            hud.setScore(20);
            
            assertTrue(hud.isNewRecord(), "Should be new record when score > best score");
            
            hud.setScore(5);
            assertFalse(hud.isNewRecord(), "Should not be new record when score < best score");
            
            hud.stop();
        });
    }

    @Test
    void testHUDRootNotNull() {
        Platform.runLater(() -> {
            HUD hud = new HUD();
            assertNotNull(hud.getHUDRoot(), "HUD root should not be null");
            hud.stop();
        });
    }

    @Test
    void testHUDCountdownMode() {
        Platform.runLater(() -> {
            HUD hud = new HUD();
            
            hud.setCountdownMode();
            assertEquals("CONTRE_LA_MONTRE", hud.getCurrentGameModeKey(), 
                        "Countdown mode should set CONTRE_LA_MONTRE key");
            
            hud.stop();
        });
    }

    @Test
    void testHUDStopDoesNotThrow() {
        Platform.runLater(() -> {
            HUD hud = new HUD();
            assertDoesNotThrow(() -> hud.stop(), "Stopping HUD should not throw exception");
        });
    }

    @Test
    void testHUDMultipleScoreAdditions() {
        Platform.runLater(() -> {
            HUD hud = new HUD();
            
            for (int i = 0; i < 10; i++) {
                hud.addScore(1);
            }
            
            assertEquals(10, hud.getCurrentScore(), "Score should be 10 after 10 additions");
            
            hud.stop();
        });
    }

    @Test
    void testHUDSetTime() {
        Platform.runLater(() -> {
            HUD hud = new HUD();
            
            assertDoesNotThrow(() -> hud.setTime(120), 
                              "Setting time should not throw exception");
            
            hud.stop();
        });
    }
}
