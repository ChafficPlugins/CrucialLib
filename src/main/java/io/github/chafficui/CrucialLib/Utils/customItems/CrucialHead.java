package io.github.chafficui.CrucialLib.Utils.customItems;

import io.github.chafficui.CrucialLib.exceptions.CrucialException;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * A {@link CrucialItem} variant that uses a player head ({@link Material#PLAYER_HEAD})
 * as its base material, skinned to a specific player via their {@link UUID}.
 *
 * <p>The head owner UUID determines whose skin is applied to the skull item.
 * It must be set (non-{@code null}) before the item is registered; otherwise,
 * {@link #registerRecipe()} will throw a {@link CrucialException}.
 *
 * <p>Example usage:
 * <pre>{@code
 * String[] recipe = {"AIR","AIR","AIR", "AIR","DIAMOND","AIR", "AIR","AIR","AIR"};
 * UUID ownerUuid = UUID.fromString("...");
 * CrucialHead head = new CrucialHead("Trophy", ownerUuid,
 *     List.of("A rare trophy"), recipe, "decoration", true, true, false);
 * head.register();
 * }</pre>
 *
 * @see CrucialItem
 * @see Stack#getStack(UUID, String, List)
 */
public class CrucialHead extends CrucialItem {
    /** The UUID of the player whose skin is applied to the head. May be {@code null} until set. */
    protected UUID headOwner;

    /**
     * Creates a minimal CrucialHead with only a type label.
     * The material is set to {@link Material#PLAYER_HEAD} and the head owner
     * is initially {@code null}. The head owner must be set before calling
     * {@link #register()}.
     *
     * @param type a category label for this item (e.g. {@code "decoration"})
     */
    public CrucialHead(String type) {
        super(type);
        material = String.valueOf(Material.PLAYER_HEAD);
        headOwner = null;
    }

    /**
     * Creates a fully configured CrucialHead with a player head skinned to
     * the specified owner.
     *
     * @param name                the display name of the item
     * @param headOwner           the UUID of the player whose skin is applied to the head
     * @param lore                lore lines displayed below the item name (may be empty)
     * @param recipe              9-element array of material names for the 3x3 shaped crafting recipe
     * @param type                a category label (e.g. {@code "decoration"})
     * @param isCraftable         whether to register a crafting recipe for this item
     * @param isUsable            whether interactions with this item are allowed
     * @param isAllowedForCrafting whether this item can be used as an ingredient in other recipes
     */
    public CrucialHead(String name, UUID headOwner, List<String> lore, String[] recipe, String type, boolean isCraftable, boolean isUsable, boolean isAllowedForCrafting) {
        super(name, Material.PLAYER_HEAD, lore, recipe, type, isCraftable, isUsable, isAllowedForCrafting);
        this.headOwner = headOwner;
    }

    /**
     * Returns the UUID of the player whose skin is applied to the head.
     *
     * @return the head owner's UUID, or {@code null} if not set
     */
    public UUID getHeadOwner() {
        return headOwner;
    }

    /**
     * Creates and registers the shaped crafting recipe for this head item.
     * The head owner must be non-{@code null}; otherwise, a {@link CrucialException}
     * with error code 008 is thrown.
     *
     * @throws CrucialException if the head owner is {@code null} (error 008),
     *                          or if the recipe could not be created (error 002)
     * @see Item#createItem(String, String, ItemStack, String[])
     */
    @Override
    protected void registerRecipe() throws CrucialException {
        if(headOwner != null) {
            ItemStack stack = applyId(Stack.getStack(headOwner, name, lore), this.id);
            this.namespacedKey = Item.createItem(id + type, name, stack, recipe);
        } else {
            throw new CrucialException(8);
        }
    }

    /**
     * Creates a new {@link ItemStack} for this head item with the correct
     * display name, lore, player skin, and embedded CrucialItem UUID.
     *
     * @return a new ItemStack representing this head, or {@code null} if this
     *         item is not registered or the head owner is {@code null}
     */
    @Override
    public ItemStack getItemStack() {
        if(isRegistered && headOwner != null) {
            return applyId(Stack.getStack(headOwner, name, lore), this.id);
        }
        return null;
    }
}
