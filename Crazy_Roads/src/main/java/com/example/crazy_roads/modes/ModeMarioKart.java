package com.example.crazy_roads.modes;
import com.example.crazy_roads.models.Car;
import com.example.crazy_roads.models.Obstacle;
import java.util.ArrayList;
import java.util.List;

public class ModeMarioKart {
    private Car car;
    private double baseSpeed;
    private double currentSpeed;
    private double boostMultiplier;
    private long raceStartTime;
    private long raceEndTime;
    private boolean raceStarted;
    private boolean raceFinished;
    private boolean gameOver;
    private int currentFrame;
    private static final int TOTAL_FRAMES = 40;

    public ModeMarioKart() {
        this.car = new Car();
        this.baseSpeed = 5.0;
        this.currentSpeed = baseSpeed;
        this.boostMultiplier = 2.0;
        this.raceStarted = false;
        this.raceFinished = false;
        this.gameOver = false;
        this.currentFrame = 0;
    }

    public void start() {
        this.car = new Car();
        this.currentSpeed = baseSpeed;
        this.raceStartTime = System.currentTimeMillis();
        this.raceStarted = true;
        this.raceFinished = false;
        this.gameOver = false;
        this.currentFrame = 0;
    }

    public void update() {
        if (!raceStarted || raceFinished || gameOver) return;

        car.updateBoost();
        car.invulnerable();

        if (car.getLife() <= 0) {
            gameOver = true;
            raceStarted = false;
        }
    }

    public void moveLeft() {
        if (raceStarted && !gameOver) {
            car.moveLeft();
        }
    }

    public void moveRight() {
        if (raceStarted && !gameOver) {
            car.moveRight();
        }
    }

    public void activateBoost() {
        if (raceStarted && !gameOver) {
            car.startBoost();
        }
    }

    public void incrementFrame() {
        currentFrame++;
        if (currentFrame >= TOTAL_FRAMES) {
            finishRace();
        }
    }

    private void finishRace() {
        raceFinished = true;
        raceEndTime = System.currentTimeMillis();
    }

    public long getRaceTime() {
        if (!raceStarted) return 0;
        if (raceFinished) {
            return (raceEndTime - raceStartTime) / 1000;
        }
        return (System.currentTimeMillis() - raceStartTime) / 1000;
    }

    public int getLane() {
        return car.getLane();
    }

    public int getLives() {
        return car.getLife();
    }

    public double getSpeed() {
        return car.isBoostActive() ? currentSpeed * boostMultiplier : currentSpeed;
    }

    public boolean isBoostActive() {
        return car.isBoostActive();
    }

    public boolean isBoostOnCooldown() {
        return car.isBoostOnCooldown();
    }

    public long getBoostCooldownRemaining() {
        return car.getBoostCooldownRemaining();
    }

    public boolean isRaceStarted() {
        return raceStarted;
    }

    public boolean isRaceFinished() {
        return raceFinished;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public int getTotalFrames() {
        return TOTAL_FRAMES;
    }

    public long getRaceStartTime() {
        return raceStartTime;
    }

    public Car getCar() {
        return car;
    }
}
