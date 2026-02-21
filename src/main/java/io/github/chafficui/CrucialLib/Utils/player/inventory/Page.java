package io.github.chafficui.CrucialLib.Utils.player.inventory;

import io.github.chafficui.CrucialLib.Utils.player.inventory.prefabs.InventoryItemPrefab;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a custom GUI page (chest inventory) that can hold {@link InventoryItem}s.
 * Extend this class and override {@link #populate()} to define your GUI layout.
 * Pages are automatically tracked in a global registry and handle click events via
 * {@link InventoryListener}.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * public class MyPage extends Page {
 *     public MyPage() {
 *         super(3, "My GUI", Material.GRAY_STAINED_GLASS_PANE);
 *     }
 *
 *     @Override
 *     public void populate() {
 *         addItem(new InventoryItem(13, Material.DIAMOND, "Click me", null, click -> {
 *             click.getPlayer().sendMessage("Clicked!");
 *         }));
 *     }
 * }
 * }</pre>
 *
 * @see InventoryItem
 * @see InventoryListener
 */
public class Page {
    /**
     * Global registry of all created pages. Pages are added on construction
     * and can be looked up by their underlying {@link Inventory} instance.
     */
    public final static ArrayList<Page> pages = new ArrayList<>();

    /**
     * Finds the {@link Page} associated with the given inventory.
     *
     * @param inventory the Bukkit inventory to look up
     * @return the matching page, or {@code null} if no page is associated with the inventory
     */
    public static Page get(Inventory inventory) {
        for (Page page : pages) {
            if (page.inventory.equals(inventory)) {
                return page;
            }
        }
        return null;
    }

    /**
     * Returns the page at the given index in the global page registry.
     *
     * @param index the index in the {@link #pages} list
     * @return the page at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public static Page get(int index) {
        return pages.get(index);
    }

    /**
     * Checks whether a {@link Page} exists for the given inventory.
     *
     * @param inventory the Bukkit inventory to check
     * @return {@code true} if a page is associated with the inventory, {@code false} otherwise
     */
    public static boolean exists(Inventory inventory) {
        return get(inventory) != null;
    }

    private final int size;
    private final String title;
    protected ArrayList<InventoryItem> inventoryItems = new ArrayList<>();
    private Inventory inventory;
    /**
     * A general-purpose map for storing arbitrary extra data associated with this page.
     * Plugins can use this to attach custom state to a page instance.
     */
    public final HashMap<String, Object> extraData = new HashMap<>();
    private final Material fillMaterial;
    protected boolean isMovable;

    /**
     * Constructs a new page and registers it in the global {@link #pages} list.
     * Every subclass must call {@code super()} in its constructor.
     *
     * @param size         the number of rows in the inventory, between 1 and 6 (inclusive);
     *                     this is multiplied by 9 to get the total slot count
     * @param title        the display title of the inventory
     * @param fillMaterial the material used to fill empty slots as a background
     * @throws IllegalArgumentException if size is not between 1 and 6
     */
    public Page(int size, String title, Material fillMaterial) {
        if (size > 6 || size < 1)
            throw new IllegalArgumentException("Size of an inventory can only be between 1 and 6!");
        this.size = size * 9;
        this.title = title;
        this.fillMaterial = fillMaterial;
        pages.add(this);
    }


    /**
     * Populates the page with inventory items. Override this method in subclasses to
     * define the GUI layout by calling {@link #addItem(InventoryItem)} or
     * {@link #addItems(InventoryItem...)}. This method is called each time the
     * inventory is reloaded via {@link #reloadInventory()}.
     *
     * <p>The default implementation adds a red glass pane close button at slot 0.</p>
     */
    public void populate() {
        addItem(new InventoryItem(0, new ItemStack(Material.RED_STAINED_GLASS_PANE), click -> click.getPlayer().closeInventory()));
    }

    /**
     * Creates the underlying Bukkit inventory, populates it, and opens it for the given player.
     *
     * @param player the player to show the inventory to
     */
    public void open(Player player) {
        inventory = Bukkit.createInventory(null, size, title);
        reloadInventory();
        player.openInventory(inventory);
    }

    /**
     * Adds a single {@link InventoryItem} to this page's item list.
     *
     * @param item the inventory item to add
     */
    public void addItem(InventoryItem item) {
        inventoryItems.add(item);
    }

    /**
     * Adds multiple {@link InventoryItem}s to this page's item list.
     *
     * @param items the inventory items to add
     */
    public void addItems(InventoryItem... items) {
        for(InventoryItem item : items) {
            addItem(item);
        }
    }

    /**
     * Adds all {@link InventoryItem}s produced by the given prefab to this page's item list.
     *
     * @param items the prefab that provides the inventory items to add
     * @see InventoryItemPrefab
     */
    public void addItems(InventoryItemPrefab items) {
        for(InventoryItem item : items.getItems()) {
            addItem(item);
        }
    }

    /**
     * Removes an {@link InventoryItem} from this page's item list.
     *
     * @param item the inventory item to remove
     */
    public void removeItem(InventoryItem item) {
        inventoryItems.remove(item);
    }

    /**
     * Clears and rebuilds the inventory contents. Fills all slots with the fill material,
     * then calls {@link #populate()} to re-add items, and finally places each item into
     * its designated slot. Does nothing if the inventory has not been created yet.
     */
    public void reloadInventory() {
        if (inventory != null) {
            inventory.clear();
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, new InventoryItem(i, new ItemStack(fillMaterial)).getItem());
            }
            inventoryItems = new ArrayList<>();
            populate();
            for (InventoryItem item : inventoryItems) {
                inventory.setItem(item.getSlot(), item.getItem());
            }
        }
    }

    /**
     * Handles an {@link InventoryClickEvent} by finding the matching {@link InventoryItem}
     * at the clicked slot and executing its action. Left-click events from players are
     * dispatched to the item's action callback. Clicks on fill material slots are cancelled.
     *
     * @param event the inventory click event to process
     */
    public void click(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && event.isLeftClick()) {
            for (InventoryItem item : inventoryItems) {
                if (event.getCurrentItem() != null && item.getItem().getType() == event.getCurrentItem().getType() &&
                item.getSlot() == event.getSlot()) {
                    item.execute(new InventoryClick(event, this));
                    if(!item.isMovable){
                        event.setCancelled(true);
                    }
                    return;
                }
            }
        }
        if(event.getCurrentItem() != null && event.getCurrentItem().getType() == fillMaterial) {
            event.setCancelled(true);
        }
    }

    /**
     * Finds the {@link InventoryItem} in this page that matches the given {@link ItemStack}.
     * If the item matches the fill material but is not a registered inventory item,
     * a default placeholder item is returned.
     *
     * @param item the item stack to look up
     * @return the matching inventory item, a fill-material placeholder, or {@code null} if not found
     */
    public InventoryItem getInventoryItem(ItemStack item) {
        for (InventoryItem inventoryItem : inventoryItems) {
            if (inventoryItem.getItem().equals(item)) {
                return inventoryItem;
            }
        }
        if(item.getType() == fillMaterial) return new InventoryItem(0, item);
        return null;
    }

    /**
     * Returns the underlying Bukkit {@link Inventory} for this page.
     * May be {@code null} if {@link #open(Player)} has not been called yet.
     *
     * @return the Bukkit inventory, or {@code null} if not yet created
     */
    public Inventory getInventory() {
        return inventory;
    }
}
