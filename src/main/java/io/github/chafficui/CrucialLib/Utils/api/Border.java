package io.github.chafficui.CrucialLib.Utils.api;

import io.github.chafficui.CrucialLib.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Per-player world border effect using NMS reflection packets.
 * <p>
 * Creates a visual vignette/blood effect on the player's screen by manipulating
 * the world border warning distance. Note: Uses version-specific NMS internals
 * and may not work on all server implementations.
 * </p>
 *
 * @see io.github.chafficui.CrucialLib.Utils.player.effects.VisualEffects
 */
public class Border {
    private static Method handle, sendPacket;
    private static Method center, distance, time, movement;
    private static Field player_connection;
    private static Constructor<?> constructor, border_constructor;
    private static Object constant;

    static {
        try {
            handle = getClass("org.bukkit.craftbukkit", "entity.CraftPlayer").getMethod("getHandle");
            player_connection = getClass("net.minecraft.server", "EntityPlayer").getField("playerConnection");
            for (Method m : getClass("net.minecraft.server", "PlayerConnection").getMethods()) {
                if (m.getName().equals("sendPacket")) {
                    sendPacket = m;
                    break;
                }
            }
            Class<?> enumclass;
            try {
                enumclass = getClass("net.minecraft.server", "EnumWorldBorderAction");
            } catch(ClassNotFoundException x) {
                enumclass = getClass("net.minecraft.server", "PacketPlayOutWorldBorder$EnumWorldBorderAction");
            }
            constructor = getClass("net.minecraft.server", "PacketPlayOutWorldBorder").getConstructor(getClass("net.minecraft.server", "WorldBorder"), enumclass);
            border_constructor = getClass("net.minecraft.server", "WorldBorder").getConstructor();

            String setCenter = "setCenter";
            String setWarningDistance = "setWarningDistance";
            String setWarningTime = "setWarningTime";
            String transitionSizeBetween = "transitionSizeBetween";

            center = getClass("net.minecraft.server", "WorldBorder").getMethod(setCenter, double.class, double.class);
            distance = getClass("net.minecraft.server", "WorldBorder").getMethod(setWarningDistance, int.class);
            time = getClass("net.minecraft.server", "WorldBorder").getMethod(setWarningTime, int.class);
            movement = getClass("net.minecraft.server", "WorldBorder").getMethod(transitionSizeBetween, double.class, double.class, long.class);

            for (Object o: enumclass.getEnumConstants()) {
                if (o.toString().equals("INITIALIZE")) {
                    constant = o;
                    break;
                }
            }
        } catch (Exception ignore) {
        }
    }

    private static Class<?> getClass(String prefix, String name) throws Exception {
        return Class.forName(prefix + "." + Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1) + "." + name);
    }

    Main plugin;

    /**
     * Constructs a new Border instance tied to the given plugin.
     *
     * @param plugin the CrucialLib plugin instance used for scheduling and resource access
     */
    public Border(Main plugin) {
        this.plugin = plugin;
    }
    /**
     * Removes the world border vignette effect from the specified player's screen.
     *
     * @param p the player whose border effect should be removed
     */
    public void removeBorder(Player p) {
        if(!Bukkit.getVersion().contains("1.17")){
            sendWorldBorderPacket(p, 0, 200000D, 200000D, 0);
        }
    }

    /**
     * Sets the world border vignette effect on the specified player's screen.
     * A higher percentage produces a stronger visual effect.
     *
     * @param p          the player to apply the border effect to
     * @param percentage the intensity of the border effect (0-100), where 0 is no effect
     *                   and 100 is maximum intensity
     */
    public void setBorder(Player p, int percentage){
        if(!Bukkit.getVersion().contains("1.17")){
            int dist = -10000 * percentage + 1300000;
            sendWorldBorderPacket(p, dist, 200000D, 200000D, 0);
        }
    }

    /**
     * Sends a raw world border NMS packet to the specified player.
     * This constructs a {@code PacketPlayOutWorldBorder} with the given parameters
     * and delivers it via the player's network connection.
     *
     * @param p         the player to send the world border packet to
     * @param dist      the warning distance value for the border
     * @param oldradius the starting border radius before transition
     * @param newradius the ending border radius after transition
     * @param delay     the transition delay in milliseconds (0 for instant)
     */
    public void sendWorldBorderPacket(Player p, int dist, double oldradius, double newradius, long delay) {
        try {
            Object wb = border_constructor.newInstance();

            Method worldServer = getClass("org.bukkit.craftbukkit", "CraftWorld").getMethod("getHandle");
            Field world = getClass("net.minecraft.server", "WorldBorder").getField("world");
            world.set(wb, worldServer.invoke(p.getWorld()));

            center.invoke(wb, p.getLocation().getX(), p.getLocation().getY());
            distance.invoke(wb, dist);
            time.invoke(wb, 15);
            movement.invoke(wb, oldradius, newradius, delay);

            Object packet = constructor.newInstance(wb, constant);
            sendPacket.invoke(player_connection.get(handle.invoke(p)), packet);
        } catch(Exception x) {
            x.printStackTrace();
        }
    }
}
