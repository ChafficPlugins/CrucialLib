package io.github.chafficui.CrucialLib.Utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    private ServerMock server;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void getVersionReturnsNonNull() {
        String version = Server.getVersion();
        assertNotNull(version);
    }

    @Test
    void checkCompatibilityWithMatchingVersion() {
        // MockBukkit's version string should contain something we can match
        String version = Server.getVersion();
        // Use a substring of the actual version to ensure a match
        assertTrue(Server.checkCompatibility(version));
    }

    @Test
    void checkCompatibilityWithNonMatchingVersion() {
        assertFalse(Server.checkCompatibility("99.99"));
    }

    @Test
    void checkCompatibilityWithMultipleVersions() {
        // At least one should not match
        assertFalse(Server.checkCompatibility("99.98", "99.99"));
    }

    @Test
    void checkCompatibilityWithEmptyArray() {
        assertFalse(Server.checkCompatibility());
    }

    @Test
    void logDoesNotThrow() {
        assertDoesNotThrow(() -> Server.log("test message"));
    }

    @Test
    void errorDoesNotThrow() {
        assertDoesNotThrow(() -> Server.error("test error"));
    }
}
