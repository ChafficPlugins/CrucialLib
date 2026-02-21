package io.github.chafficui.CrucialLib.Utils.player.inventory.prefabs;

import io.github.chafficui.CrucialLib.Utils.customItems.Stack;
import io.github.chafficui.CrucialLib.Utils.player.inventory.InventoryItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * A prefab that creates an ON/OFF toggle button pair. Clicking the ON button swaps it
 * to the OFF button and vice versa, executing the corresponding action on each toggle.
 * Both buttons occupy the same inventory slot, and the page is reloaded after each toggle.
 *
 * @see InventoryItemPrefab
 * @see YesNoButtonsPrefab
 */
public class TogglePrefab implements InventoryItemPrefab {
    private final InventoryItem[] items;
    private final boolean isOn;

    /**
     * Creates a toggle prefab with custom item stacks for the ON and OFF states.
     *
     * @param slot      the inventory slot index for the toggle button
     * @param onItem    the item stack displayed when the toggle is ON
     * @param offItem   the item stack displayed when the toggle is OFF
     * @param toggleOn  the action executed when toggling to ON
     * @param toggleOff the action executed when toggling to OFF
     * @param isOn      the initial state of the toggle ({@code true} for ON, {@code false} for OFF)
     */
    public TogglePrefab(int slot, ItemStack onItem, ItemStack offItem, InventoryItem.Action toggleOn, InventoryItem.Action toggleOff, boolean isOn) {
        this.items = new InventoryItem[2];
        this.items[0] = new InventoryItem(slot, onItem, (click) -> {
            toggleOn.run(click);
            click.getPage().removeItem(items[0]);
            click.getPage().addItem(items[1]);
            click.getPage().reloadInventory();
        });
        this.items[1] = new InventoryItem(slot, offItem, (click) -> {
            toggleOff.run(click);
            click.getPage().removeItem(items[1]);
            click.getPage().addItem(items[0]);
            click.getPage().reloadInventory();
        });
        this.isOn = isOn;
    }

    /**
     * Creates a toggle prefab with default green wool (ON) and red wool (OFF) item stacks.
     *
     * @param slot      the inventory slot index for the toggle button
     * @param toggleOn  the action executed when toggling to ON
     * @param toggleOff the action executed when toggling to OFF
     * @param isOn      the initial state of the toggle ({@code true} for ON, {@code false} for OFF)
     */
    public TogglePrefab(int slot, InventoryItem.Action toggleOn, InventoryItem.Action toggleOff, boolean isOn) {
        this(slot, Stack.getStack(Material.GREEN_WOOL, "§aON"), Stack.getStack(Material.RED_WOOL, "§cOFF"), toggleOn, toggleOff, isOn);
    }

    /**
     * Creates a toggle prefab with default item stacks and an initial state of OFF.
     *
     * @param slot      the inventory slot index for the toggle button
     * @param toggleOn  the action executed when toggling to ON
     * @param toggleOff the action executed when toggling to OFF
     */
    public TogglePrefab(int slot, InventoryItem.Action toggleOn, InventoryItem.Action toggleOff) {
        this(slot, toggleOn, toggleOff, false);
    }


    /**
     * {@inheritDoc}
     *
     * @return a single-element array containing the current state's toggle button
     */
    @Override
    public InventoryItem[] getItems() {
        return new InventoryItem[isOn ? 0 : 1];
    }
}
