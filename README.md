# CrucialLib
[![](https://jitpack.io/v/ChafficPlugins/CrucialLib.svg)](https://jitpack.io/#ChafficPlugins/CrucialLib)

CrucialLib is an open-source, easy-to-use Spigot library that aims to simplify plugin development. It is maintained under [ChafficPlugins](https://github.com/ChafficPlugins) as a fork of the no longer maintained [CrucialAPI](https://github.com/Chafficui/CrucialAPI).

> Note: If you have any suggestions, questions, or feedback for this wiki, please use [Discussions](https://github.com/ChafficPlugins/CrucialLib/discussions).

## Setup

> **Attention!** Do not forget to add CrucialLib as a dependency in your `plugin.yml` file!

### Maven

Add the JitPack repository to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Then add the dependency:

```xml
<dependency>
    <groupId>com.github.ChafficPlugins</groupId>
    <artifactId>CrucialLib</artifactId>
    <version>v3.0.1</version>
</dependency>
```

### Gradle

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.ChafficPlugins:CrucialLib:v3.0.1'
}
```

### plugin.yml

```yaml
depend: [CrucialLib]
```

## Features

### Custom Items

Register custom items with unique IDs, crafting recipes, and interaction control. Items are identified via `PersistentDataContainer`, so they survive serialization reliably on Bukkit 1.21+.

```java
// Create and register a custom item
String[] recipe = {
    "AIR", "DIAMOND", "AIR",
    "AIR", "STICK",    "AIR",
    "AIR", "STICK",    "AIR"
};
CrucialItem sword = new CrucialItem("Super Sword", Material.DIAMOND_SWORD,
    List.of("A powerful blade"), recipe, "weapon", true, true, false);
sword.register();

// Identify a custom item from a player's hand
CrucialItem found = CrucialItem.getByStack(player.getInventory().getItemInMainHand());
if (found != null) {
    // It's a registered CrucialItem
}
```

### Custom Inventories (GUIs)

Build interactive chest GUIs with clickable items, fill materials, and prefab components.

```java
// Create a custom page and open it
Page settings = new Page(3, "Settings", Material.GRAY_STAINED_GLASS_PANE) {
    @Override
    public void populate() {
        addItem(new InventoryItem(13, Material.DIAMOND, "Click me", List.of(),
            click -> click.getPlayer().sendMessage("Clicked!")));
    }
};
settings.open(player);

// Prefabs for common patterns
addItems(new TogglePrefab(10, click -> enableFeature(), click -> disableFeature()));
addItems(new YesNoButtonsPrefab(11, 15, click -> confirm(), click -> cancel()));
```

### Localization

Key-based localization with YAML files and placeholder substitution.

```java
// Register a localization source from a YAML file
new LocalizedFromYaml("myPlugin", getDataFolder(), "lang.yml");

// Retrieve a translated string: key format is "identifier_yamlkey"
String msg = Localizer.getLocalizedString("myPlugin_welcome", playerName);
// If lang.yml contains: welcome: "Hello {0}!" -> returns "Hello Steve!"
```

### Player Effects

Title/subtitle display, tablist customization, blood screen effects, and particle spawning.

```java
// Show a title
Interface.showText(player, "Welcome", "Enjoy your stay", 3);

// Blood screen effect (uses world border vignette)
VisualEffects.setBlood(player, 50, 2.0f);  // 50% intensity for 2 seconds

// Spawn particles
VisualEffects.showParticles(player, Particle.HEART, 20);
```

### Timed Boss Bars

```java
Bossbar.sendBossbar(player, "Quest Progress", BarColor.GREEN, 75f, 100L);
```

### JSON & YAML I/O

```java
// JSON
Json.saveFile(Json.toJson(myObject), "data.json");
MyClass obj = Json.fromJson("data.json", MyClass.class);

// YAML (Bukkit YamlConfiguration)
YamlConfiguration config = Yaml.loadFile(getDataFolder(), "settings.yml");
Yaml.saveFile(config, getDataFolder(), "settings.yml");
```

### Server Utilities

```java
// Version compatibility check
if (Server.checkCompatibility("1.21", "1.20")) {
    // Server is running a supported version
}

// Console logging
Server.log("Plugin enabled successfully");
Server.error("Something went wrong");
```

### Auto-Updater

```java
new Updater(this, SPIGET_RESOURCE_ID, getFile(), Updater.UpdateType.CHECK_DOWNLOAD, true);
```

### bStats Integration

```java
Stats stats = new Stats(this, BSTATS_PLUGIN_ID);
stats.sendPieChart("server_type", "survival");
```

## Download

You can get the latest stable release build here: [Stable Build](https://github.com/ChafficPlugins/CrucialLib/releases/latest)
If you want the most up-to-date builds, you can find them here: [Releases](https://github.com/ChafficPlugins/CrucialLib/releases)

## Documentation

Full documentation is available in the [docs/](docs/) directory:

- [Setup Guide](docs/setup.md) -- Installation for server admins and developers
- [API Guide](docs/api-guide.md) -- Code examples for all major APIs
- [Javadoc](docs/javadoc.md) -- Generated API reference (updated on each release)
- [Migration Guide](docs/migrations.md) -- Upgrading from CrucialAPI or older versions

## Generating Javadoc locally

```bash
mvn javadoc:javadoc
# Output: target/site/apidocs/
```

## Getting Help

If you need help or want to chat with the team or other developers, you can join the [Official ChafficPlugins Discord Server](https://discord.gg/ARxYx4Z).
Alternatively, you can use the [Discussions](https://github.com/ChafficPlugins/CrucialLib/discussions) section.

## Dependencies

This project requires **Java 21** and **Spigot 1.21** or higher.

| Version | Supported          |
| ------- | ------------------ |
| 3.0.x   | :white_check_mark: |
| 2.2.x   | :x:                |
| 2.1.x   | :x:                |
| 2.0.x   | :x:                |
| 1.x.x   | :x:                |
