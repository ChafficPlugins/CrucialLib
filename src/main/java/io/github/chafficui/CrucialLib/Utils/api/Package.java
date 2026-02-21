package io.github.chafficui.CrucialLib.Utils.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;

/**
 * NMS reflection helpers for accessing CraftBukkit internals.
 * <p>
 * Provides methods to get entity handles and send packets.
 * Note: Uses version-specific NMS paths and may not work on all server implementations.
 * </p>
 */
public class Package {
    private final static HashMap<Class<? extends Entity>, Method> HANDLES = new HashMap<Class<? extends Entity>, Method>();
    private static Field player_connection = null;
    private static Method player_sendPacket = null;
    /**
     * Returns the CraftBukkit version string extracted from the server package name.
     * For example, on a 1.16.5 server this might return {@code "v1_16_R3."}.
     *
     * @return the NMS version string with a trailing dot, or an empty string if unavailable
     */
    public static String getVersion(){
        String[] array = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",");
        if (array.length == 4)
            return array[3] + ".";
        return "";
    }
    /**
     * Resolves an NMS (net.minecraft.server) class by its simple name.
     * The version string is automatically inserted into the fully qualified class name.
     *
     * @param name the simple name of the NMS class (e.g., {@code "EntityPlayer"})
     * @return the resolved {@link Class} object, or {@code null} if the class was not found
     */
    public static Class<?> getNmsClass(String name){
        String version = getVersion();
        String className = "net.minecraft.server." + version + name;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        }
        catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }
        return clazz;
    }
    /**
     * Finds the first declared field in the given class that matches the specified type.
     * The field is made accessible before being returned.
     *
     * @param clazz the class to search for the field
     * @param type  the field type to match
     * @return the first matching {@link Field}, or {@code null} if no field of that type exists
     */
    public static Field getFirstFieldByType(Class<?> clazz, Class<?> type){
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getType() == type) {
                return field;
            }
        }
        return null;
    }
    /**
     * Invokes the {@code getHandle()} method on a CraftBukkit entity to obtain its
     * underlying NMS entity object. Results are cached by entity class for performance.
     *
     * @param entity the Bukkit entity to unwrap
     * @return the NMS entity handle, or {@code null} if reflection fails
     */
    public static Object getHandle(Entity entity){
        try {
            if (HANDLES.get(entity.getClass()) != null)
                return HANDLES.get(entity.getClass()).invoke(entity);
            else {
                Method entity_getHandle = entity.getClass().getMethod("getHandle");
                HANDLES.put(entity.getClass(), entity_getHandle);
                return entity_getHandle.invoke(entity);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Sends an NMS packet to the specified player via their player connection.
     * The player connection and send method are lazily resolved and cached on first use.
     *
     * @param p      the player to send the packet to
     * @param packet the NMS packet object to send
     * @throws IllegalArgumentException if the packet argument is invalid
     */
    public static void sendPacket(Player p, Object packet) throws IllegalArgumentException {
        try {
            if (player_connection == null){
                player_connection = Objects.requireNonNull(Package.getHandle(p)).getClass().getField("playerConnection");
                for (Method m : player_connection.get(Package.getHandle(p)).getClass().getMethods()){
                    if (m.getName().equalsIgnoreCase("sendPacket")){
                        player_sendPacket = m;
                    }
                }
            }
            player_sendPacket.invoke(player_connection.get(Package.getHandle(p)), packet);
        }
        catch (IllegalAccessException | NoSuchFieldException | InvocationTargetException ex){
            ex.printStackTrace();
        }
    }
}
