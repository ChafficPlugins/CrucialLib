package io.github.chafficui.CrucialLib.Utils.customItems;

import io.github.chafficui.CrucialLib.Main;
import io.github.chafficui.CrucialLib.exceptions.CrucialException;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CrucialItemTest {

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

    // --- Constructor tests ---

    @Test
    void constructorWithTypeSetsId() {
        CrucialItem item = new CrucialItem("weapon");
        assertNotNull(item.getId());
        assertEquals("weapon", item.getType());
    }

    @Test
    void constructorWithAllParamsSetsFields() {
        List<String> lore = Arrays.asList("Lore line");
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Sword", Material.DIAMOND_SWORD, lore, recipe, "weapon", true, true, false);

        assertEquals("Sword", item.getName());
        assertEquals("DIAMOND_SWORD", item.getMaterial());
        assertEquals("weapon", item.getType());
        assertNotNull(item.getId());
    }

    @Test
    void defaultFieldValues() {
        CrucialItem item = new CrucialItem("item");
        assertTrue(item.isCraftable);
        assertTrue(item.isUsable);
        assertFalse(item.isAllowedForCrafting);
    }

    @Test
    void fullConstructorSetsFlags() {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Item", Material.STONE, List.of(), recipe, "tool", false, false, true);

        assertFalse(item.isCraftable);
        assertFalse(item.isUsable);
        assertTrue(item.isAllowedForCrafting);
    }

    @Test
    void eachItemGetsUniqueId() {
        CrucialItem a = new CrucialItem("type");
        CrucialItem b = new CrucialItem("type");
        assertNotEquals(a.getId(), b.getId());
    }

    // --- Setter / getter chain tests ---

    @Test
    void setNameReturnsThis() {
        CrucialItem item = new CrucialItem("item");
        CrucialItem returned = item.setName("Test");
        assertSame(item, returned);
        assertEquals("Test", item.getName());
    }

    @Test
    void setMaterialReturnsThis() {
        CrucialItem item = new CrucialItem("item");
        CrucialItem returned = item.setMaterial("STONE");
        assertSame(item, returned);
        assertEquals("STONE", item.getMaterial());
    }

    @Test
    void setRecipeReturnsThis() {
        CrucialItem item = new CrucialItem("item");
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem returned = item.setRecipe(recipe);
        assertSame(item, returned);
        assertArrayEquals(recipe, item.getRecipe());
    }

    @Test
    void setAndGetLore() {
        CrucialItem item = new CrucialItem("item");
        List<String> lore = Arrays.asList("Line 1", "Line 2");
        item.setLore(lore);
        assertEquals("[Line 1, Line 2]", item.getLore());
    }

    @Test
    void fluentBuilderChaining() {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "STONE", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("tool")
                .setName("Pick")
                .setMaterial("STONE")
                .setRecipe(recipe);

        assertEquals("Pick", item.getName());
        assertEquals("STONE", item.getMaterial());
        assertArrayEquals(recipe, item.getRecipe());
    }

    // --- Registration lifecycle tests ---

    @Test
    void isRegisteredDefaultsFalse() {
        CrucialItem item = new CrucialItem("item");
        assertFalse(item.isRegistered());
    }

    @Test
    void registerAddsToGlobalSetAndSetsRegistered() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Sword", Material.DIAMOND_SWORD, List.of("A sharp blade"), recipe, "weapon", true, true, false);
        item.register();

        assertTrue(item.isRegistered());
        assertTrue(CrucialItem.CRUCIAL_ITEMS.contains(item));
    }

    @Test
    void registerCreatesNamespacedKey() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "IRON_INGOT", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Axe", Material.IRON_AXE, List.of(), recipe, "tool", true, true, false);
        item.register();

        assertNotNull(item.getNamespacedKey());
    }

    @Test
    void registerTwiceIsNoOp() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Sword", Material.DIAMOND_SWORD, List.of(), recipe, "weapon", true, true, false);
        item.register();
        item.register(); // should be no-op, not throw

        assertTrue(item.isRegistered());
        assertEquals(1, CrucialItem.CRUCIAL_ITEMS.size());
    }

    @Test
    void unregisterRemovesFromGlobalSetAndClearsRegistered() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Sword", Material.DIAMOND_SWORD, List.of(), recipe, "weapon", true, true, false);
        item.register();
        item.unregister();

        assertFalse(item.isRegistered());
        assertFalse(CrucialItem.CRUCIAL_ITEMS.contains(item));
    }

    @Test
    void deleteRemovesFromGlobalSetAndClearsRegistered() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "GOLD_INGOT", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Ring", Material.GOLD_INGOT, List.of(), recipe, "accessory", true, true, false);
        item.register();
        item.delete();

        assertFalse(item.isRegistered());
        assertFalse(CrucialItem.CRUCIAL_ITEMS.contains(item));
    }

    @Test
    void deleteWhenNotRegisteredDoesNothing() {
        CrucialItem item = new CrucialItem("item");
        assertDoesNotThrow(item::delete);
    }

    @Test
    void unregisterWhenNotRegisteredDoesNothing() {
        CrucialItem item = new CrucialItem("item");
        assertDoesNotThrow(item::unregister);
    }

    @Test
    void reRegisterAfterUnregister() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Sword", Material.DIAMOND_SWORD, List.of(), recipe, "weapon", true, true, false);
        item.register();
        item.unregister();
        item.register();

        assertTrue(item.isRegistered());
        assertTrue(CrucialItem.CRUCIAL_ITEMS.contains(item));
    }

    @Test
    void reloadReRegistersRecipe() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Sword", Material.DIAMOND_SWORD, List.of(), recipe, "weapon", true, true, false);
        item.register();

        item.setName("Super Sword");
        assertDoesNotThrow(item::reload);
        assertTrue(item.isRegistered());
    }

    @Test
    void reloadWhenNotRegisteredIsNoOp() {
        CrucialItem item = new CrucialItem("item");
        assertDoesNotThrow(() -> item.reload());
        assertFalse(item.isRegistered());
    }

    @Test
    void multipleItemsCanBeRegistered() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem sword = new CrucialItem("Sword", Material.DIAMOND_SWORD, List.of(), recipe, "weapon", true, true, false);
        String[] recipe2 = new String[]{"AIR", "AIR", "AIR", "AIR", "IRON_INGOT", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem axe = new CrucialItem("Axe", Material.IRON_AXE, List.of(), recipe2, "tool", true, true, false);
        sword.register();
        axe.register();

        assertEquals(2, CrucialItem.CRUCIAL_ITEMS.size());
        assertTrue(sword.isRegistered());
        assertTrue(axe.isRegistered());
    }

    // --- ItemStack and ID extraction tests ---

    @Test
    void getItemStackWhenNotRegisteredReturnsNull() {
        CrucialItem item = new CrucialItem("item");
        assertNull(item.getItemStack());
    }

    @Test
    void getItemStackWhenRegisteredReturnsStackWithCorrectMaterial() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Sword", Material.DIAMOND_SWORD, List.of("A sharp blade"), recipe, "weapon", true, true, false);
        item.register();

        ItemStack stack = item.getItemStack();
        assertNotNull(stack);
        assertEquals(Material.DIAMOND_SWORD, stack.getType());
    }

    @Test
    void getItemStackHasDisplayName() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Sword", Material.DIAMOND_SWORD, List.of("Lore"), recipe, "weapon", true, true, false);
        item.register();

        ItemStack stack = item.getItemStack();
        assertNotNull(stack.getItemMeta());
        assertEquals("Sword", stack.getItemMeta().getDisplayName());
    }

    @Test
    void getItemStackHasLore() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Sword", Material.DIAMOND_SWORD, List.of("Sharp", "Deadly"), recipe, "weapon", true, true, false);
        item.register();

        ItemStack stack = item.getItemStack();
        List<String> lore = stack.getItemMeta().getLore();
        assertNotNull(lore);
        assertEquals(2, lore.size());
        assertEquals("Sharp", lore.get(0));
        assertEquals("Deadly", lore.get(1));
    }

    @Test
    void getItemStackHasPersistentDataContainerId() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Sword", Material.DIAMOND_SWORD, List.of(), recipe, "weapon", true, true, false);
        item.register();

        ItemStack stack = item.getItemStack();
        assertNotNull(stack.getItemMeta());
        // The item should have a PDC entry used for identification
        UUID extractedId = CrucialItem.getId(stack);
        assertNotNull(extractedId);
        assertEquals(item.getId(), extractedId);
    }

    @Test
    void getByIdFindsRegisteredItem() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Sword", Material.DIAMOND_SWORD, List.of(), recipe, "weapon", true, true, false);
        item.register();

        CrucialItem found = CrucialItem.getById(item.getId());
        assertNotNull(found);
        assertSame(item, found);
    }

    @Test
    void getByIdReturnsNullWhenNotFound() {
        assertNull(CrucialItem.getById(UUID.randomUUID()));
    }

    @Test
    void getByStackReturnsNullForStackWithoutPDCId() {
        // A plain stack without PDC data should not be recognized as a CrucialItem
        ItemStack stack = Stack.getStack(Material.STONE, "Stone");
        assertNull(CrucialItem.getByStack(stack));
    }

    @Test
    void getByStackReturnsNullForPlainStack() {
        ItemStack stack = new ItemStack(Material.STONE);
        assertNull(CrucialItem.getByStack(stack));
    }

    @Test
    void getByStackReturnsNullForNull() {
        assertNull(CrucialItem.getId(null));
    }

    @Test
    void getByIdReturnsNullAfterUnregister() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Sword", Material.DIAMOND_SWORD, List.of(), recipe, "weapon", true, true, false);
        item.register();
        UUID id = item.getId();
        item.unregister();

        assertNull(CrucialItem.getById(id));
    }

    // --- Round-trip ID identification tests ---

    @Test
    void getIdFromRegisteredItemStack() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Sword", Material.DIAMOND_SWORD, List.of(), recipe, "weapon", true, true, false);
        item.register();

        ItemStack stack = item.getItemStack();
        UUID extractedId = CrucialItem.getId(stack);
        assertNotNull(extractedId, "getId should extract the UUID from a registered item's stack");
        assertEquals(item.getId(), extractedId, "Extracted ID should match the original item's ID");
    }

    @Test
    void getByStackFindsRegisteredItem() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Sword", Material.DIAMOND_SWORD, List.of(), recipe, "weapon", true, true, false);
        item.register();

        ItemStack stack = item.getItemStack();
        CrucialItem found = CrucialItem.getByStack(stack);
        assertNotNull(found, "getByStack should find the registered item");
        assertSame(item, found, "getByStack should return the same instance");
    }

    @Test
    void getByStackDistinguishesMultipleItems() throws CrucialException {
        String[] recipe1 = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem sword = new CrucialItem("Sword", Material.DIAMOND_SWORD, List.of(), recipe1, "weapon", true, true, false);
        String[] recipe2 = new String[]{"AIR", "AIR", "AIR", "AIR", "IRON_INGOT", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem axe = new CrucialItem("Axe", Material.IRON_AXE, List.of(), recipe2, "tool", true, true, false);
        sword.register();
        axe.register();

        assertSame(sword, CrucialItem.getByStack(sword.getItemStack()));
        assertSame(axe, CrucialItem.getByStack(axe.getItemStack()));
        assertNotEquals(CrucialItem.getId(sword.getItemStack()), CrucialItem.getId(axe.getItemStack()));
    }

    @Test
    void getIdReturnsNullForStackWithoutPDC() {
        ItemStack stack = new ItemStack(Material.DIAMOND_SWORD);
        assertNull(CrucialItem.getId(stack), "Plain stack without PDC data should return null");
    }

    @Test
    void getIdReturnsNullForStackWithNullMeta() {
        ItemStack stack = new ItemStack(Material.AIR);
        assertNull(CrucialItem.getId(stack));
    }

    @Test
    void getByStackReturnsNullWhenIdExistsButItemNotRegistered() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Sword", Material.DIAMOND_SWORD, List.of(), recipe, "weapon", true, true, false);
        item.register();

        ItemStack stack = item.getItemStack();
        item.unregister();

        UUID extractedId = CrucialItem.getId(stack);
        assertNotNull(extractedId, "PDC data should survive on the stack even after unregister");
        assertNull(CrucialItem.getByStack(stack), "getByStack should return null when the item is no longer registered");
    }

    @Test
    void multipleGetItemStackCallsReturnConsistentId() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialItem item = new CrucialItem("Sword", Material.DIAMOND_SWORD, List.of(), recipe, "weapon", true, true, false);
        item.register();

        ItemStack stack1 = item.getItemStack();
        ItemStack stack2 = item.getItemStack();
        assertEquals(CrucialItem.getId(stack1), CrucialItem.getId(stack2),
                "Multiple getItemStack calls should produce stacks with the same ID");
    }

    // --- Equality tests ---

    @Test
    void equalsReturnsFalseForNonCrucialItem() {
        CrucialItem item = new CrucialItem("item");
        assertNotEquals("not a crucial item", item);
    }

    @Test
    void twoDistinctItemsAreNotEqual() {
        CrucialItem a = new CrucialItem("weapon");
        CrucialItem b = new CrucialItem("weapon");
        assertNotEquals(a, b);
    }
}
