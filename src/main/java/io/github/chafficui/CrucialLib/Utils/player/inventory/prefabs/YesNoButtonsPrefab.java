package io.github.chafficui.CrucialLib.Utils.player.inventory.prefabs;

import io.github.chafficui.CrucialLib.Utils.customItems.Stack;
import io.github.chafficui.CrucialLib.Utils.player.inventory.InventoryItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * A prefab that creates a Yes/No button pair at specified inventory slots.
 * Each button has its own click action. By default, the Yes button uses green wool
 * and the No button uses red wool.
 *
 * @see InventoryItemPrefab
 * @see TogglePrefab
 */
public class YesNoButtonsPrefab implements InventoryItemPrefab {
    private final InventoryItem[] items;

    /**
     * Creates a Yes/No button pair with custom item stacks.
     *
     * @param slotYes   the inventory slot index for the Yes button
     * @param slotNo    the inventory slot index for the No button
     * @param yesItem   the item stack displayed for the Yes button
     * @param noItem    the item stack displayed for the No button
     * @param yesAction the action executed when the Yes button is clicked
     * @param noAction  the action executed when the No button is clicked
     */
    public YesNoButtonsPrefab(int slotYes, int slotNo, ItemStack yesItem, ItemStack noItem, InventoryItem.Action yesAction, InventoryItem.Action noAction) {
        this.items = new InventoryItem[2];
        this.items[0] = new InventoryItem(slotYes, yesItem, yesAction);
        this.items[1] = new InventoryItem(slotNo, noItem, noAction);
    }

    /**
     * Creates a Yes/No button pair with default green wool (Yes) and red wool (No) item stacks.
     *
     * @param slotYes   the inventory slot index for the Yes button
     * @param slotNo    the inventory slot index for the No button
     * @param yesAction the action executed when the Yes button is clicked
     * @param noAction  the action executed when the No button is clicked
     */
    public YesNoButtonsPrefab(int slotYes, int slotNo, InventoryItem.Action yesAction, InventoryItem.Action noAction) {
        this(slotYes, slotNo, Stack.getStack(Material.GREEN_WOOL, "§aYES"), Stack.getStack(Material.RED_WOOL, "§cNO"), yesAction, noAction);
    }


    /**
     * {@inheritDoc}
     *
     * @return a two-element array containing the Yes button at index 0 and the No button at index 1
     */
    @Override
    public InventoryItem[] getItems() {
        return items;
    }
}
