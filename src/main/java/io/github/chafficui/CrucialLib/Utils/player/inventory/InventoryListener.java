package io.github.chafficui.CrucialLib.Utils.player.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

/**
 * Internal Bukkit event listener that routes inventory click, pickup, and move events
 * to the appropriate {@link Page}. This listener is registered automatically by CrucialLib
 * during plugin initialization and should not be registered manually.
 *
 * @see Page
 * @see InventoryItem
 */
public class InventoryListener implements Listener {

    /**
     * Handles inventory click events by delegating to the {@link Page#click(InventoryClickEvent)}
     * method of the page associated with the clicked inventory, if one exists.
     *
     * @param event the inventory click event
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Page page = Page.get(event.getInventory());
        if(page != null) {
            page.click(event);
        }
    }

    /**
     * Handles inventory item pickup events. Cancels the event if the item is marked
     * as immovable, or if the page itself is not movable and the item is unrecognized.
     *
     * @param event the inventory pickup item event
     */
    @EventHandler
    public void onInventoryPickup(InventoryPickupItemEvent event) {
        Page page = Page.get(event.getInventory());
        if(page != null) {
            InventoryItem item = page.getInventoryItem(event.getItem().getItemStack());
            if((item != null && !item.isMovable) || (item == null && !page.isMovable)) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Handles inventory item move events (e.g., hopper interactions). Cancels the event
     * if the item is marked as immovable, or if the page itself is not movable and the
     * item is unrecognized.
     *
     * @param event the inventory move item event
     */
    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent event) {
        Page page = Page.get(event.getSource());
        if(page != null) {
            InventoryItem item = page.getInventoryItem(event.getItem());
            if((item != null && !item.isMovable) || (item == null && !page.isMovable)) {
                event.setCancelled(true);
            }
        }
    }
}
