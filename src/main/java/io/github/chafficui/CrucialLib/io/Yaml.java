package io.github.chafficui.CrucialLib.io;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Yaml {

    public static void saveFile(FileConfiguration file, File datafolder, String filename) throws IOException {
        file.save(new File(datafolder, filename));
    }

    public static YamlConfiguration loadFile(File datafolder, String filename) throws IOException {
        File file = new File(datafolder, filename);

        if(!file.exists()){
            datafolder.mkdirs();
            file.createNewFile();
        }

        return YamlConfiguration.loadConfiguration(file);
    }
}
