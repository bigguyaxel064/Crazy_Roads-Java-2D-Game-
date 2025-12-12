package com.example.crazy_roads.generation;

import com.example.crazy_roads.models.Obstacle;
import com.example.crazy_roads.models.BonusItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerationProcedurale {
    private Random random;
    private double lastObstacleY;
    private int frameCount;
    private static final double MIN_DISTANCE_BETWEEN_OBSTACLES = 300;
    private static final double OBSTACLE_SIZE = 60;
    private static final double BONUS_SIZE = 50;
    private static final int LANE_COUNT = 3;
    private static final int BONUS_SPAWN_INTERVAL = 30;
    
    public GenerationProcedurale() {
        this.random = new Random();
        this.lastObstacleY = -500;
        this.frameCount = 0;
    }
    
    public List<Obstacle> generateObstacles(double currentY) {
        List<Obstacle> newObstacles = new ArrayList<>();
        
        if (currentY - lastObstacleY >= MIN_DISTANCE_BETWEEN_OBSTACLES) {
            double spawnY = -100;
            List<Obstacle> obstacleGroup = generateObstacleGroup(spawnY);
            newObstacles.addAll(obstacleGroup);
            lastObstacleY = currentY;
        }
        
        return newObstacles;
    }
    
    public BonusItem generateBonus() {
        frameCount++;
        
        if (frameCount >= BONUS_SPAWN_INTERVAL) {
            frameCount = 0;
            
            int randomLane = random.nextInt(LANE_COUNT);
            
            BonusItem.BonusType randomType = random.nextBoolean() ? 
                BonusItem.BonusType.INVINCIBILITY : 
                BonusItem.BonusType.SCORE_MULTIPLIER;
            
            return new BonusItem(randomLane, 0.0, -100, BONUS_SIZE, randomType);
        }
        
        return null;
    }
    
    private List<Obstacle> generateObstacleGroup(double yPosition) {
        List<Obstacle> obstacles = new ArrayList<>();
        
        int freeLane = random.nextInt(LANE_COUNT);
        
        int obstacleCount = random.nextInt(2) + 1;
        
        List<Integer> occupiedLanes = new ArrayList<>();
        
        for (int i = 0; i < obstacleCount; i++) {
            int lane;
            do {
                lane = random.nextInt(LANE_COUNT);
            } while (lane == freeLane || occupiedLanes.contains(lane));
            
            occupiedLanes.add(lane);
            
            Obstacle obstacle = new Obstacle(
                lane,
                0.0,
                yPosition,
                OBSTACLE_SIZE,
                1
            );
            
            obstacles.add(obstacle);
        }
        
        return obstacles;
    }
    
    public void reset() {
        this.lastObstacleY = -500;
        this.frameCount = 0;
    }
    
    public void setMinDistance(double distance) {
    }
}
