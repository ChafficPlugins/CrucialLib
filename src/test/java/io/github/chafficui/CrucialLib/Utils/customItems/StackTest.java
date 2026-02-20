package io.github.chafficui.CrucialLib.Utils.customItems;

import io.github.chafficui.CrucialLib.Main;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StackTest {

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

    // --- Basic stack creation ---

    @Test
    void getStackWithMaterialReturnsCorrectType() {
        ItemStack stack = Stack.getStack(Material.DIAMOND);
        assertNotNull(stack);
        assertEquals(Material.DIAMOND, stack.getType());
    }

    @Test
    void getStackWithNullMaterialReturnsAir() {
        ItemStack stack = Stack.getStack(null);
        assertEquals(Material.AIR, stack.getType());
    }

    // --- Display name and lore ---

    @Test
    void getStackWithNameSetsDisplayName() {
        ItemStack stack = Stack.getStack(Material.STONE, "Test Item");
        assertNotNull(stack);
        assertNotNull(stack.getItemMeta());
        assertEquals("Test Item", stack.getItemMeta().getDisplayName());
    }

    @Test
    void getStackWithNameAndLore() {
        List<String> lore = Arrays.asList("Line 1", "Line 2");
        ItemStack stack = Stack.getStack(Material.IRON_SWORD, "My Sword", lore);
        assertNotNull(stack);
        ItemMeta meta = stack.getItemMeta();
        assertNotNull(meta);
        assertEquals("My Sword", meta.getDisplayName());
        assertNotNull(meta.getLore());
        assertEquals(2, meta.getLore().size());
        assertEquals("Line 1", meta.getLore().get(0));
        assertEquals("Line 2", meta.getLore().get(1));
    }

    @Test
    void getStackWithEmptyLore() {
        ItemStack stack = Stack.getStack(Material.STONE, "Stone", List.of());
        assertNotNull(stack.getItemMeta());
        // MockBukkit may return null for empty lore lists
        var lore = stack.getItemMeta().getLore();
        assertTrue(lore == null || lore.isEmpty());
    }

    // --- Shiny flag (enchantment) ---

    @Test
    void getStackWithShinyTrueAddsEnchantment() {
        ItemStack stack = Stack.getStack(Material.DIAMOND_SWORD, "Shiny Sword", List.of("Glowing"), true);
        ItemMeta meta = stack.getItemMeta();
        assertNotNull(meta);
        assertTrue(meta.hasEnchant(Enchantment.UNBREAKING));
    }

    @Test
    void getStackWithShinyFalseNoEnchantment() {
        ItemStack stack = Stack.getStack(Material.DIAMOND_SWORD, "Normal Sword", List.of("Dull"), false);
        ItemMeta meta = stack.getItemMeta();
        assertNotNull(meta);
        assertFalse(meta.hasEnchant(Enchantment.UNBREAKING));
    }

    // --- Item flags ---

    @Test
    void stackHasHiddenAttributeFlags() {
        ItemStack stack = Stack.getStack(Material.STONE, "Stone");
        ItemMeta meta = stack.getItemMeta();
        assertNotNull(meta);
        assertTrue(meta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES));
        assertTrue(meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS));
        assertTrue(meta.hasItemFlag(ItemFlag.HIDE_DESTROYS));
        assertTrue(meta.hasItemFlag(ItemFlag.HIDE_PLACED_ON));
        assertTrue(meta.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE));
    }

    @Test
    void stackWithLoreHasHiddenFlags() {
        ItemStack stack = Stack.getStack(Material.IRON_SWORD, "Sword", List.of("Lore"));
        ItemMeta meta = stack.getItemMeta();
        assertTrue(meta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES));
        assertTrue(meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS));
    }

    // --- Player head creation ---

    @Test
    void getStackWithUuidCreatesPlayerHead() {
        UUID owner = UUID.randomUUID();
        ItemStack stack = Stack.getStack(owner, "Head", List.of("Owner's head"));
        assertNotNull(stack);
        assertEquals(Material.PLAYER_HEAD, stack.getType());
    }

    @Test
    void playerHeadHasDisplayName() {
        UUID owner = UUID.randomUUID();
        ItemStack stack = Stack.getStack(owner, "Trophy Head", List.of());
        assertEquals("Trophy Head", stack.getItemMeta().getDisplayName());
    }

    @Test
    void playerHeadHasLore() {
        UUID owner = UUID.randomUUID();
        ItemStack stack = Stack.getStack(owner, "Head", List.of("Rare", "Unique"));
        List<String> lore = stack.getItemMeta().getLore();
        assertNotNull(lore);
        assertEquals(2, lore.size());
        assertEquals("Rare", lore.get(0));
    }

    // --- Attribute modifier ---

    @Test
    void addAttributeModifierToStack() {
        ItemStack stack = Stack.getStack(Material.DIAMOND_SWORD, "Sword");
        AttributeModifier modifier = new AttributeModifier("test", 1.0, AttributeModifier.Operation.ADD_NUMBER);
        ItemStack result = Stack.addAttributeModifier(stack, Attribute.MAX_HEALTH, modifier);
        assertNotNull(result);
        assertNotNull(result.getItemMeta());
        assertNotNull(result.getItemMeta().getAttributeModifiers());
        assertTrue(result.getItemMeta().getAttributeModifiers().containsKey(Attribute.MAX_HEALTH));
    }

    @Test
    void addAttributeModifierToNullReturnsNull() {
        AttributeModifier modifier = new AttributeModifier("test", 1.0, AttributeModifier.Operation.ADD_NUMBER);
        ItemStack result = Stack.addAttributeModifier(null, Attribute.MAX_HEALTH, modifier);
        assertNull(result);
    }

    @Test
    void addAttributeModifierToAirReturnsAir() {
        ItemStack air = new ItemStack(Material.AIR);
        AttributeModifier modifier = new AttributeModifier("test", 1.0, AttributeModifier.Operation.ADD_NUMBER);
        ItemStack result = Stack.addAttributeModifier(air, Attribute.MAX_HEALTH, modifier);
        assertEquals(Material.AIR, result.getType());
    }

    @Test
    void addAttributeModifierWithNullAttributeReturnsStack() {
        ItemStack stack = Stack.getStack(Material.STONE, "Stone");
        AttributeModifier modifier = new AttributeModifier("test", 1.0, AttributeModifier.Operation.ADD_NUMBER);
        ItemStack result = Stack.addAttributeModifier(stack, null, modifier);
        assertNotNull(result);
    }

    @Test
    void addAttributeModifierWithNullModifierReturnsStack() {
        ItemStack stack = Stack.getStack(Material.STONE, "Stone");
        ItemStack result = Stack.addAttributeModifier(stack, Attribute.MAX_HEALTH, null);
        assertNotNull(result);
    }

    @Test
    void addAttributeModifierPreservesExistingMeta() {
        ItemStack stack = Stack.getStack(Material.DIAMOND_SWORD, "My Sword", List.of("Sharp"));
        AttributeModifier modifier = new AttributeModifier("test", 2.0, AttributeModifier.Operation.ADD_NUMBER);
        ItemStack result = Stack.addAttributeModifier(stack, Attribute.MAX_HEALTH, modifier);

        assertEquals("My Sword", result.getItemMeta().getDisplayName());
        assertNotNull(result.getItemMeta().getLore());
        assertEquals("Sharp", result.getItemMeta().getLore().get(0));
    }
}
