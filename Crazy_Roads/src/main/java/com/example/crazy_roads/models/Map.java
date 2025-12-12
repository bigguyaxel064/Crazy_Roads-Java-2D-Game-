package com.example.crazy_roads.models;

import com.example.crazy_roads.managers.RessourceLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private List<Frame> frames;
    private double scrollSpeed = 16.0;
    private boolean useImages = false;

    private Image subwayImage;
    private Image finishLineImage;
    private boolean isMarioKartMode = false;
    private int totalFrames = 0;
    private int framesScrolled = 0;

    private static final double FRAME_HEIGHT = 600;

    public Map(int i) {
        frames = new ArrayList<>();
        loadImages();
        initializeFrames();
    }

    public Map(boolean marioKartMode) {
        this.isMarioKartMode = marioKartMode;
        this.totalFrames = 100;
        frames = new ArrayList<>();
        loadImages();
        initializeFramesMarioKart();
    }

    private void loadImages() {
        String imagePath = "/images/Map/Frame1.png";
        subwayImage = RessourceLoader.loadImage(imagePath);
        useImages = RessourceLoader.isImageValid(subwayImage);
        
        if (isMarioKartMode) {
            String finishPath = "/images/Map/Finish line.png";
            finishLineImage = RessourceLoader.loadImage(finishPath);
        }
    }

    private void initializeFrames() {
        frames.clear();

        if (useImages) {
            frames.add(new Frame(subwayImage, 0, FRAME_HEIGHT));
            frames.add(new Frame(subwayImage, -FRAME_HEIGHT, FRAME_HEIGHT));
            frames.add(new Frame(subwayImage, -FRAME_HEIGHT*2, FRAME_HEIGHT));
            frames.add(new Frame(subwayImage, -FRAME_HEIGHT*3, FRAME_HEIGHT));
            frames.add(new Frame(subwayImage, -FRAME_HEIGHT*4, FRAME_HEIGHT));
            frames.add(new Frame(subwayImage, -FRAME_HEIGHT*5, FRAME_HEIGHT));
            frames.add(new Frame(subwayImage, -FRAME_HEIGHT*6, FRAME_HEIGHT));


        } else {
            frames.add(new Frame(null, 0, FRAME_HEIGHT));
            frames.add(new Frame(null, -FRAME_HEIGHT, FRAME_HEIGHT));
        }
    }

    private void initializeFramesMarioKart() {
        frames.clear();

        if (useImages) {
            for (int i = 0; i < 99; i++) {
                frames.add(new Frame(subwayImage, -FRAME_HEIGHT * i, FRAME_HEIGHT));
            }
            if (RessourceLoader.isImageValid(finishLineImage)) {
                frames.add(new Frame(finishLineImage, -FRAME_HEIGHT * 99, FRAME_HEIGHT));
            } else {
                frames.add(new Frame(subwayImage, -FRAME_HEIGHT * 99, FRAME_HEIGHT));
            }
        } else {
            for (int i = 0; i < 100; i++) {
                frames.add(new Frame(null, -FRAME_HEIGHT * i, FRAME_HEIGHT));
            }
        }
        
        framesScrolled = 0;
    }

    public void update() {
        if (frames.isEmpty()) return;

        for (Frame frame : frames) {
            frame.move(scrollSpeed);
        }

        if (isMarioKartMode) {
            while (!frames.isEmpty() && frames.get(0).isOffScreen()) {
                frames.remove(0);
                framesScrolled++;
            }
        } else {
            while (!frames.isEmpty() && frames.get(0).isOffScreen()) {
                frames.remove(0);

                double lastY = frames.isEmpty() ? 0 : frames.get(frames.size() - 1).getPositionY();
                if (useImages) {
                    frames.add(new Frame(subwayImage, lastY - FRAME_HEIGHT, FRAME_HEIGHT));
                } else {
                    frames.add(new Frame(null, lastY - FRAME_HEIGHT, FRAME_HEIGHT));
                }
            }
        }
    }

    public void render(GraphicsContext gc, double roadWidth, double roadX) {
        if (frames.isEmpty()) {
            gc.setFill(Color.DARKGRAY);
            gc.fillRect(roadX, 0, roadWidth, FRAME_HEIGHT);
            gc.setFill(Color.WHITE);
            gc.fillText("Aucune frame chargée", roadX + roadWidth / 2 - 50, FRAME_HEIGHT / 2);
            return;
        }

        if (useImages) {
            for (Frame frame : frames) {
                frame.render(gc, roadWidth, roadX);
            }
        } else {
            Color[] colors = {Color.LIGHTBLUE, Color.LIGHTGREEN, Color.LIGHTYELLOW};
            int colorIndex = 0;
            for (Frame frame : frames) {
                gc.setFill(colors[colorIndex % colors.length]);
                gc.fillRect(roadX, frame.getPositionY(), roadWidth, frame.getHeight());

                gc.setStroke(Color.BLACK);
                gc.setLineWidth(2);
                gc.strokeRect(roadX + 5, frame.getPositionY() + 5, roadWidth - 10, frame.getHeight() - 10);

                gc.setFill(Color.BLACK);
                gc.fillText("Frame " + colorIndex + " - Y: " + (int) frame.getPositionY(),
                        roadX + roadWidth / 2 - 50, frame.getPositionY() + frame.getHeight() / 2);
                colorIndex++;
            }
        }
    }
    public List<Frame> getFrames() {
        return frames;
    }

    public double getScrollSpeed() {
        return scrollSpeed;
    }

    public int getFramesScrolled() {
        return framesScrolled;
    }

    public boolean isFinishLineReached() {
        return isMarioKartMode && framesScrolled >= 100;
    }

    public void setScrollSpeed(double scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }

    public boolean isUseImages() {
        return useImages;
    }
}
