package com.example.crazy_roads.generation;

import com.example.crazy_roads.models.Obstacle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GenerationProceduraleTest {
    private GenerationProcedurale generator;
    private static final double MIN_DISTANCE = 300.0;

    @BeforeEach
    void setUp() {
        generator = new GenerationProcedurale();
    }

    @Test
    void testInitialGeneration() {
        List<Obstacle> obstacles = generator.generateObstacles(0);
        // À la position 0, lastObstacleY=-500, donc la distance est 500 >= 300, des obstacles sont générés
        assertFalse(obstacles.isEmpty(), "Obstacles should be generated at position 0 because distance is sufficient");
        assertTrue(obstacles.size() >= 1 && obstacles.size() <= 2, "Should generate 1 or 2 obstacles");
    }

    @Test
    void testGenerateObstaclesAfterMinDistance() {
        List<Obstacle> obstacles = generator.generateObstacles(MIN_DISTANCE);
        
        assertFalse(obstacles.isEmpty(), "Obstacles should be generated after minimum distance");
        assertTrue(obstacles.size() >= 1 && obstacles.size() <= 2, 
                "Should generate 1 or 2 obstacles per group");
    }

    @Test
    void testObstacleProperties() {
        List<Obstacle> obstacles = generator.generateObstacles(MIN_DISTANCE);
        
        if (!obstacles.isEmpty()) {
            Obstacle obstacle = obstacles.get(0);
            assertEquals(-100.0, obstacle.getPosition(), 
                    "Obstacles should spawn at Y=-100 (off-screen)");
            assertEquals(60.0, obstacle.getSize(), 
                    "Obstacle size should be 60");
            assertEquals(0.0, obstacle.getSpeed(), 
                    "Obstacle speed should be 0 (static)");
            assertEquals(1, obstacle.getDamage(), 
                    "Obstacle damage should be 1");
        }
    }

    @Test
    void testAlwaysOneFreeLane() {
        for (int i = 0; i < 20; i++) {
            generator.reset();
            List<Obstacle> obstacles = generator.generateObstacles(MIN_DISTANCE);
            
            if (!obstacles.isEmpty()) {
                Set<Integer> occupiedLanes = new HashSet<>();
                for (Obstacle obstacle : obstacles) {
                    occupiedLanes.add(obstacle.getLane());
                }
                
                assertTrue(occupiedLanes.size() < 3, 
                        "At least one lane should always be free");
            }
        }
    }

    @Test
    void testLaneValidRange() {
        List<Obstacle> obstacles = generator.generateObstacles(MIN_DISTANCE);
        
        for (Obstacle obstacle : obstacles) {
            int lane = obstacle.getLane();
            assertTrue(lane >= 0 && lane <= 2, 
                    "Lane should be between 0 and 2, but was: " + lane);
        }
    }

    @Test
    void testMultipleGenerations() {
        List<Obstacle> firstBatch = generator.generateObstacles(MIN_DISTANCE);
        assertFalse(firstBatch.isEmpty(), "First batch should generate obstacles");
        
        List<Obstacle> secondBatch = generator.generateObstacles(MIN_DISTANCE + 50);
        assertTrue(secondBatch.isEmpty(), 
                "Second batch should be empty (distance not met)");
        
        List<Obstacle> thirdBatch = generator.generateObstacles(MIN_DISTANCE * 2);
        assertFalse(thirdBatch.isEmpty(), "Third batch should generate obstacles");
    }

    @Test
    void testReset() {
        generator.generateObstacles(MIN_DISTANCE);
        generator.reset();
        
        List<Obstacle> obstacles = generator.generateObstacles(0);
        assertFalse(obstacles.isEmpty(), 
                "After reset, obstacles should be generated at position 0");
    }

    @Test
    void testConsecutiveGeneration() {
        double currentY = 0;
        int generationCount = 0;
        
        for (int i = 0; i < 10; i++) {
            currentY += MIN_DISTANCE;
            List<Obstacle> obstacles = generator.generateObstacles(currentY);
            
            if (!obstacles.isEmpty()) {
                generationCount++;
            }
        }
        
        assertTrue(generationCount > 0, "Should generate obstacles multiple times");
    }

    @Test
    void testObstacleGroupSize() {
        for (int i = 0; i < 50; i++) {
            generator.reset();
            List<Obstacle> obstacles = generator.generateObstacles(MIN_DISTANCE);
            
            assertTrue(obstacles.size() <= 2, 
                    "Obstacle group should never have more than 2 obstacles");
        }
    }

    @Test
    void testNoDuplicateLanesInGroup() {
        for (int i = 0; i < 20; i++) {
            generator.reset();
            List<Obstacle> obstacles = generator.generateObstacles(MIN_DISTANCE);
            
            Set<Integer> lanes = new HashSet<>();
            for (Obstacle obstacle : obstacles) {
                assertTrue(lanes.add(obstacle.getLane()), 
                        "Each obstacle in a group should be in a different lane");
            }
        }
    }

    @Test
    void testSetMinDistance() {
        assertDoesNotThrow(() -> generator.setMinDistance(400.0), 
                "Setting minimum distance should not throw exception");
    }
}
