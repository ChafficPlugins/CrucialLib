package io.github.chafficui.CrucialLib.Utils.api;

import org.bukkit.entity.Player;

/**
 * Utility for sending title and subtitle text to players.
 *
 * @see io.github.chafficui.CrucialLib.Utils.player.effects.Interface
 */
public class Title {

    /**
     * Sends a title and subtitle to the specified player with the given timing parameters.
     *
     * @param p        the player to send the title to
     * @param fadeIn   the fade-in time in ticks
     * @param stay     the stay time in ticks
     * @param fadeOut  the fade-out time in ticks
     * @param title    the main title text to display
     * @param subtitle the subtitle text to display below the title
     */
    public static void sendTitle(Player p, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        p.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }
}
