package com.example.crazy_roads.managers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MusicManagerTest {

    @Test
    void testStopWithoutPlay() {
        assertDoesNotThrow(() -> MusicManager.stopBackgroundMusic(),
                "Stopping without playing should not throw exception");
    }

    @Test
    void testPauseWithoutPlay() {
        assertDoesNotThrow(() -> MusicManager.pauseBackgroundMusic(),
                "Pausing without playing should not throw exception");
    }

    @Test
    void testResumeWithoutPlay() {
        assertDoesNotThrow(() -> MusicManager.resumeBackgroundMusic(),
                "Resuming without playing should not throw exception");
    }

    @Test
    void testUpdateVolumeWithoutPlay() {
        assertDoesNotThrow(() -> MusicManager.updateVolume(),
                "Updating volume without playing should not throw exception");
    }

    @Test
    void testMusicManagerExists() {
        assertNotNull(MusicManager.class, "MusicManager class should exist");
    }
}
