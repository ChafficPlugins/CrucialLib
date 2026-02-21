package io.github.chafficui.CrucialLib.Utils.customItems;

import io.github.chafficui.CrucialLib.Utils.Server;
import io.github.chafficui.CrucialLib.exceptions.CrucialException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Provides static factory methods for creating shaped crafting recipes and
 * registering them with the Bukkit server.
 *
 * <p>Each factory method builds an {@link ItemStack} (either from a
 * {@link Material} or a player head {@link UUID}), wraps it in a
 * {@link ShapedRecipe} defined by a 9-element material-name array, and
 * registers the recipe with {@link Bukkit#addRecipe}.
 *
 * <p>The recipe array maps directly to a 3x3 crafting grid in row-major
 * order (indices 0-2 = top row, 3-5 = middle row, 6-8 = bottom row).
 * Slots containing {@code "AIR"} are treated as empty.
 *
 * @see CrucialItem
 * @see CrucialHead
 * @see Stack
 */
public class Item {
    /**
     * Returns the CrucialLib plugin instance used for creating {@link NamespacedKey}s.
     *
     * @return the CrucialLib {@link JavaPlugin} instance
     */
    private static JavaPlugin getPlugin() {
        return (JavaPlugin) Bukkit.getPluginManager().getPlugin("CrucialLib");
    }

    /**
     * Creates a shaped crafting recipe for a standard item and registers it
     * with the server. The {@link ItemStack} is built from the given material,
     * name, and lore via {@link Stack#getStack(Material, String, List)}.
     *
     * @param key      a unique key suffix used (along with the name) to form the
     *                 {@link NamespacedKey} for the recipe
     * @param name     the display name of the resulting item
     * @param lore     lore lines displayed below the item name
     * @param material the base material for the item
     * @param recipe   9-element array of material names defining the 3x3 shaped recipe
     * @return the {@link NamespacedKey} of the registered recipe
     * @throws CrucialException if the recipe could not be created (error 002)
     */
    public static NamespacedKey createItem(String key, String name, List<String> lore,
                                           Material material, String[] recipe) throws CrucialException {
        try{
            ItemStack stack = Stack.getStack(material, name, lore);
            return addRecipe(key, name, recipe, stack);
        } catch(IllegalArgumentException e){
            throw new CrucialException(2);
        }
    }

    /**
     * Creates a shaped crafting recipe for a player head item and registers it
     * with the server. The {@link ItemStack} is built as a player skull skinned
     * to the specified owner via {@link Stack#getStack(UUID, String, List)}.
     *
     * @param key       a unique key suffix used (along with the name) to form the
     *                  {@link NamespacedKey} for the recipe
     * @param name      the display name of the resulting head item
     * @param lore      lore lines displayed below the item name
     * @param headOwner the UUID of the player whose skin is applied to the skull
     * @param recipe    9-element array of material names defining the 3x3 shaped recipe
     * @return the {@link NamespacedKey} of the registered recipe
     * @throws CrucialException if the recipe could not be created (error 002)
     */
    public static NamespacedKey createHead(String key, String name, List<String> lore,
                                           UUID headOwner, String[] recipe) throws CrucialException {
        try{
            ItemStack stack = Stack.getStack(headOwner, name, lore);
            return addRecipe(key, name, recipe, stack);
        } catch(IllegalArgumentException e){
            throw new CrucialException(2);
        }
    }

    /**
     * Creates a shaped crafting recipe for a pre-built {@link ItemStack} and
     * registers it with the server. This overload is used when the caller has
     * already constructed the stack (e.g. with a CrucialItem UUID applied).
     *
     * @param key    a unique key suffix used (along with the name) to form the
     *               {@link NamespacedKey} for the recipe
     * @param name   the display name of the resulting item
     * @param stack  the pre-built {@link ItemStack} produced by the recipe
     * @param recipe 9-element array of material names defining the 3x3 shaped recipe
     * @return the {@link NamespacedKey} of the registered recipe
     * @throws CrucialException if the recipe could not be created (error 002)
     */
    public static NamespacedKey createItem(String key, String name, ItemStack stack,  String[] recipe) throws CrucialException {
        try{
            return addRecipe(key, name, recipe, stack);
        } catch(IllegalArgumentException e){
            throw new CrucialException(2);
        }
    }

    /**
     * Builds a {@link ShapedRecipe} from the given parameters and registers it
     * with {@link Bukkit#addRecipe}. The recipe key and name are sanitized
     * (spaces replaced, lowercased) before forming the {@link NamespacedKey}.
     *
     * <p>The recipe array is iterated with characters {@code '1'} through
     * {@code '9'} mapped to grid positions. Slots whose material is
     * {@link Material#AIR} are left empty.
     *
     * @param key    a unique key suffix for the recipe
     * @param name   the display name (also used as part of the namespaced key)
     * @param recipe 9-element array of material names for the 3x3 grid
     * @param stack  the {@link ItemStack} produced by the recipe
     * @return the {@link NamespacedKey} of the registered recipe
     */
    private static NamespacedKey addRecipe(String key, String name, String[] recipe, ItemStack stack){
        name = name.replaceAll(" ", "_").toLowerCase();
        key = key.replaceAll(" ", "_").replaceAll(":", ".").toLowerCase();
        NamespacedKey namespacedKey = new NamespacedKey(getPlugin(), name + key);
        ShapedRecipe shapedRecipe = new ShapedRecipe(namespacedKey, stack);
        int num = 48;

        shapedRecipe.shape("123","456","789");

        for (String item:recipe) {
            num++;
            Material mat = Objects.requireNonNull(Material.getMaterial(item));
            if (mat != Material.AIR) {
                char c = (char)num;
                shapedRecipe.setIngredient(c, mat);
            }
        }

        Bukkit.addRecipe(shapedRecipe);
        Server.log("Successfully created " + name + " (key: " + name + key + ")");
        return namespacedKey;
    }
}
