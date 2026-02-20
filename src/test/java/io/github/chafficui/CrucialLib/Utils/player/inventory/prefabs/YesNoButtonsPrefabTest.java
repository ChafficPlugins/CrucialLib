package io.github.chafficui.CrucialLib.Utils.player.inventory.prefabs;

import io.github.chafficui.CrucialLib.Main;
import io.github.chafficui.CrucialLib.Utils.player.inventory.InventoryItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class YesNoButtonsPrefabTest {

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
    void getItemsReturnsTwoItems() {
        YesNoButtonsPrefab prefab = new YesNoButtonsPrefab(3, 5, click -> {}, click -> {});
        InventoryItem[] items = prefab.getItems();

        assertEquals(2, items.length);
    }

    @Test
    void defaultYesButtonIsGreenWool() {
        YesNoButtonsPrefab prefab = new YesNoButtonsPrefab(0, 1, click -> {}, click -> {});
        InventoryItem[] items = prefab.getItems();

        assertEquals(Material.GREEN_WOOL, items[0].getMaterial());
    }

    @Test
    void defaultNoButtonIsRedWool() {
        YesNoButtonsPrefab prefab = new YesNoButtonsPrefab(0, 1, click -> {}, click -> {});
        InventoryItem[] items = prefab.getItems();

        assertEquals(Material.RED_WOOL, items[1].getMaterial());
    }

    @Test
    void yesButtonAtCorrectSlot() {
        YesNoButtonsPrefab prefab = new YesNoButtonsPrefab(3, 5, click -> {}, click -> {});
        InventoryItem[] items = prefab.getItems();

        assertEquals(3, items[0].getSlot());
    }

    @Test
    void noButtonAtCorrectSlot() {
        YesNoButtonsPrefab prefab = new YesNoButtonsPrefab(3, 5, click -> {}, click -> {});
        InventoryItem[] items = prefab.getItems();

        assertEquals(5, items[1].getSlot());
    }

    @Test
    void customItemsAreUsed() {
        ItemStack yesStack = new ItemStack(Material.DIAMOND);
        ItemStack noStack = new ItemStack(Material.REDSTONE);
        YesNoButtonsPrefab prefab = new YesNoButtonsPrefab(0, 1, yesStack, noStack, click -> {}, click -> {});
        InventoryItem[] items = prefab.getItems();

        assertEquals(Material.DIAMOND, items[0].getMaterial());
        assertEquals(Material.REDSTONE, items[1].getMaterial());
    }

    @Test
    void yesActionIsExecuted() {
        AtomicBoolean yesClicked = new AtomicBoolean(false);
        YesNoButtonsPrefab prefab = new YesNoButtonsPrefab(0, 1, click -> yesClicked.set(true), click -> {});
        InventoryItem[] items = prefab.getItems();

        items[0].execute(null);
        assertTrue(yesClicked.get());
    }

    @Test
    void noActionIsExecuted() {
        AtomicBoolean noClicked = new AtomicBoolean(false);
        YesNoButtonsPrefab prefab = new YesNoButtonsPrefab(0, 1, click -> {}, click -> noClicked.set(true));
        InventoryItem[] items = prefab.getItems();

        items[1].execute(null);
        assertTrue(noClicked.get());
    }

    @Test
    void implementsInventoryItemPrefab() {
        YesNoButtonsPrefab prefab = new YesNoButtonsPrefab(0, 1, click -> {}, click -> {});
        assertInstanceOf(InventoryItemPrefab.class, prefab);
    }
}
