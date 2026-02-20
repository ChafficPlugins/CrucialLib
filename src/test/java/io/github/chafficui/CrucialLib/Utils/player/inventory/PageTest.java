package io.github.chafficui.CrucialLib.Utils.player.inventory;

import io.github.chafficui.CrucialLib.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class PageTest {

    private ServerMock server;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        MockBukkit.load(Main.class);
        Page.pages.clear();
    }

    @AfterEach
    void tearDown() {
        Page.pages.clear();
        MockBukkit.unmock();
    }

    // --- Constructor and size validation ---

    @Test
    void constructorRegistersPageInStaticList() {
        Page page = new Page(1, "Test", Material.GRAY_STAINED_GLASS_PANE);
        assertTrue(Page.pages.contains(page));
    }

    @Test
    void sizeZeroThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Page(0, "Bad", Material.AIR));
    }

    @Test
    void sizeSevenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Page(7, "Bad", Material.AIR));
    }

    @Test
    void validSizesDoNotThrow() {
        for (int i = 1; i <= 6; i++) {
            int size = i;
            assertDoesNotThrow(() -> new Page(size, "Page " + size, Material.GRAY_STAINED_GLASS_PANE));
        }
    }

    // --- Open and inventory ---

    @Test
    void openCreatesInventory() {
        Page page = new Page(3, "Inventory", Material.GRAY_STAINED_GLASS_PANE);
        Player player = server.addPlayer();
        page.open(player);

        assertNotNull(page.getInventory());
    }

    @Test
    void openedInventoryHasCorrectSize() {
        Page page = new Page(2, "Small", Material.GRAY_STAINED_GLASS_PANE);
        Player player = server.addPlayer();
        page.open(player);

        assertEquals(18, page.getInventory().getSize());
    }

    @Test
    void openFillsWithFillMaterial() {
        Page page = new Page(1, "Filled", Material.BLACK_STAINED_GLASS_PANE);
        Player player = server.addPlayer();
        page.open(player);

        Inventory inv = page.getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (i != 0) {
                assertNotNull(item, "Slot " + i + " should not be null");
            }
        }
    }

    // --- Static lookup ---

    @Test
    void getByInventoryFindsPage() {
        Page page = new Page(1, "Find Me", Material.GRAY_STAINED_GLASS_PANE);
        Player player = server.addPlayer();
        page.open(player);

        Page found = Page.get(page.getInventory());
        assertSame(page, found);
    }

    @Test
    void existsReturnsTrueForOpenPage() {
        Page page = new Page(1, "Exists", Material.GRAY_STAINED_GLASS_PANE);
        Player player = server.addPlayer();
        page.open(player);

        assertTrue(Page.exists(page.getInventory()));
    }

    @Test
    void getByIndexReturnsCorrectPage() {
        Page first = new Page(1, "First", Material.GRAY_STAINED_GLASS_PANE);
        Page second = new Page(2, "Second", Material.GRAY_STAINED_GLASS_PANE);

        assertSame(first, Page.get(0));
        assertSame(second, Page.get(1));
    }

    // --- Item management ---

    @Test
    void removeItemWorks() {
        Page page = new Page(3, "Remove", Material.GRAY_STAINED_GLASS_PANE);
        InventoryItem item = new InventoryItem(4, new ItemStack(Material.DIAMOND), click -> {});
        page.addItem(item);
        page.removeItem(item);

        assertNull(page.getInventoryItem(item.getItem()));
    }

    // --- Extra data ---

    @Test
    void extraDataCanStoreAndRetrieve() {
        Page page = new Page(1, "Data", Material.GRAY_STAINED_GLASS_PANE);
        page.extraData.put("score", 42);
        page.extraData.put("name", "test");

        assertEquals(42, page.extraData.get("score"));
        assertEquals("test", page.extraData.get("name"));
    }

    // --- Default populate ---

    @Test
    void defaultPopulateAddsCloseButton() {
        Page page = new Page(1, "Close", Material.GRAY_STAINED_GLASS_PANE);
        Player player = server.addPlayer();
        page.open(player);

        Inventory inv = page.getInventory();
        ItemStack slot0 = inv.getItem(0);
        assertNotNull(slot0);
        assertEquals(Material.RED_STAINED_GLASS_PANE, slot0.getType());
    }

    // --- Fill material click ---

    @Test
    void getInventoryItemReturnsDummyForFillMaterial() {
        Page page = new Page(1, "Fill", Material.GRAY_STAINED_GLASS_PANE);
        Player player = server.addPlayer();
        page.open(player);

        ItemStack fillStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        InventoryItem result = page.getInventoryItem(fillStack);
        assertNotNull(result);
    }

    @Test
    void getInventoryItemReturnsNullForUnknownItem() {
        Page page = new Page(1, "Unknown", Material.GRAY_STAINED_GLASS_PANE);
        Player player = server.addPlayer();
        page.open(player);

        ItemStack unknown = new ItemStack(Material.BEDROCK);
        InventoryItem result = page.getInventoryItem(unknown);
        assertNull(result);
    }
}
