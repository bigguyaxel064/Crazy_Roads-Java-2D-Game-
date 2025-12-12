package com.example.crazy_roads.managers;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import java.io.InputStream;

public class RessourceLoader {

    public static Image loadImage(String path) {
        return loadImageSafe(path, 452, 800);
    }

    public static Image loadImageSafe(String path, double targetWidth, double targetHeight) {
        try {
            InputStream stream = RessourceLoader.class.getResourceAsStream(path);

            if (stream == null) {
                System.err.println("Stream null pour: " + path);
                return createFallbackImage(targetWidth, targetHeight);
            }

            Image image = new Image(stream, targetWidth, targetHeight, true, true);

            if (image.isError()) {
                System.err.println("Erreur image pour: " + path);
                return createFallbackImage(targetWidth, targetHeight);
            }

            return image;

        } catch (Exception e) {
            System.err.println("Exception pour: " + path + " - " + e.getMessage());
            return createFallbackImage(targetWidth, targetHeight);
        }
    }

    private static Image createFallbackImage(double width, double height) {
        WritableImage fallback = new WritableImage((int)width, (int)height);
        PixelWriter writer = fallback.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int gray = 50 + (y * 100 / (int)height);
                int color = (255 << 24) | (gray << 8);
                writer.setArgb(x, y, color);
            }
        }

        return fallback;
    }

    public static boolean isImageValid(Image image) {
        if (image == null) {
            return false;
        }

        if (image.isError()) {
            return false;
        }

        if (image.getWidth() <= 0 || image.getHeight() <= 0) {
            return false;
        }

        return !(image.getProgress() < 1.0);
    }
}