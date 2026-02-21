package io.github.chafficui.CrucialLib.Utils.customItems;

import io.github.chafficui.CrucialLib.exceptions.CrucialException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * Represents a custom item with a unique ID, crafting recipe, and interaction flags.
 *
 * <p>Each CrucialItem is identified by a {@link UUID} stored in the item's
 * {@link org.bukkit.persistence.PersistentDataContainer}. Items must be
 * {@linkplain #register() registered} before they can be crafted or looked up.
 *
 * <p>Example usage:
 * <pre>{@code
 * String[] recipe = {"AIR","AIR","AIR", "AIR","DIAMOND","AIR", "AIR","AIR","AIR"};
 * CrucialItem sword = new CrucialItem("Sword", Material.DIAMOND_SWORD,
 *     List.of("A sharp blade"), recipe, "weapon", true, true, false);
 * sword.register();
 *
 * // Later, identify an item in a player's hand:
 * CrucialItem found = CrucialItem.getByStack(player.getInventory().getItemInMainHand());
 * }</pre>
 *
 * @see CrucialHead
 * @see Item
 */
public class CrucialItem {
    /** Global registry of all currently registered CrucialItems. */
    public final static Set<CrucialItem> CRUCIAL_ITEMS = new HashSet<>();

    private static JavaPlugin getPlugin() {
        return (JavaPlugin) Bukkit.getPluginManager().getPlugin("CrucialLib");
    }

    static NamespacedKey getIdKey() {
        return new NamespacedKey(getPlugin(), "crucialitem_id");
    }

    /**
     * Stores the given UUID in the item's {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param stack the item stack to tag
     * @param id    the unique identifier to store
     * @return the same stack, with the ID applied
     */
    static ItemStack applyId(ItemStack stack, UUID id) {
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(getIdKey(), PersistentDataType.STRING, id.toString());
            stack.setItemMeta(meta);
        }
        return stack;
    }

    /**
     * Extracts the CrucialItem UUID from an {@link ItemStack}, if present.
     *
     * @param stack the item stack to inspect (may be {@code null})
     * @return the UUID stored on the stack, or {@code null} if the stack is not a CrucialItem
     */
    public static UUID getId(ItemStack stack) {
        if (stack != null && stack.getItemMeta() != null) {
            String idStr = stack.getItemMeta().getPersistentDataContainer().get(getIdKey(), PersistentDataType.STRING);
            if (idStr != null) {
                return UUID.fromString(idStr);
            }
        }
        return null;
    }

    /**
     * Looks up the registered {@link CrucialItem} that matches the given stack.
     *
     * @param stack the item stack to look up
     * @return the matching CrucialItem, or {@code null} if not found or not registered
     */
    public static CrucialItem getByStack(ItemStack stack) {
        UUID id = getId(stack);
        if (id != null) {
            return getById(id);
        }
        return null;
    }

    /**
     * Finds a registered CrucialItem by its UUID.
     *
     * @param id the unique identifier to search for
     * @return the matching CrucialItem, or {@code null} if not registered
     */
    public static CrucialItem getById(UUID id) {
        for (CrucialItem crucialItem : CRUCIAL_ITEMS) {
            if (crucialItem.id.equals(id)) {
                return crucialItem;
            }
        }
        return null;
    }

    //Custom Item
    /** The {@link NamespacedKey} of this item's crafting recipe. Set after {@link #register()}. */
    protected NamespacedKey namespacedKey;
    /** Display name shown on the item. */
    protected String name = "";
    /** Material name (uppercase), e.g. {@code "DIAMOND_SWORD"}. */
    protected String material = "";
    /** Lore lines displayed below the item name. */
    protected List<String> lore = new ArrayList<>();
    /** 9-element array of material names defining the 3x3 shaped crafting recipe. */
    protected String[] recipe = new String[9];
    /** Unique identifier for this item, stored in the {@link org.bukkit.persistence.PersistentDataContainer}. */
    protected final UUID id;
    /** A category or type label (e.g. {@code "weapon"}, {@code "tool"}). */
    protected final String type;
    /** Whether this item is currently registered with the server. */
    protected boolean isRegistered = false;

    /** Whether a crafting recipe should be registered for this item. */
    public boolean isCraftable = true;
    /** Whether players can use (interact with) this item. If {@code false}, interactions are cancelled. */
    public boolean isUsable = true;
    /** Whether this item can be used as an ingredient in other crafting recipes. */
    public boolean isAllowedForCrafting = false;

    /**
     * Creates a minimal CrucialItem with only a type. Use the fluent setters
     * ({@link #setName}, {@link #setMaterial}, {@link #setRecipe}) to configure it
     * before calling {@link #register()}.
     *
     * @param type a category label for this item (e.g. {@code "weapon"})
     */
    public CrucialItem(String type) {
        this.id = UUID.randomUUID();
        this.type = type;
    }

    /**
     * Creates a fully configured CrucialItem.
     *
     * @param name                the display name
     * @param material            the base material
     * @param lore                lore lines (may be empty)
     * @param recipe              9-element array of material names for the shaped recipe
     * @param type                a category label (e.g. {@code "weapon"})
     * @param isCraftable         whether to register a crafting recipe
     * @param isUsable            whether interactions with this item are allowed
     * @param isAllowedForCrafting whether this item can be used as a crafting ingredient
     */
    public CrucialItem(String name, Material material, List<String> lore, String[] recipe, String type, boolean isCraftable, boolean isUsable, boolean isAllowedForCrafting) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.lore = lore;
        this.recipe = recipe;
        this.type = type;
        this.isCraftable = isCraftable;
        this.isUsable = isUsable;
        this.isAllowedForCrafting = isAllowedForCrafting;
        this.material = material.name();
    }

    /**
     * Unregisters this item, removing its crafting recipe from the server
     * and removing it from {@link #CRUCIAL_ITEMS}. Safe to call when not registered.
     */
    public void unregister() {
        if (isRegistered) {
            if (Bukkit.getRecipe(namespacedKey) != null) {
                Bukkit.removeRecipe(namespacedKey);
            }
            CRUCIAL_ITEMS.remove(this);
            isRegistered = false;
        }
    }

    /**
     * Registers this item with the server, adding its crafting recipe and
     * making it discoverable via {@link #getByStack} and {@link #getById}.
     * No-op if already registered.
     *
     * @throws CrucialException if a CrucialItem with this ID is already in the registry (error 007),
     *                          or if the recipe could not be created (error 002)
     */
    public void register() throws CrucialException {
        if (!isRegistered) {
            if (!CRUCIAL_ITEMS.contains(this)) {
                registerRecipe();
                isRegistered = true;
                CRUCIAL_ITEMS.add(this);
            } else {
                throw new CrucialException(7);
            }
        }
    }

    /**
     * Reloads the crafting recipe for this item (removes and re-adds it).
     * Useful after changing properties like name or recipe. No-op if not registered.
     *
     * @throws CrucialException if the recipe could not be recreated
     */
    public void reload() throws CrucialException {
        if (isRegistered) {
            Bukkit.removeRecipe(namespacedKey);
            registerRecipe();
        }
    }

    /**
     * Creates and registers the shaped crafting recipe with the server.
     *
     * @throws CrucialException if the recipe could not be created
     */
    protected void registerRecipe() throws CrucialException {
        ItemStack stack = applyId(Stack.getStack(Material.getMaterial(material), name, lore), this.id);
        namespacedKey = Item.createItem(id + type, name, stack, recipe);
    }

    /**
     * Permanently deletes this item: removes the crafting recipe and unregisters it.
     * Safe to call when not registered.
     */
    public void delete() {
        if (isRegistered) {
            isRegistered = false;
            Bukkit.removeRecipe(namespacedKey);
            CRUCIAL_ITEMS.remove(this);
        }
    }

    /**
     * Creates a new {@link ItemStack} for this item with the correct display name,
     * lore, and embedded CrucialItem UUID.
     *
     * @return a new ItemStack, or {@code null} if this item is not registered
     */
    public ItemStack getItemStack() {
        if (isRegistered) {
            return applyId(Stack.getStack(Material.getMaterial(material), name, lore), this.id);
        }
        return null;
    }

    /**
     * Sets the lore lines for this item.
     *
     * @param lore the lore lines
     */
    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    /**
     * Returns the lore as a string representation.
     *
     * @return string form of the lore list
     */
    public String getLore() {
        return String.valueOf(lore);
    }

    /**
     * Returns the display name of this item.
     *
     * @return the item name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the display name of this item.
     *
     * @param name the new display name
     * @return this instance for fluent chaining
     */
    public CrucialItem setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Returns the material name (e.g. {@code "DIAMOND_SWORD"}).
     *
     * @return the material name string
     */
    public String getMaterial() {
        return material;
    }

    /**
     * Sets the material for this item.
     *
     * @param material the material name (e.g. {@code "DIAMOND_SWORD"})
     * @return this instance for fluent chaining
     */
    public CrucialItem setMaterial(String material) {
        this.material = material;
        return this;
    }

    /**
     * Returns the 9-element crafting recipe array.
     *
     * @return the recipe material names
     */
    public String[] getRecipe() {
        return recipe;
    }

    /**
     * Sets the shaped crafting recipe.
     *
     * @param recipe 9-element array of material names (e.g. {@code "AIR"}, {@code "DIAMOND"})
     * @return this instance for fluent chaining
     */
    public CrucialItem setRecipe(String[] recipe) {
        this.recipe = recipe;
        return this;
    }

    /**
     * Returns the {@link NamespacedKey} for this item's crafting recipe.
     *
     * @return the recipe key, or {@code null} if not yet registered
     */
    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    /**
     * Returns the unique identifier of this CrucialItem.
     *
     * @return the UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the type/category label of this item.
     *
     * @return the type string
     */
    public String getType() {
        return type;
    }

    /**
     * Returns whether this item is currently registered with the server.
     *
     * @return {@code true} if registered
     */
    public boolean isRegistered() {
        return isRegistered;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CrucialItem) {
            CrucialItem crucialItem = (CrucialItem) obj;
            return crucialItem.id == this.id && crucialItem.type.equalsIgnoreCase(this.type);
        }
        return false;
    }
}
