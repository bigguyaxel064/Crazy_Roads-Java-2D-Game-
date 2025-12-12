package com.example.crazy_roads.modes;
import com.example.crazy_roads.models.Car;
import com.example.crazy_roads.models.Obstacle;

import java.util.ArrayList;
import java.util.List;

public class ModeSubwaySurfers {
    private Car car;
    private List<Obstacle> activeObstacle;
    private double baseSpeed; 
    private double actualSpeed; 
    private double accelerationFactor;
    private long startTime; 
    private long lastFrameTime; 
    private int score; 
    private boolean inProgress;
    private boolean gameOver;


    public ModeSubwaySurfers() {
        this.car = new Car();
        this.activeObstacle = new ArrayList<>();
        this.baseSpeed = 1.0;
        this.actualSpeed = baseSpeed;
        this.accelerationFactor = 0.05;
        this.inProgress = false;
        this.gameOver = false;
        this.score = 0;
    }

    public void start() {
        this.car = new Car();
        this.activeObstacle.clear();
        this.actualSpeed = baseSpeed;
        this.score = 0;
        this.startTime = System.currentTimeMillis();
        this.gameOver = false;
        this.inProgress = true;
        this.lastFrameTime = startTime;
    }

    public void update() {
        if(!inProgress || gameOver) return;
        long now = System.currentTimeMillis();
        long deltaMs = now - lastFrameTime;
        lastFrameTime = now;
        increaseSpeed(deltaMs);
        updateObstacles(deltaMs);
        checkCollisions();
        cleanupObstacles();
        car.updateBoost();
        car.invulnerable();
        calculateScore();
        if(!car.isAlive()) {
            gameOver = true;
            inProgress = false;
        }
    }

    private void increaseSpeed(long deltaMs) {
        double deltaSec = deltaMs / 1000.0;
        actualSpeed += accelerationFactor * deltaSec;
        if(actualSpeed > 5.0) {
            actualSpeed = 5.0;
        }
    }


    private void updateObstacles(long deltaMs) {
        for (Obstacle obs : activeObstacle) {
            obs.update(deltaMs);
        }
    }

    private void checkCollisions() {
        for (Obstacle obs : activeObstacle) {
            obs.applyTo(car);
        }
    }

    private void cleanupObstacles() {
        activeObstacle.removeIf(obs -> obs.getDistance() < -10.0);
    }

    private void calculateScore() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        score = (int) (elapsedTime / 1000);
    }

    public void moveLeft() {
        if(inProgress && !gameOver) {
            car.moveLeft();
        }
    }

    public void moveRight() {
        if(inProgress && !gameOver) {
            car.moveRight();
        }
    }

    public void activateBoost() {
        if(inProgress && !gameOver) {
            car.startBoost();
        }
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return car.getLife();
    }

    public int getLane() {
        return car.getLane();
    }

    public double getSpeed() {
        return actualSpeed;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public List<Obstacle> getActiveObstacles() {
        return activeObstacle;
    }
    
    public Car getCar() {
        return car;
    }
}
