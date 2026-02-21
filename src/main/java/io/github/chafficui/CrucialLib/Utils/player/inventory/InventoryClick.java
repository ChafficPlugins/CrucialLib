package io.github.chafficui.CrucialLib.Utils.player.inventory;


import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

/**
 * Wraps an {@link InventoryClickEvent} with convenient accessors for the {@link Page}
 * and {@link Player}. Instances of this class are passed to {@link InventoryItem.Action}
 * callbacks when an inventory item is clicked.
 *
 * @see InventoryItem.Action
 * @see Page
 */
public class InventoryClick {
    private final InventoryClickEvent event;
    private final Page page;

    /**
     * Constructs a new click context wrapping the given event and page.
     *
     * @param event the underlying Bukkit inventory click event
     * @param page  the {@link Page} in which the click occurred
     */
    public InventoryClick(InventoryClickEvent event, Page page) {
        this.event = event;
        this.page = page;
    }

    /**
     * Returns the {@link Page} in which this click occurred.
     *
     * @return the page containing the clicked item
     */
    public Page getPage() {
        return page;
    }

    /**
     * Returns the underlying Bukkit {@link InventoryClickEvent}.
     *
     * @return the raw click event
     */
    public InventoryClickEvent getEvent() {
        return event;
    }

    /**
     * Returns the slot index that was clicked.
     *
     * @return the clicked slot number
     */
    public int getSlot() {
        return event.getSlot();
    }

    /**
     * Returns the {@link Player} who performed the click.
     *
     * @return the player who clicked
     */
    public Player getPlayer() {
        return (Player) event.getWhoClicked();
    }

    /**
     * Returns the Bukkit {@link Inventory} associated with the page where the click occurred.
     *
     * @return the page's inventory
     */
    public Inventory getClickedInventory() {
        return page.getInventory();
    }
}