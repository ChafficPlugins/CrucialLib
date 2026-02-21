package io.github.chafficui.CrucialLib.Utils.api;

import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

/**
 * Per-player world border effect using the Bukkit {@link WorldBorder} API.
 *
 * <p>Creates a visual vignette/blood effect on the player's screen by
 * manipulating the world border warning distance. A large virtual border is
 * centred on the player, and the warning distance controls how strongly the
 * red vignette overlay appears.</p>
 *
 * <p>This implementation uses the standard Bukkit API
 * ({@link Bukkit#createWorldBorder()}, {@link Player#setWorldBorder(WorldBorder)})
 * which is available on Spigot and Paper since 1.18.2.</p>
 *
 * @see io.github.chafficui.CrucialLib.Utils.player.effects.VisualEffects
 */
public class Border {

    /** Half-size of the virtual border. Large enough that the player is always inside. */
    private static final double BORDER_SIZE = 200_000.0;

    /**
     * Constructs a new Border instance.
     */
    public Border() {
    }

    /**
     * Removes the world border vignette effect from the specified player's screen.
     *
     * @param p the player whose border effect should be removed
     */
    public void removeBorder(Player p) {
        p.setWorldBorder(null);
    }

    /**
     * Sets the world border vignette effect on the specified player's screen.
     * A higher percentage value produces a stronger visual effect (more red vignette).
     *
     * <p>The percentage maps to a warning distance: 0 means no vignette,
     * 100 means maximum vignette intensity.</p>
     *
     * @param p          the player to apply the border effect to
     * @param percentage the intensity of the border effect (0–100), where 0 is maximum
     *                   intensity and 100 is no effect (matching the original API contract
     *                   where this value is {@code 100 - bloodPercentage})
     */
    public void setBorder(Player p, int percentage) {
        // The original formula: dist = -10000 * percentage + 1300000
        // percentage=100 (no blood) => dist=300000  (low warning, no vignette)
        // percentage=0   (max blood) => dist=1300000 (high warning, strong vignette)
        int warningDistance = -10000 * percentage + 1300000;

        WorldBorder wb = Bukkit.createWorldBorder();
        wb.setCenter(p.getLocation());
        wb.setSize(BORDER_SIZE);
        wb.setWarningDistance(warningDistance);
        wb.setWarningTime(15);
        wb.setDamageAmount(0);
        wb.setDamageBuffer(0);
        p.setWorldBorder(wb);
    }
}
