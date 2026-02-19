package io.github.chafficui.CrucialLib;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private ServerMock server;
    private Main plugin;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(Main.class);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void pluginLoads() {
        assertNotNull(plugin);
        assertTrue(plugin.isEnabled());
    }

    @Test
    void pluginHasCorrectName() {
        assertEquals("CrucialLib", plugin.getDescription().getName());
    }

    @Test
    void getVersionReturnsNonNull() {
        assertNotNull(plugin.getVersion());
        assertFalse(plugin.getVersion().isEmpty());
    }
}
