package io.github.chafficui.CrucialLib.Utils.localization;

import io.github.chafficui.CrucialLib.io.Yaml;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * A {@link Localized} implementation backed by a Bukkit YAML configuration file.
 * Translations are loaded from a YAML file on disk and can be reloaded at runtime
 * via {@link #reloadYaml()}.
 *
 * @see Localized
 * @see Localizer
 * @see io.github.chafficui.CrucialLib.io.Yaml
 */
public class LocalizedFromYaml extends Localized {
    protected YamlConfiguration yaml;
    private final File dataFolder;
    private final String filePath;

    /**
     * Creates a new YAML-backed localization source, registers it under the given
     * identifier, and loads the YAML file from disk.
     *
     * @param identifier the unique identifier used to reference this localization
     *                   source in composite keys (the part before the first
     *                   underscore in {@code "identifier_key"} format)
     * @param dataFolder the parent directory containing the YAML file
     * @param filePath   the path of the YAML file relative to {@code dataFolder}
     * @throws IOException if the YAML file cannot be created or read
     */
    public LocalizedFromYaml(String identifier, File dataFolder, String filePath) throws IOException {
        super(identifier);
        this.dataFolder = dataFolder;
        this.filePath = filePath;
        yaml = Yaml.loadFile(dataFolder, filePath);
    }

    /**
     * Reloads the YAML configuration from disk, picking up any changes
     * that were made to the file since the last load.
     *
     * @throws IOException if the YAML file cannot be read
     */
    public void reloadYaml() throws IOException {
        yaml = Yaml.loadFile(dataFolder, filePath);
    }

    /**
     * Returns the translated string for the given key from the YAML configuration.
     *
     * @param key the YAML key to look up
     * @return the translated string, or an empty string if the key is not found
     */
    @Override
    public String getLocalizedString(String key) {
        String localizedString = yaml.getString(key);
        if (localizedString == null) {
            return "";
        }
        return localizedString;
    }
}
