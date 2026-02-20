package io.github.chafficui.CrucialLib.Utils.player.inventory;

import io.github.chafficui.CrucialLib.Main;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class InventoryItemTest {

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

    // --- Construction and properties ---

    @Test
    void constructorWithMaterialSetsFields() {
        InventoryItem item = new InventoryItem(5, Material.DIAMOND, "Diamond", List.of("Rare"), click -> {});
        assertEquals(5, item.getSlot());
        assertEquals(Material.DIAMOND, item.getMaterial());
        assertEquals("Diamond", item.getName());
    }

    @Test
    void constructorWithItemStackPreservesType() {
        ItemStack stack = new ItemStack(Material.EMERALD);
        InventoryItem item = new InventoryItem(3, stack, click -> {});
        assertEquals(3, item.getSlot());
        assertEquals(Material.EMERALD, item.getMaterial());
    }

    @Test
    void defaultSlotOnlyConstructorUsesGlassPane() {
        InventoryItem item = new InventoryItem(0);
        assertEquals(Material.WHITE_STAINED_GLASS_PANE, item.getMaterial());
    }

    @Test
    void defaultIsMovableFalse() {
        InventoryItem item = new InventoryItem(0, new ItemStack(Material.STONE), click -> {});
        assertFalse(item.isMovable);
    }

    @Test
    void movableCanBeSetTrue() {
        InventoryItem item = new InventoryItem(0, new ItemStack(Material.STONE), click -> {}, true);
        assertTrue(item.isMovable);
    }

    @Test
    void movableWithSlotOnly() {
        InventoryItem item = new InventoryItem(0, true);
        assertTrue(item.isMovable);
    }

    // --- Inventory item detection ---

    @Test
    void isInventoryItemReturnsFalseForPlainStack() {
        ItemStack plain = new ItemStack(Material.STONE);
        assertFalse(InventoryItem.isInventoryItem(plain));
    }

    @Test
    void isInventoryItemReturnsFalseForNull() {
        assertFalse(InventoryItem.isInventoryItem(null));
    }

    @Test
    void inventoryItemHasAttributeModifier() {
        InventoryItem item = new InventoryItem(0, new ItemStack(Material.STONE), click -> {});
        ItemStack stack = item.getItem();
        assertNotNull(stack.getItemMeta());
        // Verify the attribute modifier was added (MAX_HEALTH with CRUCIALLIB_INVENTORYITEM)
        assertNotNull(stack.getItemMeta().getAttributeModifiers());
    }

    // --- Lore ---

    @Test
    void getLoreReturnsSetLore() {
        InventoryItem item = new InventoryItem(0, Material.STONE, "Stone", List.of("Line 1", "Line 2"), click -> {});
        List<String> lore = item.getLore();
        assertNotNull(lore);
        assertEquals(2, lore.size());
        assertEquals("Line 1", lore.get(0));
        assertEquals("Line 2", lore.get(1));
    }

    @Test
    void getLoreReturnsNullForItemWithoutLore() {
        InventoryItem item = new InventoryItem(0);
        assertNull(item.getLore());
    }

    // --- Extra data ---

    @Test
    void extraDataCanStoreValues() {
        InventoryItem item = new InventoryItem(0);
        item.extraData.put("key", "value");
        item.extraData.put("count", 42);

        assertEquals("value", item.extraData.get("key"));
        assertEquals(42, item.extraData.get("count"));
    }

    // --- Action execution ---

    @Test
    void executeRunsProvidedAction() {
        AtomicBoolean executed = new AtomicBoolean(false);
        InventoryItem item = new InventoryItem(0, new ItemStack(Material.STONE), click -> executed.set(true));

        item.execute(null);
        assertTrue(executed.get());
    }

    @Test
    void defaultActionDoesNothing() {
        InventoryItem item = new InventoryItem(0, new ItemStack(Material.STONE));
        assertDoesNotThrow(() -> item.execute(null));
    }

    // --- Multiple constructors all produce valid items ---

    @Test
    void allConstructorsProduceItemsWithCorrectSlots() {
        InventoryItem a = new InventoryItem(0);
        InventoryItem b = new InventoryItem(1, new ItemStack(Material.STONE));
        InventoryItem c = new InventoryItem(2, Material.DIAMOND, "D", List.of(), click -> {});
        InventoryItem d = new InventoryItem(3, new ItemStack(Material.GOLD_INGOT), click -> {});
        InventoryItem e = new InventoryItem(4, true);
        InventoryItem f = new InventoryItem(5, new ItemStack(Material.IRON_INGOT), click -> {}, true);

        assertEquals(0, a.getSlot());
        assertEquals(1, b.getSlot());
        assertEquals(2, c.getSlot());
        assertEquals(3, d.getSlot());
        assertEquals(4, e.getSlot());
        assertEquals(5, f.getSlot());

        assertNotNull(a.getItem());
        assertNotNull(b.getItem());
        assertNotNull(c.getItem());
        assertNotNull(d.getItem());
        assertNotNull(e.getItem());
        assertNotNull(f.getItem());
    }

    @Test
    void constructorWithMaterialAndMovable() {
        InventoryItem item = new InventoryItem(3, Material.EMERALD, "Gem", List.of("Shiny"), click -> {}, true);
        assertEquals(3, item.getSlot());
        assertEquals(Material.EMERALD, item.getMaterial());
        assertEquals("Gem", item.getName());
        assertTrue(item.isMovable);
    }
}
