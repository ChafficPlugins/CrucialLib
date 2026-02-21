package io.github.chafficui.CrucialLib.Utils;

import io.github.chafficui.CrucialLib.Main;
import org.bukkit.Bukkit;

/**
 * Static accessor for CrucialLib plugin metadata.
 * Provides convenience methods to retrieve information about the
 * running CrucialLib plugin instance from the Bukkit plugin manager.
 */
public class Plugin {

    /**
     * Returns the version string of the CrucialLib plugin as declared
     * in its {@code plugin.yml}.
     *
     * @return the current CrucialLib plugin version
     */
    public static String getVersion(){
        return Bukkit.getPluginManager().getPlugin("CrucialLib").getDescription().getVersion();
    }
}
