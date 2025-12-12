package com.example.crazy_roads.modes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ModeMarioKartTest {
    private ModeMarioKart mode;

    @BeforeEach
    void setUp() {
        mode = new ModeMarioKart();
    }

    @Test
    void testInitialState() {
        assertFalse(mode.isRaceStarted(), "Race should not be started initially");
        assertFalse(mode.isRaceFinished(), "Race should not be finished initially");
        assertFalse(mode.isGameOver(), "Game should not be over initially");
        assertEquals(0, mode.getCurrentFrame(), "Initial frame should be 0");
        assertEquals(60, mode.getTotalFrames(), "Total frames should be 60");
        assertEquals(3, mode.getLives(), "Initial lives should be 3");
    }

    @Test
    void testStartRace() {
        mode.start();
        assertTrue(mode.isRaceStarted(), "Race should be started after calling start()");
        assertEquals(0, mode.getRaceTime(), "Race time should be 0 at start");
    }

    @Test
    void testIncrementFrame() {
        mode.incrementFrame();
        assertEquals(1, mode.getCurrentFrame(), "Frame should be 1 after increment");
        
        for (int i = 1; i < 60; i++) {
            mode.incrementFrame();
        }
        
        assertEquals(60, mode.getCurrentFrame(), "Frame should be 60 after 60 increments");
        assertTrue(mode.isRaceFinished(), "Race should be finished at frame 60");
    }

    @Test
    void testBoostActivation() {
        mode.start();
        assertFalse(mode.isBoostActive(), "Boost should not be active initially");
        
        mode.activateBoost();
        assertTrue(mode.isBoostActive(), "Boost should be active after activation");
    }

    @Test
    void testSpeedIncrease() {
        mode.start();
        double initialSpeed = mode.getSpeed();
        
        mode.activateBoost();
        assertTrue(mode.getSpeed() >= initialSpeed, "Speed should not decrease with boost");
    }

    @Test
    void testCarReference() {
        assertNotNull(mode.getCar(), "Car should not be null");
        assertSame(mode.getCar(), mode.getCar(), "getCar() should return the same instance");
    }

    @Test
    void testRaceTimeProgression() throws InterruptedException {
        mode.start();
        long initialTime = mode.getRaceTime();
        
        Thread.sleep(1100);
        mode.update();
        
        assertTrue(mode.getRaceTime() > initialTime, "Race time should increase after update");
    }
    
    @Test
    void testFrameProgression() {
        int initialFrame = mode.getCurrentFrame();
        mode.incrementFrame();
        assertEquals(initialFrame + 1, mode.getCurrentFrame(), "Frame should increment by 1");
    }
    
    @Test
    void testRaceNotFinishedEarly() {
        for (int i = 0; i < 30; i++) {
            mode.incrementFrame();
        }
        assertFalse(mode.isRaceFinished(), "Race should not be finished at frame 30");
    }
    
    @Test
    void testGetLane() {
        int lane = mode.getLane();
        assertTrue(lane >= 0 && lane <= 2, "Lane should be between 0 and 2");
    }
}
