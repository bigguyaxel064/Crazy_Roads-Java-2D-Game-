package com.example.crazy_roads.models;

public class BonusItem {
    private int lane;
    private double x;
    private double y;
    private double size;
    private BonusType type;
    private boolean collected;
    
    public enum BonusType {
        INVINCIBILITY,
        SCORE_MULTIPLIER
    }
    
    public BonusItem(int lane, double x, double y, double size, BonusType type) {
        this.lane = lane;
        this.x = x;
        this.y = y;
        this.size = size;
        this.type = type;
        this.collected = false;
    }
    
    public int getLane() {
        return lane;
    }
    
    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public double getSize() {
        return size;
    }
    
    public BonusType getType() {
        return type;
    }
    
    public boolean isCollected() {
        return collected;
    }
    
    public void collect() {
        this.collected = true;
    }
    
    public boolean checkCollision(Car car, double carY) {
        if (collected) return false;
        
        return car.getLane() == this.lane && 
               Math.abs(carY - this.y) < (size + 30);
    }
}
