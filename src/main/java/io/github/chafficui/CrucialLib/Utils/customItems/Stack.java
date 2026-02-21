package io.github.chafficui.CrucialLib.Utils.customItems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

/**
 * Utility class for building {@link ItemStack} instances with display names, lore,
 * enchantments, and attribute modifiers.
 *
 * <p>All stacks produced by this class hide attribute, enchant, destroy, placed-on,
 * additional tooltip, and unbreakable flags by default, resulting in a clean tooltip
 * for the player.</p>
 */
public class Stack {

    /**
     * Creates a basic {@link ItemStack} from the given material.
     *
     * @param material the material type for the stack, or {@code null} to get an AIR stack
     * @return an {@link ItemStack} of the specified material, or {@link Material#AIR} if the material is {@code null}
     */
    public static ItemStack getStack(Material material){
        if(material!=null){
            return new ItemStack(material);
        }
        return new ItemStack(Material.AIR);
    }

    /**
     * Creates an {@link ItemStack} with a custom display name.
     *
     * @param material the material type for the stack
     * @param name     the display name to set on the item
     * @return an {@link ItemStack} with the given material and display name, with hidden tooltip flags applied
     */
    public static ItemStack getStack(Material material, String name){
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        return getCleanMeta(stack, meta);
    }

    /**
     * Creates an {@link ItemStack} with a custom display name and lore.
     *
     * @param material the material type for the stack
     * @param name     the display name to set on the item
     * @param lore     the lore lines to set on the item
     * @return an {@link ItemStack} with the given material, display name, and lore, with hidden tooltip flags applied
     */
    public static ItemStack getStack(Material material, String name, List<String> lore){
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(lore);
        return getCleanMeta(stack, meta);
    }

    /**
     * Creates a player head {@link ItemStack} whose skin is determined by the given player UUID.
     *
     * @param uuid the {@link UUID} of the player whose head skin should be used
     * @param name the display name to set on the head item
     * @param lore the lore lines to set on the head item
     * @return a {@link Material#PLAYER_HEAD} {@link ItemStack} with the specified owner, display name, and lore
     */
    public static ItemStack getStack(UUID uuid, String name, List<String> lore){
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        assert meta != null;
        getCleanMeta(stack, meta);
        meta = (SkullMeta) stack.getItemMeta();
        meta.setDisplayName(name);
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        meta.setLore(lore);
        stack.setItemMeta(meta);

        return stack;
    }

    /**
     * Creates an {@link ItemStack} with a custom display name, lore, and an optional glow effect.
     *
     * <p>When {@code shiny} is {@code true}, an {@link Enchantment#UNBREAKING} enchantment at
     * level 0 is added to produce a visual glow effect. Because enchantment tooltips are hidden
     * by default, the enchantment itself is not visible to the player.</p>
     *
     * @param material the material type for the stack
     * @param name     the display name to set on the item
     * @param lore     the lore lines to set on the item
     * @param shiny    {@code true} to add an Unbreaking enchantment for a glow effect, {@code false} for no glow
     * @return an {@link ItemStack} with the given material, display name, lore, and optional glow, with hidden tooltip flags applied
     */
    public static ItemStack getStack(Material material, String name, List<String> lore, boolean shiny){
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(lore);
        if(shiny)
            meta.addEnchant(Enchantment.UNBREAKING, 0, true);
        return getCleanMeta(stack, meta);
    }

    /**
     * @deprecated AttributeModifier names and UUIDs are unreliable on Bukkit 1.21+ due to
     * the deprecation of {@code AttributeModifier(UUID, String, double, Operation)} and
     * {@code Attribute.MAX_HEALTH}. Use {@link CrucialItem} for custom item identification
     * or {@link io.github.chafficui.CrucialLib.Utils.player.inventory.InventoryItem} for
     * inventory GUI items instead.
     */
    @Deprecated(since = "3.0.0")
    public static ItemStack addAttributeModifier(ItemStack stack, Attribute attribute, AttributeModifier modifier) {
        if(stack == null || stack.getType().equals(Material.AIR) || attribute == null || modifier == null) {
            return stack;
        }
        ItemMeta meta = stack.getItemMeta();
        meta.addAttributeModifier(attribute, modifier);
        return getCleanMeta(stack, meta);
    }

    /**
     * Applies hidden item flags to the given meta and sets it on the stack.
     */
    private static ItemStack getCleanMeta(ItemStack stack, ItemMeta meta) {
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        stack.setItemMeta(meta);
        return stack;
    }
}
