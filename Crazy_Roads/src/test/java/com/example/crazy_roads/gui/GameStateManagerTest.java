package com.example.crazy_roads.gui;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameStateManagerTest {

    @Test
    void testGameStateManagerClassExists() {
        assertNotNull(GameStateManager.class, "GameStateManager class should exist");
    }

    @Test
    void testGameStateManagerCanBeInstantiated() {
        assertDoesNotThrow(() -> new GameStateManager(),
                "GameStateManager should be instantiable");
    }

    @Test
    void testGameStateManagerIsNotNull() {
        GameStateManager manager = new GameStateManager();
        assertNotNull(manager, "GameStateManager instance should not be null");
    }
}
