package com.example.crazy_roads.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ObstacleTest {
    private Obstacle obstacle;

    @BeforeEach
    void setUp() {
        obstacle = new Obstacle(1, 5.0, 100.0, 60.0, 1);
    }

    @Test
    void testInitialState() {
        assertEquals(1, obstacle.getLane(), "Lane should be 1");
        assertEquals(5.0, obstacle.getSpeed(), "Speed should be 5.0");
        assertEquals(100.0, obstacle.getPosition(), "Position should be 100.0");
        assertEquals(60.0, obstacle.getSize(), "Size should be 60.0");
        assertEquals(1, obstacle.getDamage(), "Damage should be 1");
        assertEquals(0, obstacle.getColorType(), "Default color type should be 0");
    }

    @Test
    void testSetters() {
        obstacle.setLane(2);
        assertEquals(2, obstacle.getLane(), "Lane should be updated to 2");

        obstacle.setSpeed(10.0);
        assertEquals(10.0, obstacle.getSpeed(), "Speed should be updated to 10.0");

        obstacle.setPosition(200.0);
        assertEquals(200.0, obstacle.getPosition(), "Position should be updated to 200.0");

        obstacle.setSize(80.0);
        assertEquals(80.0, obstacle.getSize(), "Size should be updated to 80.0");

        obstacle.setDamage(2);
        assertEquals(2, obstacle.getDamage(), "Damage should be updated to 2");

        obstacle.setColorType(1);
        assertEquals(1, obstacle.getColorType(), "Color type should be updated to 1");
    }

    @Test
    void testColorTypeBlue() {
        obstacle.setColorType(0);
        assertEquals(0, obstacle.getColorType(), "Color type 0 represents blue barrel");
    }

    @Test
    void testColorTypeRed() {
        obstacle.setColorType(1);
        assertEquals(1, obstacle.getColorType(), "Color type 1 represents red barrel");
    }

    @Test
    void testPositionUpdate() {
        double initialPosition = obstacle.getPosition();
        obstacle.setPosition(initialPosition + 10);
        assertEquals(initialPosition + 10, obstacle.getPosition(), "Position should increase");
    }

    @Test
    void testMultipleUpdates() {
        obstacle.setPosition(50.0);
        obstacle.setPosition(100.0);
        obstacle.setPosition(150.0);
        assertEquals(150.0, obstacle.getPosition(), "Position should be updated to latest value");
    }

    @Test
    void testLaneBoundaries() {
        obstacle.setLane(0);
        assertEquals(0, obstacle.getLane(), "Lane 0 should be valid");

        obstacle.setLane(2);
        assertEquals(2, obstacle.getLane(), "Lane 2 should be valid");
    }
}
