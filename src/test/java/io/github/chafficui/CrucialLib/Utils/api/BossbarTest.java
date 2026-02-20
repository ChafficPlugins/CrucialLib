package io.github.chafficui.CrucialLib.Utils.api;

import io.github.chafficui.CrucialLib.Main;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class BossbarTest {

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
    void sendBossbarDoesNotThrow() {
        Player player = server.addPlayer();
        assertDoesNotThrow(() -> Bossbar.sendBossbar(player, "Test Bar", BarColor.RED, 50f, 100L));
    }

    @Test
    void sendBossbarTwiceToSamePlayerDoesNotThrow() {
        Player player = server.addPlayer();
        Bossbar.sendBossbar(player, "First", BarColor.BLUE, 25f, 100L);
        assertDoesNotThrow(() -> Bossbar.sendBossbar(player, "Updated", BarColor.GREEN, 75f, 200L));
    }

    @Test
    void sendBossbarToDifferentPlayers() {
        Player player1 = server.addPlayer();
        Player player2 = server.addPlayer();

        assertDoesNotThrow(() -> {
            Bossbar.sendBossbar(player1, "Bar 1", BarColor.RED, 50f, 100L);
            Bossbar.sendBossbar(player2, "Bar 2", BarColor.BLUE, 75f, 100L);
        });
    }

    @Test
    void sendBossbarWithZeroProgress() {
        Player player = server.addPlayer();
        assertDoesNotThrow(() -> Bossbar.sendBossbar(player, "Empty", BarColor.WHITE, 0f, 100L));
    }

    @Test
    void sendBossbarWithFullProgress() {
        Player player = server.addPlayer();
        assertDoesNotThrow(() -> Bossbar.sendBossbar(player, "Full", BarColor.YELLOW, 100f, 100L));
    }
}
