package io.github.chafficui.CrucialLib.Utils;

import io.github.chafficui.CrucialLib.Main;
import org.bukkit.Bukkit;

public class Plugin {

    public static String getVersion(){
        return Bukkit.getPluginManager().getPlugin("CrucialLib").getDescription().getVersion();
    }
}
