package io.github.chafficui.CrucialLib.Utils.customItems;

import io.github.chafficui.CrucialLib.Main;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.Arrays;
import java.util.List;

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
    }

    @Test
    void addAttributeModifierToStack() {
        ItemStack stack = Stack.getStack(Material.DIAMOND_SWORD, "Sword");
        AttributeModifier modifier = new AttributeModifier("test", 1.0, AttributeModifier.Operation.ADD_NUMBER);
        ItemStack result = Stack.addAttributeModifier(stack, Attribute.MAX_HEALTH, modifier);
        assertNotNull(result);
        assertNotNull(result.getItemMeta());
        assertNotNull(result.getItemMeta().getAttributeModifiers());
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
}
