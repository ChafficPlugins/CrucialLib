package io.github.chafficui.CrucialLib.Utils;

import io.github.chafficui.CrucialLib.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class StatsTest {

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
    void statsCanBeCreated() {
        Stats stats = new Stats(plugin, 12345);
        assertNotNull(stats);
    }

    @Test
    void sendPieChartDoesNotThrow() {
        Stats stats = new Stats(plugin, 12345);
        assertDoesNotThrow(() -> stats.sendPieChart("test_chart", "test_data"));
    }
}
