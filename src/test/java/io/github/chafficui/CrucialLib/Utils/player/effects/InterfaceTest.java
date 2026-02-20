package io.github.chafficui.CrucialLib.Utils.player.effects;

import io.github.chafficui.CrucialLib.Main;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class InterfaceTest {

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

    // --- showText overloads ---

    @Test
    void showTextDefaultTimingDoesNotThrow() {
        Player player = server.addPlayer();
        assertDoesNotThrow(() -> Interface.showText(player, "Title", "Sub"));
    }

    @Test
    void showTextWithSecondsDoesNotThrow() {
        Player player = server.addPlayer();
        assertDoesNotThrow(() -> Interface.showText(player, "Title", "Sub", 5));
    }

    @Test
    void showTextWithFadeDoesNotThrow() {
        Player player = server.addPlayer();
        assertDoesNotThrow(() -> Interface.showText(player, "Title", "Sub", 5, 10));
    }

    @Test
    void showTextWithFadeInOutDoesNotThrow() {
        Player player = server.addPlayer();
        assertDoesNotThrow(() -> Interface.showText(player, "Title", "Sub", 5, 10, 20));
    }

    @Test
    void clearTextDoesNotThrow() {
        Player player = server.addPlayer();
        assertDoesNotThrow(() -> Interface.clearText(player));
    }

    // --- Name management ---

    @Test
    void setNameChangesDisplayName() {
        Player player = server.addPlayer();
        Interface.setName(player, "CustomName");

        assertEquals("CustomName", player.getDisplayName());
    }

    @Test
    void setNameChangesTablistName() {
        Player player = server.addPlayer();
        Interface.setName(player, "TabName");

        assertEquals("TabName", player.getPlayerListName());
    }

    @Test
    void setTablistNameOnlyChangesTablist() {
        Player player = server.addPlayer();
        Interface.setTablistName(player, "OnlyTab");

        assertEquals("OnlyTab", player.getPlayerListName());
    }

    @Test
    void setChatNameChangesDisplayName() {
        Player player = server.addPlayer();
        Interface.setChatName(player, "ChatPlayer");

        assertEquals("ChatPlayer", player.getDisplayName());
    }

    @Test
    void setDisplayNameChangesDisplayName() {
        Player player = server.addPlayer();
        Interface.setDisplayName(player, "DisplayPlayer");

        assertEquals("DisplayPlayer", player.getDisplayName());
    }

    // --- Tablist header/footer ---

    @Test
    void setTablistTitleDoesNotThrow() {
        Player player = server.addPlayer();
        assertDoesNotThrow(() -> Interface.setTablistTitle(player,
                new String[]{"Header Line 1", "Header Line 2"},
                new String[]{"Footer Line 1"}));
    }

    @Test
    void removeTablistDoesNotThrow() {
        Player player = server.addPlayer();
        assertDoesNotThrow(() -> Interface.removeTablist(player));
    }
}
