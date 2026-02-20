package io.github.chafficui.CrucialLib.Utils.customItems;

import io.github.chafficui.CrucialLib.Main;
import io.github.chafficui.CrucialLib.exceptions.CrucialException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

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
    void createItemReturnsNamespacedKey() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        NamespacedKey key = Item.createItem("testkey", "TestItem", List.of("Lore"), Material.DIAMOND, recipe);

        assertNotNull(key);
    }

    @Test
    void createItemRegistersRecipeWithBukkit() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "IRON_INGOT", "AIR", "AIR", "AIR", "AIR"};
        NamespacedKey key = Item.createItem("recipetest", "RecipeItem", List.of(), Material.IRON_SWORD, recipe);

        Recipe found = Bukkit.getRecipe(key);
        assertNotNull(found);
        assertInstanceOf(ShapedRecipe.class, found);
    }

    @Test
    void createItemRecipeHasCorrectResult() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "GOLD_INGOT", "AIR", "AIR", "AIR", "AIR"};
        NamespacedKey key = Item.createItem("goldtest", "GoldItem", List.of(), Material.GOLD_INGOT, recipe);

        ShapedRecipe shaped = (ShapedRecipe) Bukkit.getRecipe(key);
        assertNotNull(shaped);
        assertEquals(Material.GOLD_INGOT, shaped.getResult().getType());
    }

    @Test
    void createItemSanitizesSpacesInKey() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        NamespacedKey key = Item.createItem("my key", "My Item", List.of(), Material.DIAMOND, recipe);

        assertNotNull(key);
        assertFalse(key.getKey().contains(" "));
    }

    @Test
    void createItemFromPrebuiltStack() throws CrucialException {
        ItemStack stack = Stack.getStack(Material.EMERALD, "Emerald Tool", List.of("Shiny"));
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "EMERALD", "AIR", "AIR", "AIR", "AIR"};
        NamespacedKey key = Item.createItem("emeraldkey", "EmeraldTool", stack, recipe);

        assertNotNull(key);
        Recipe found = Bukkit.getRecipe(key);
        assertNotNull(found);
    }

    @Test
    void createHeadReturnsNamespacedKey() throws CrucialException {
        UUID owner = UUID.randomUUID();
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        NamespacedKey key = Item.createHead("headkey", "HeadItem", List.of(), owner, recipe);

        assertNotNull(key);
        Recipe found = Bukkit.getRecipe(key);
        assertNotNull(found);
    }

    @Test
    void removeRecipeByKey() throws CrucialException {
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        NamespacedKey key = Item.createItem("removetest", "RemoveItem", List.of(), Material.DIAMOND, recipe);

        assertNotNull(Bukkit.getRecipe(key));
        Bukkit.removeRecipe(key);
        assertNull(Bukkit.getRecipe(key));
    }
}
