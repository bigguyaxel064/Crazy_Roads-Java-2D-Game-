package com.example.crazy_roads.models;


public class Car {
    protected int lane;
    protected int life;
    protected double speed;
    protected double baseSpeed;
    protected boolean BoostActive;
    protected long boostStartTimeMs;
    protected long boostDurationMs;
    protected long lastBoostEndTime;
    protected long boostCooldownMs;
    protected int boostsRemaining;
    protected boolean isCollide;
    protected long CollideImminent;
    protected long CollideDurationMs;
    protected long CollideDurationMsMk;

    public int getLane() {
        return lane;
    }

    public int getLife() {
        return life;
    }

    public double getSpeed() {
        return speed;
    }

    public Car() {
        this.lane = 1;
        this.life = 3;
        this.speed = 1.0;
        this.baseSpeed = 1.0;
        this.BoostActive = false;
        this.boostStartTimeMs = 0;
        this.boostDurationMs = 5000;
        this.lastBoostEndTime = 0;
        this.boostCooldownMs = 3000;
        this.boostsRemaining = 0;
        this.isCollide = false;
        this.CollideImminent = 0;
        this.CollideDurationMs = 3000;
        this.CollideDurationMsMk = 5000;
    }

    public void moveLeft() {
        if (lane > 0) {
            lane--;
        }
    }

    public void moveRight() {
        if (lane < 2) {
            lane++;
        }
    }

    public void startBoost() {
        long now = System.currentTimeMillis();
        if (BoostActive || (now - lastBoostEndTime < boostCooldownMs)) {
            return;
        }
        this.boostStartTimeMs = now;
        activateBoost();
        this.BoostActive = true;
        this.boostsRemaining--;
    }

    public void updateBoost() {
        if(!isBoostActive()) return;
        long now = System.currentTimeMillis();
        if(now - boostStartTimeMs >= boostDurationMs) {
            deactivateBoost();
            BoostActive = false;
            lastBoostEndTime = now;
        }
    }


    public void collide() {
        if(isCollide) return;
        life--;
        speed -= 0.2;
        if (speed < 0.5) speed = 0.5;
        isCollide = true;
        CollideImminent = System.currentTimeMillis();
    }

    public void collideMk() {
		if(isCollide) return;
		life--;
		isCollide = true;
		CollideImminent = System.currentTimeMillis();
		speed -= 0.5;
		if(speed < 0.5) speed = 0.5;
	}

    public void invulnerable() {
        if(!isCollide) return;
        long right_now = System.currentTimeMillis();
        if(right_now - CollideImminent >= CollideDurationMs) {
            isCollide = false; 
        }
    }
    
    public void invulnerableMk() {
        if(!isCollide) return;
        long right_now = System.currentTimeMillis();
        if(right_now - CollideImminent >= CollideDurationMsMk) {
            isCollide = false; 
        }
    }

    public boolean isAlive() {
        return life > 0;
    }

    public void activateBoost() {
        speed *= 2;
    }


    public void deactivateBoost() {
        speed = baseSpeed;
    }

    public boolean isBoostActive() {
        return BoostActive;
    }

    public boolean isBoostOnCooldown() {
        if (BoostActive) return false;
        long now = System.currentTimeMillis();
        return (now - lastBoostEndTime < boostCooldownMs);
    }

    public long getBoostCooldownRemaining() {
        if (!isBoostOnCooldown()) return 0;
        long now = System.currentTimeMillis();
        long elapsed = now - lastBoostEndTime;
        return Math.max(0, boostCooldownMs - elapsed);
    }

    public boolean isCollide() {
        return isCollide;
    }
    
    public int getBoostsRemaining() {
        return boostsRemaining;
    }

}
