package com.example.crazy_roads.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CarTest {
    private Car Car;

    @BeforeEach
    void setUp() {
        Car = new Car();
    }

    @Test
    void testInitialState() {
        assertEquals(1, Car.getLane(), "Initial lane should be 1 (middle)");
        assertEquals(3, Car.getLife(), "Initial life should be 3");
        assertEquals(1.0, Car.getSpeed(), "Initial speed should be 1.0");
        assertTrue(Car.isAlive(), "Car should be alive initially");
        assertFalse(Car.isBoostActive(), "Boost should be inactive initially");
    }

    @Test
    void testMoveLeft() {
        Car.moveLeft();
        assertEquals(0, Car.getLane(), "Lane should be 0 after moving left");
        
        Car.moveLeft();
        assertEquals(0, Car.getLane(), "Lane should stay 0 (can't go below 0)");
    }

    @Test
    void testMoveRight() {
        Car.moveRight();
        assertEquals(2, Car.getLane(), "Lane should be 2 after moving right");
        
        Car.moveRight();
        assertEquals(2, Car.getLane(), "Lane should stay 2 (can't go above 2)");
    }

    @Test
    void testCollide() {
        int initialLife = Car.getLife();
        Car.collide();
        
        assertEquals(initialLife - 1, Car.getLife(), "Life should decrease by 1");
        assertTrue(Car.isCollide(), "Car should be in collision state");
    }

    @Test
    void testMultipleCollisions() {
        Car.collide();
        assertEquals(2, Car.getLife(), "Life should be 2 after first collision");
        
        Car.collide();
        assertEquals(2, Car.getLife(), "Life should stay 2 (invulnerability)");
    }

    @Test
    void testDeathAfterCollisions() {
        Car.collide();
        try { Thread.sleep(3100); } catch (InterruptedException e) {}
        Car.invulnerable();
        
        Car.collide();
        try { Thread.sleep(3100); } catch (InterruptedException e) {}
        Car.invulnerable();
        
        Car.collide();
        
        assertEquals(0, Car.getLife(), "Life should be 0 after 3 collisions");
        assertFalse(Car.isAlive(), "Car should be dead after losing all lives");
    }

    @Test
    void testBoost() {
        double initialSpeed = Car.getSpeed();
        Car.startBoost();
        
        assertTrue(Car.isBoostActive(), "Boost should be active after starting");
        assertEquals(initialSpeed * 2, Car.getSpeed(), "Speed should double with boost");
    }

    @Test
    void testBoostDeactivation() {
        Car.startBoost();
        Car.deactivateBoost();
        
        // Le flag BoostActive reste true jusqu'à ce que updateBoost() soit appelé
        // deactivateBoost() remet juste la vitesse à la normale
        assertEquals(1.0, Car.getSpeed(), "Speed should return to base speed");
    }

    @Test
    void testInvulnerabilityTimer() {
        Car.collide();
        assertTrue(Car.isCollide(), "Should be invulnerable after collision");
        
        try {
            Thread.sleep(3100);
        } catch (InterruptedException e) {
            fail("Thread sleep interrupted");
        }
        
        Car.invulnerable();
        assertFalse(Car.isCollide(), "Invulnerability should expire after 3 seconds");
    }
}
