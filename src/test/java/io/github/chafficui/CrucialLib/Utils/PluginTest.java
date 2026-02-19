package io.github.chafficui.CrucialLib.Utils;

import io.github.chafficui.CrucialLib.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class PluginTest {

    private ServerMock server;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        MockBukkit.load(Main.class);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void getVersionReturnsPluginVersion() {
        String version = Plugin.getVersion();
        assertNotNull(version);
        assertFalse(version.isEmpty());
    }
}
