package com.example.crazy_roads.gui;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IGamePageTest {

    @Test
    void testIGamePageInterfaceExists() {
        assertNotNull(IGamePage.class, "IGamePage interface should exist");
    }

    @Test
    void testIGamePageIsInterface() {
        assertTrue(IGamePage.class.isInterface(), 
                   "IGamePage should be an interface");
    }

    @Test
    void testIGamePageHasMethods() {
        assertTrue(IGamePage.class.getDeclaredMethods().length > 0 || 
                   IGamePage.class.getMethods().length > 0,
                   "IGamePage should declare methods");
    }
}
