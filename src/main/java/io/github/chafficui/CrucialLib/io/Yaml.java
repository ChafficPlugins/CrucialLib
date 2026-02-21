package io.github.chafficui.CrucialLib.io;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Utility class for Bukkit YAML configuration file I/O.
 * Provides methods to save and load {@link YamlConfiguration} files,
 * automatically creating parent directories and files when necessary.
 */
public class Yaml {

    /**
     * Saves a {@link FileConfiguration} to a YAML file within the specified directory.
     *
     * @param file       the configuration to save
     * @param datafolder the parent directory for the file
     * @param filename   the name (or relative path) of the file within {@code datafolder}
     * @throws IOException if the file cannot be written
     */
    public static void saveFile(FileConfiguration file, File datafolder, String filename) throws IOException {
        file.save(new File(datafolder, filename));
    }

    /**
     * Loads a YAML configuration file from disk. If the file or its parent
     * directories do not exist, they are created automatically.
     *
     * @param datafolder the parent directory for the file
     * @param filename   the name (or relative path) of the file within {@code datafolder}
     * @return the loaded {@link YamlConfiguration}
     * @throws IOException if the file cannot be created or read
     */
    public static YamlConfiguration loadFile(File datafolder, String filename) throws IOException {
        File file = new File(datafolder, filename);

        if(!file.exists()){
            datafolder.mkdirs();
            file.createNewFile();
        }

        return YamlConfiguration.loadConfiguration(file);
    }
}
