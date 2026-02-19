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
    <version>v3.0.0</version>
</dependency>
```

### Gradle

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.ChafficPlugins:CrucialLib:v3.0.0'
}
```

### plugin.yml

```yaml
depend: [CrucialLib]
```

## Examples

```java
public class Main extends JavaPlugin {
    private final String CrucialLibVersion = "3.0.0";

    /** Auto-download CrucialLib */
    @Override
    public void onLoad() {
        if (getServer().getPluginManager().getPlugin("CrucialLib") == null) {
            try {
                URL website = new URL(
                    "https://github.com/ChafficPlugins/CrucialLib/releases/download/v"
                    + CrucialLibVersion + "/CrucialLib-v" + CrucialLibVersion + ".jar"
                );
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream("plugins/CrucialLib.jar");
                fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
                Bukkit.getPluginManager().loadPlugin(new File("plugins/CrucialLib.jar"));
            } catch (IOException | InvalidDescriptionException
                     | org.bukkit.plugin.InvalidPluginException e) {
                e.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
    }

    @Override
    public void onEnable() {
        if (Server.checkCompatibility("1.21")) {
            // Server is running a supported version, continue startup
        } else {
            // Unsupported version — disable the plugin
            Bukkit.getPluginManager().disablePlugin(this);
        }

        new CrucialItem("Super shovel", Material.DIAMOND_SHOVEL, "item")
            .setCrafting(new String[]{
                "AIR", "AIR", "AIR",
                "DIAMOND", "DIAMOND", "DIAMOND",
                "AIR", "AIR", "AIR"
            });
    }
}
```

## Download

You can get the latest stable release build here: [Stable Build](https://github.com/ChafficPlugins/CrucialLib/releases/latest)
If you want the most up-to-date builds, you can find them here: [Releases](https://github.com/ChafficPlugins/CrucialLib/releases)

## Documentation

Full documentation is available in the [docs/](docs/) directory:

- [Setup Guide](docs/setup.md) — Installation for server admins and developers
- [API Guide](docs/api-guide.md) — Code examples for all major APIs
- [Migration Guide](docs/migrations.md) — Upgrading from CrucialAPI or older versions

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
