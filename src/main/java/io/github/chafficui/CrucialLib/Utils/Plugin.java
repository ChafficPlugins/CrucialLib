package io.github.chafficui.CrucialLib.Utils;

import io.github.chafficui.CrucialLib.Main;

public class Plugin {
    private static final Main PLUGIN = Main.getPlugin(Main.class);

    public static String getVersion(){
        return PLUGIN.getDescription().getVersion();
    }
}
