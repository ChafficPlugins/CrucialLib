package io.github.chafficui.CrucialLib.Utils.customItems;

import io.github.chafficui.CrucialLib.Main;
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
    void isRegisteredDefaultsFalse() {
        CrucialItem item = new CrucialItem("item");
        assertFalse(item.isRegistered());
    }

    @Test
    void getItemStackWhenNotRegisteredReturnsNull() {
        CrucialItem item = new CrucialItem("item");
        assertNull(item.getItemStack());
    }

    @Test
    void getByIdReturnsNullWhenNotFound() {
        assertNull(CrucialItem.getById(UUID.randomUUID()));
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
    void equalsReturnsFalseForNonCrucialItem() {
        CrucialItem item = new CrucialItem("item");
        assertNotEquals("not a crucial item", item);
    }

    @Test
    void defaultFieldValues() {
        CrucialItem item = new CrucialItem("item");
        assertTrue(item.isCraftable);
        assertTrue(item.isUsable);
        assertFalse(item.isAllowedForCrafting);
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
}
