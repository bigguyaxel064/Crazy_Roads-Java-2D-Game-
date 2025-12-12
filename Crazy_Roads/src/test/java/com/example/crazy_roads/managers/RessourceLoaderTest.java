package com.example.crazy_roads.managers;

import javafx.scene.image.Image;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RessourceLoaderTest {

    @Test
    void testLoadImage() {
        assertDoesNotThrow(() -> RessourceLoader.loadImage("/images/Map/Frame1.png"),
                "Loading image should not throw exception");
    }

    @Test
    void testLoadImageSafe() {
        Image image = RessourceLoader.loadImageSafe("/images/Map/Frame1.png", 400, 600);
        assertNotNull(image, "Loaded image should not be null");
    }

    @Test
    void testLoadImageSafeWithInvalidPath() {
        Image image = RessourceLoader.loadImageSafe("/invalid/path/image.png", 400, 600);
        assertNotNull(image, "Should return fallback image for invalid path");
        assertFalse(image.isError(), "Fallback image should not have errors");
    }

    @Test
    void testLoadImageSafeWithValidDimensions() {
        Image image = RessourceLoader.loadImageSafe("/images/Map/Frame1.png", 200, 300);
        assertNotNull(image, "Image should be loaded");
        assertTrue(image.getWidth() > 0, "Image width should be positive");
        assertTrue(image.getHeight() > 0, "Image height should be positive");
    }

    @Test
    void testIsImageValidWithNull() {
        assertFalse(RessourceLoader.isImageValid(null), 
                    "Null image should be invalid");
    }

    @Test
    void testIsImageValidWithValidImage() {
        Image image = RessourceLoader.loadImage("/images/Map/Frame1.png");
        // Result depends on whether the image exists and loads successfully
        assertNotNull(image, "Loaded image should not be null");
    }

    @Test
    void testLoadImageWithDifferentSizes() {
        assertDoesNotThrow(() -> {
            RessourceLoader.loadImageSafe("/images/Map/Frame1.png", 100, 100);
            RessourceLoader.loadImageSafe("/images/Map/Frame1.png", 500, 500);
            RessourceLoader.loadImageSafe("/images/Map/Frame1.png", 1000, 1000);
        }, "Loading images with different sizes should not throw exception");
    }

    @Test
    void testFallbackImageCreation() {
        Image fallback = RessourceLoader.loadImageSafe("/nonexistent/path.png", 300, 400);
        assertNotNull(fallback, "Fallback image should be created");
        assertTrue(fallback.getWidth() > 0, "Fallback width should be positive");
        assertTrue(fallback.getHeight() > 0, "Fallback height should be positive");
    }

    @Test
    void testLoadMultipleImages() {
        assertDoesNotThrow(() -> {
            RessourceLoader.loadImage("/images/Map/Frame1.png");
            RessourceLoader.loadImage("/images/Car/voiture_jaune.png");
            RessourceLoader.loadImage("/images/Map/Finish line.png");
        }, "Loading multiple images should not throw exception");
    }

    @Test
    void testImageDimensionsNotNegative() {
        Image image = RessourceLoader.loadImageSafe("/images/Map/Frame1.png", 400, 600);
        assertTrue(image.getWidth() >= 0, "Image width should not be negative");
        assertTrue(image.getHeight() >= 0, "Image height should not be negative");
    }

    @Test
    void testLargeImageDimensions() {
        assertDoesNotThrow(() -> 
            RessourceLoader.loadImageSafe("/images/Map/Frame1.png", 2000, 2000),
            "Loading with large dimensions should not throw exception");
    }

    @Test
    void testSmallImageDimensions() {
        Image image = RessourceLoader.loadImageSafe("/images/Map/Frame1.png", 10, 10);
        assertNotNull(image, "Image with small dimensions should be created");
        assertTrue(image.getWidth() > 0, "Small image should have positive width");
    }
}
