package io.github.chafficui.CrucialLib.Utils.player.inventory;

import io.github.chafficui.CrucialLib.Utils.customItems.Stack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

/**
 * Represents a clickable item in a custom GUI inventory ({@link Page}).
 * Each item occupies a slot, has an optional click action, and can be marked as movable or immovable.
 * Items are tagged with a {@link org.bukkit.persistence.PersistentDataContainer} marker for identification.
 *
 * @see Page
 * @see InventoryClick
 * @see Action
 */
public class InventoryItem {
    private static JavaPlugin getPlugin() {
        return (JavaPlugin) Bukkit.getPluginManager().getPlugin("CrucialLib");
    }

    private static NamespacedKey getMarkerKey() {
        return new NamespacedKey(getPlugin(), "inventoryitem");
    }

    private static ItemStack applyMarker(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(getMarkerKey(), PersistentDataType.BYTE, (byte) 1);
            stack.setItemMeta(meta);
        }
        return stack;
    }

    /**
     * Checks whether the given {@link ItemStack} is tagged as an InventoryItem
     * by inspecting its {@link org.bukkit.persistence.PersistentDataContainer} for the marker key.
     *
     * @param stack the item stack to check; may be {@code null}
     * @return {@code true} if the stack carries the InventoryItem marker, {@code false} otherwise
     */
    public static boolean isInventoryItem(ItemStack stack) {
        if (stack != null && stack.getItemMeta() != null) {
            return stack.getItemMeta().getPersistentDataContainer().has(getMarkerKey(), PersistentDataType.BYTE);
        }
        return false;
    }

    private final ItemStack stack;
    private final int slot;
    private final Action action;
    public final HashMap<String, Object> extraData = new HashMap<>();
    public final boolean isMovable;

    /**
     * Creates an immovable inventory item from a material, display name, lore, and click action.
     *
     * @param slot     the inventory slot index this item occupies
     * @param material the material type for the item
     * @param name     the display name of the item
     * @param lore     the lore lines for the item
     * @param action   the action to execute when the item is clicked
     */
    public InventoryItem(int slot, Material material, String name, List<String> lore, Action action) {
        this.stack = applyMarker(Stack.getStack(material, name, lore));
        this.action = action;
        this.slot = slot;
        this.isMovable = false;
    }

    /**
     * Creates an inventory item from a material, display name, lore, and click action,
     * with configurable movability.
     *
     * @param slot      the inventory slot index this item occupies
     * @param material  the material type for the item
     * @param name      the display name of the item
     * @param lore      the lore lines for the item
     * @param action    the action to execute when the item is clicked
     * @param isMovable whether the player can move this item in the inventory
     */
    public InventoryItem(int slot, Material material, String name, List<String> lore, Action action, boolean isMovable) {
        this.stack = applyMarker(Stack.getStack(material, name, lore));
        this.action = action;
        this.slot = slot;
        this.isMovable = isMovable;
    }

    /**
     * Creates an immovable inventory item from an existing {@link ItemStack} with a click action.
     *
     * @param slot   the inventory slot index this item occupies
     * @param stack  the item stack to display
     * @param action the action to execute when the item is clicked
     */
    public InventoryItem(int slot, ItemStack stack, Action action) {
        this.slot = slot;
        this.stack = applyMarker(stack);
        this.action = action;
        this.isMovable = false;
    }

    /**
     * Creates an inventory item from an existing {@link ItemStack} with a click action
     * and configurable movability.
     *
     * @param slot      the inventory slot index this item occupies
     * @param stack     the item stack to display
     * @param action    the action to execute when the item is clicked
     * @param isMovable whether the player can move this item in the inventory
     */
    public InventoryItem(int slot, ItemStack stack, Action action, boolean isMovable) {
        this.slot = slot;
        this.stack = applyMarker(stack);
        this.action = action;
        this.isMovable = isMovable;
    }

    /**
     * Creates an immovable inventory item from an existing {@link ItemStack} with no click action.
     *
     * @param slot  the inventory slot index this item occupies
     * @param stack the item stack to display
     */
    public InventoryItem(int slot, ItemStack stack) {
        this.slot = slot;
        this.stack = applyMarker(stack);
        this.action = click -> {};
        this.isMovable = false;
    }

    /**
     * Creates an inventory item from an existing {@link ItemStack} with no click action
     * and configurable movability.
     *
     * @param slot      the inventory slot index this item occupies
     * @param stack     the item stack to display
     * @param isMovable whether the player can move this item in the inventory
     */
    public InventoryItem(int slot, ItemStack stack, boolean isMovable) {
        this.slot = slot;
        this.stack = applyMarker(stack);
        this.action = click -> {};
        this.isMovable = isMovable;
    }

    /**
     * Creates an immovable placeholder item using a white stained glass pane with no click action.
     *
     * @param slot the inventory slot index this item occupies
     */
    public InventoryItem(int slot) {
        this.slot = slot;
        this.stack = applyMarker(new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
        this.action = click -> {};
        this.isMovable = false;
    }

    /**
     * Creates a placeholder item using a white stained glass pane with no click action
     * and configurable movability.
     *
     * @param slot      the inventory slot index this item occupies
     * @param isMovable whether the player can move this item in the inventory
     */
    public InventoryItem(int slot, boolean isMovable) {
        this.slot = slot;
        this.stack = applyMarker(new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
        this.action = click -> {};
        this.isMovable = isMovable;
    }

    /**
     * Returns the inventory slot index this item occupies.
     *
     * @return the slot index
     */
    public int getSlot() {
        return slot;
    }

    /**
     * Returns the {@link Material} type of this item's underlying stack.
     *
     * @return the material type
     */
    public Material getMaterial() {
        return stack.getType();
    }

    /**
     * Returns the underlying {@link ItemStack} for this inventory item,
     * including the PersistentDataContainer marker.
     *
     * @return the item stack
     */
    public ItemStack getItem() {
        return stack;
    }

    /**
     * Returns the display name of this item, or an empty string if the item has no meta.
     *
     * @return the display name, or {@code ""} if unavailable
     */
    public String getName() {
        ItemMeta meta = stack.getItemMeta();
        if(meta != null) {
            return meta.getDisplayName();
        }
        return "";
    }

    /**
     * Returns the lore lines of this item, or {@code null} if the item has no meta.
     *
     * @return the lore as a list of strings, or {@code null} if unavailable
     */
    public List<String> getLore() {
        ItemMeta meta = stack.getItemMeta();
        if(meta != null) {
            return meta.getLore();
        }
        return null;
    }

    /**
     * Executes this item's click action with the given click context.
     *
     * @param click the click event context
     * @see Action
     */
    public void execute(InventoryClick click) {
        action.run(click);
    }

    /**
     * Functional interface representing a callback to be executed when an {@link InventoryItem} is clicked.
     *
     * @see InventoryClick
     */
    public interface Action {
        /**
         * Called when the associated inventory item is clicked.
         *
         * @param click the click event context containing the player, page, and event details
         */
        void run(InventoryClick click);
    }
}
