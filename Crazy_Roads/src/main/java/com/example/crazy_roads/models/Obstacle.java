package com.example.crazy_roads.models;

public class Obstacle {
	protected int lane;
	protected double speed;
	protected double position;
	protected double distance;
	protected double size;
	protected int damage;
	protected boolean active;
	protected int colorType;
	
	public int getLane() {
		return lane;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public double getSize() {
		return size;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public double getPosition() {
		return position;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public int getColorType() {
		return colorType;
	}
	
	public void setColorType(int colorType) {
		this.colorType = colorType;
	}
	
	public void setLane(int lane) {
		this.lane = lane;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void setPosition(double position) {
		this.position = position;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public void setSize(double size) {
		this.size = size;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public Obstacle(int lane, double speed, double position, double size, int damage) {
		this.lane = lane;
		this.speed = speed;
		this.position = position;
		this.size = size;
		this.damage = damage;
		this.active = true;
		this.colorType = 0;
	}
	
	public void update(long deltaMs) {
		if(!active) return;
		if(speed != 0.0) {
			double deltaSec = deltaMs / 1000.0;
			this.distance += speed * deltaSec;
		}
	}
	
	public boolean collidesWith(Car v) {
		if(!active) return false;
		if(v == null) return false;
		if(this.lane != v.getLane()) return false;
		double carPosition = 0.0;
		double carAppSize = 0.5;
		double distDiff = Math.abs(this.position - carPosition);
		return distDiff <= (this.size + carAppSize);
	}
	
	public boolean collidesWithScreenPosition(Car v, double carYPosition) {
		if(!active) return false;
		if(v == null) return false;
		if(this.lane != v.getLane()) return false;
		// Vérifier si l'obstacle est proche de la Car sur l'écran
		double distDiff = Math.abs(this.position - carYPosition);
		return distDiff <= 80.0; // Zone de collision
	}
	
	public boolean applyTo(Car v) {
		if(!active) return false;
		if(v == null) return false;
		if(collidesWith(v)) {
			v.collide();
			this.active = false;
		}
		return true;
	}
	public boolean applyToMK(Car c) {
		if(!active) return false;
		if (c == null) return false;
		if(collidesWith(c)) {
			c.collideMk();
			this.active = false;
		}
		return true;
	}
	
	public boolean applyToMKScreenPosition(Car c, double carYPosition) {
		if(!active) return false;
		if (c == null) return false;
		if(collidesWithScreenPosition(c, carYPosition)) {
			c.collideMk();
			this.active = false;
		}
		return true;
	}
}

