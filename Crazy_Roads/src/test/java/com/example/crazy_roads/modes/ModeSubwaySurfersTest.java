package com.example.crazy_roads.modes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ModeSubwaySurfersTest {
    private ModeSubwaySurfers mode;

    @BeforeEach
    void setUp() {
        mode = new ModeSubwaySurfers();
    }

    @Test
    void testInitialState() {
        assertFalse(mode.isGameOver(), "Game should not be over initially");
        assertEquals(0, mode.getScore(), "Initial score should be 0");
        assertNotNull(mode.getCar(), "Car should be initialized");
        assertEquals(3, mode.getCar().getLife(), "Car should have 3 lives initially");
    }

    @Test
    void testScoreIncreaseOverTime() throws InterruptedException {
        mode.start();
        int initialScore = mode.getScore();
        
        Thread.sleep(1100);
        mode.update();
        
        assertTrue(mode.getScore() > initialScore, "Score should increase over time");
    }

    @Test
    void testScoreBasedOnTime() throws InterruptedException {
        mode.start();
        
        Thread.sleep(2100);
        mode.update();
        
        assertTrue(mode.getScore() >= 2, "Score should be at least 2 after 2 seconds");
    }

    @Test
    void testCarLife() {
        assertEquals(3, mode.getCar().getLife(), "Car should start with 3 lives");
        mode.getCar().collide();
        assertTrue(mode.getCar().getLife() < 3, "Car should lose lives on collision");
    }
    
    @Test
    void testCarReference() {
        assertNotNull(mode.getCar(), "Car should not be null");
        assertSame(mode.getCar(), mode.getCar(), "getCar() should return the same instance");
    }
}
