package io.github.chafficui.CrucialLib.Utils;

import org.bukkit.Bukkit;

import java.util.logging.Logger;

/**
 * Server utility for version checking and console logging.
 * Provides helper methods to retrieve the server version string,
 * verify compatibility against a set of accepted versions, and
 * write informational or error messages to the server console.
 */
public class Server {
    private final static Logger logger = Logger.getLogger("CrucialLib");

    /**
     * Returns the full Bukkit server version string.
     *
     * @return the server version as reported by {@link Bukkit#getVersion()}
     */
    public static String getVersion(){
        return Bukkit.getVersion();
    }

    /**
     * Checks whether the current server version matches any of the provided
     * valid version strings. A match is determined by checking if the server
     * version string contains the candidate. If an error occurs during the
     * check, compatibility is assumed and {@code true} is returned.
     *
     * @param validVersions one or more version substrings to check against
     *                      the server version
     * @return {@code true} if the server version contains at least one of the
     *         given substrings, or if an error occurs during the check;
     *         {@code false} otherwise
     */
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

    /**
     * Logs an informational message to the server console.
     *
     * @param message the message to log at INFO level
     */
    public static void log(String message){
        logger.info(message);
    }

    /**
     * Logs an error message to the server console.
     *
     * @param message the message to log at SEVERE level
     */
    public static void error(String message){
        logger.severe(message);
    }
}
