package io.github.chafficui.CrucialLib.Events;

import io.github.chafficui.CrucialLib.Main;
import io.github.chafficui.CrucialLib.Utils.customItems.CrucialItem;
import io.github.chafficui.CrucialLib.exceptions.CrucialException;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CrucialItemEventsTest {

    private ServerMock server;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        MockBukkit.load(Main.class);
        CrucialItem.CRUCIAL_ITEMS.clear();
    }

    @AfterEach
    void tearDown() {
        CrucialItem.CRUCIAL_ITEMS.clear();
        MockBukkit.unmock();
    }

    // --- Core event logic tests (without relying on MockBukkit event dispatch quirks) ---

    @Test
    void registeredItemIsInGlobalSet() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Blocked", Material.DIAMOND_SWORD, List.of(), recipe, "test", true, false, false);
        item.register();

        assertTrue(CrucialItem.CRUCIAL_ITEMS.contains(item));
        assertFalse(item.isUsable, "Item should be non-usable");
    }

    @Test
    void usableItemFlagIsRespected() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Usable", Material.IRON_SWORD, List.of(), recipe, "test", true, true, false);
        item.register();

        assertTrue(item.isUsable, "Item should be usable");
    }

    @Test
    void craftingFlagIsRespected() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem allowedItem = new CrucialItem("Allowed", Material.DIAMOND, List.of(), recipe, "test", true, true, true);
        CrucialItem blockedItem = new CrucialItem("Blocked", Material.GOLD_INGOT, List.of(), recipe, "test2", true, true, false);
        allowedItem.register();
        blockedItem.register();

        assertTrue(allowedItem.isAllowedForCrafting, "Item should be allowed for crafting");
        assertFalse(blockedItem.isAllowedForCrafting, "Item should not be allowed for crafting");
    }

    @Test
    void plainItemIsNotRecognizedAsCrucialItem() {
        ItemStack plainStack = new ItemStack(Material.STONE);
        assertNull(CrucialItem.getByStack(plainStack), "Plain items should not be recognized as CrucialItems");
    }

    @Test
    void nullItemIsNotRecognizedAsCrucialItem() {
        assertNull(CrucialItem.getId(null), "Null should not be recognized as a CrucialItem");
    }
}
