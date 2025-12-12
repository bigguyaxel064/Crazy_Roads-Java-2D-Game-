package com.example.crazy_roads.models;

import com.example.crazy_roads.managers.RessourceLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Frame {
    private Image image;
    private double positionY;
    private double height;

    public Frame(Image image, double positionY, double height) {
        this.image = image;
        this.positionY = positionY;
        this.height = height;
    }

    public void move(double speed) {
        this.positionY += speed;
    }

    public boolean isOffScreen() {
        return this.positionY > 800;
    }

    public void render(GraphicsContext gc, double roadWidth, double roadX) {
        if (RessourceLoader.isImageValid(image)) {
            try {
                if (image.getWidth() > 0 && image.getHeight() > 0) {
                    gc.drawImage(image, roadX, positionY, roadWidth, height);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public double getHeight() {
        return height;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean hasValidImage() {
        return RessourceLoader.isImageValid(image);
    }
}