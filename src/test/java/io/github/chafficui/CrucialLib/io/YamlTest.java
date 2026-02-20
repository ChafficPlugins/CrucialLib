package io.github.chafficui.CrucialLib.io;

import io.github.chafficui.CrucialLib.Main;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class YamlTest {

    private ServerMock server;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        MockBukkit.load(Main.class);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void loadFileCreatesNewFileIfMissing() throws IOException {
        File dataFolder = tempDir.resolve("data").toFile();
        YamlConfiguration config = Yaml.loadFile(dataFolder, "new.yml");

        assertNotNull(config);
        assertTrue(new File(dataFolder, "new.yml").exists());
    }

    @Test
    void loadFileCreatesParentDirectories() throws IOException {
        File dataFolder = tempDir.resolve("deep/nested/path").toFile();
        Yaml.loadFile(dataFolder, "config.yml");

        assertTrue(dataFolder.exists());
        assertTrue(new File(dataFolder, "config.yml").exists());
    }

    @Test
    void saveAndLoadRoundTrip() throws IOException {
        File dataFolder = tempDir.toFile();
        YamlConfiguration config = new YamlConfiguration();
        config.set("greeting", "hello");
        config.set("count", 42);
        config.set("enabled", true);

        Yaml.saveFile(config, dataFolder, "test.yml");

        YamlConfiguration loaded = Yaml.loadFile(dataFolder, "test.yml");
        assertEquals("hello", loaded.getString("greeting"));
        assertEquals(42, loaded.getInt("count"));
        assertTrue(loaded.getBoolean("enabled"));
    }

    @Test
    void savePreservesNestedKeys() throws IOException {
        File dataFolder = tempDir.toFile();
        YamlConfiguration config = new YamlConfiguration();
        config.set("database.host", "localhost");
        config.set("database.port", 3306);
        config.set("database.credentials.user", "admin");

        Yaml.saveFile(config, dataFolder, "nested.yml");

        YamlConfiguration loaded = Yaml.loadFile(dataFolder, "nested.yml");
        assertEquals("localhost", loaded.getString("database.host"));
        assertEquals(3306, loaded.getInt("database.port"));
        assertEquals("admin", loaded.getString("database.credentials.user"));
    }

    @Test
    void savePreservesLists() throws IOException {
        File dataFolder = tempDir.toFile();
        YamlConfiguration config = new YamlConfiguration();
        config.set("items", List.of("sword", "shield", "potion"));

        Yaml.saveFile(config, dataFolder, "list.yml");

        YamlConfiguration loaded = Yaml.loadFile(dataFolder, "list.yml");
        List<String> items = loaded.getStringList("items");
        assertEquals(3, items.size());
        assertEquals("sword", items.get(0));
        assertEquals("shield", items.get(1));
        assertEquals("potion", items.get(2));
    }

    @Test
    void loadExistingFilePreservesContent() throws IOException {
        File dataFolder = tempDir.toFile();

        YamlConfiguration config = new YamlConfiguration();
        config.set("key", "value1");
        Yaml.saveFile(config, dataFolder, "preserve.yml");

        YamlConfiguration loaded = Yaml.loadFile(dataFolder, "preserve.yml");
        assertEquals("value1", loaded.getString("key"));
    }

    @Test
    void loadNewFileReturnsEmptyConfig() throws IOException {
        File dataFolder = tempDir.toFile();
        YamlConfiguration config = Yaml.loadFile(dataFolder, "empty.yml");

        assertTrue(config.getKeys(false).isEmpty());
    }

    @Test
    void overwriteExistingValues() throws IOException {
        File dataFolder = tempDir.toFile();

        YamlConfiguration config = new YamlConfiguration();
        config.set("version", 1);
        Yaml.saveFile(config, dataFolder, "overwrite.yml");

        YamlConfiguration loaded = Yaml.loadFile(dataFolder, "overwrite.yml");
        loaded.set("version", 2);
        Yaml.saveFile(loaded, dataFolder, "overwrite.yml");

        YamlConfiguration reloaded = Yaml.loadFile(dataFolder, "overwrite.yml");
        assertEquals(2, reloaded.getInt("version"));
    }
}
