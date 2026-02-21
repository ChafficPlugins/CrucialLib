package io.github.chafficui.CrucialLib.Events;

import io.github.chafficui.CrucialLib.Utils.customItems.CrucialItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

/**
 * Bukkit event listener that enforces {@link CrucialItem} interaction and crafting rules.
 * <p>
 * Cancels interactions with non-usable items and prevents non-allowed items from
 * being used in crafting recipes.
 * </p>
 *
 * @see CrucialItem
 */
public class CrucialItemEvents implements Listener {

    @EventHandler
    private void onInteract(PlayerInteractEntityEvent event){
        ItemStack stack = event.getPlayer().getItemOnCursor();
        if(cancelInteractionEvent(stack)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event){
        ItemStack stack = event.getPlayer().getItemOnCursor();
        if(cancelInteractionEvent(stack)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onInteract(PlayerInteractAtEntityEvent event){
        ItemStack stack = event.getPlayer().getItemOnCursor();
        if(cancelInteractionEvent(stack)) {
            event.setCancelled(true);
        }
    }

    private boolean cancelInteractionEvent(ItemStack stack){
        if(stack.getItemMeta() != null){
            CrucialItem item = CrucialItem.getByStack(stack);
            return item != null && !item.isUsable;
        }
        return false;
    }

    @EventHandler
    private void onCrafting(PrepareItemCraftEvent event){
        CraftingInventory inventory = event.getInventory();
        for (ItemStack stack:inventory.getStorageContents()){
            CrucialItem item = CrucialItem.getByStack(stack);
            if(item != null && (!item.isAllowedForCrafting || CrucialItem.getByStack(inventory.getResult()) != null)){
                inventory.setResult(null);
            }
        }
    }
}
