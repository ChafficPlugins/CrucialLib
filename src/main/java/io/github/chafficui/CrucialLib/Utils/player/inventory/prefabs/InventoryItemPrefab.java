package io.github.chafficui.CrucialLib.Utils.player.inventory.prefabs;

import io.github.chafficui.CrucialLib.Utils.player.inventory.InventoryItem;

/**
 * A prefab that produces a group of {@link InventoryItem}s to be added to a {@link io.github.chafficui.CrucialLib.Utils.player.inventory.Page}.
 * Implementations define reusable UI component patterns (e.g., toggle buttons, yes/no pairs).
 *
 * @see io.github.chafficui.CrucialLib.Utils.player.inventory.Page#addItems(InventoryItemPrefab)
 * @see TogglePrefab
 * @see YesNoButtonsPrefab
 */
public interface InventoryItemPrefab {
    /**
     * Returns the array of {@link InventoryItem}s produced by this prefab.
     * The returned items should be added to a page via
     * {@link io.github.chafficui.CrucialLib.Utils.player.inventory.Page#addItems(InventoryItemPrefab)}.
     *
     * @return an array of inventory items representing this prefab's UI components
     */
    public InventoryItem[] getItems();
}
