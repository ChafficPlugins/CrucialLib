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

public class Item {
    private static JavaPlugin getPlugin() {
        return (JavaPlugin) Bukkit.getPluginManager().getPlugin("CrucialLib");
    }

    public static NamespacedKey createItem(String key, String name, List<String> lore,
                                           Material material, String[] recipe) throws CrucialException {
        try{
            ItemStack stack = Stack.getStack(material, name, lore);
            return addRecipe(key, name, recipe, stack);
        } catch(IllegalArgumentException e){
            throw new CrucialException(2);
        }
    }

    public static NamespacedKey createHead(String key, String name, List<String> lore,
                                           UUID headOwner, String[] recipe) throws CrucialException {
        try{
            ItemStack stack = Stack.getStack(headOwner, name, lore);
            return addRecipe(key, name, recipe, stack);
        } catch(IllegalArgumentException e){
            throw new CrucialException(2);
        }
    }

    public static NamespacedKey createItem(String key, String name, ItemStack stack,  String[] recipe) throws CrucialException {
        try{
            return addRecipe(key, name, recipe, stack);
        } catch(IllegalArgumentException e){
            throw new CrucialException(2);
        }
    }

    private static NamespacedKey addRecipe(String key, String name, String[] recipe, ItemStack stack){
        name = name.replaceAll(" ", "_");
        key = key.replaceAll(" ", "_");
        key = key.replaceAll(":", ".");
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
