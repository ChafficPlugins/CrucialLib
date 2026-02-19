package io.github.chafficui.CrucialLib.Utils;

import org.bukkit.Bukkit;

import java.util.logging.Logger;

public class Server {
    private final static Logger logger = Logger.getLogger("CrucialLib");

    public static String getVersion(){
        return Bukkit.getVersion();
    }

    public static boolean checkCompatibility(String... validVersions){
        try {
            for (String validVersion :
                    validVersions) {
                if (Bukkit.getVersion().contains(validVersion)) {
                    return true;
                }
            }
            return false;
        } catch (Exception | Error e){
            error("Error checking compatibility. There might be issues with plugins that use CrucialLib!");
            return true;
        }
    }

    public static void log(String message){
        logger.info(message);
    }

    public static void error(String message){
        logger.severe(message);
    }
}
