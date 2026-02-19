package io.github.chafficui.CrucialLib.Utils.api;

import io.github.chafficui.CrucialLib.Main;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class TitleTest {

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
    void sendTitleDoesNotThrow() {
        Player player = server.addPlayer();
        assertDoesNotThrow(() -> Title.sendTitle(player, 10, 40, 10, "Hello", "World"));
    }

    @Test
    void sendTitleWithEmptyStrings() {
        Player player = server.addPlayer();
        assertDoesNotThrow(() -> Title.sendTitle(player, 0, 0, 0, "", ""));
    }
}
