# API Guide

This guide covers the main CrucialLib APIs with code examples.

## Server version checking

Use `Server.checkVersion()` to verify the server is running a supported Minecraft version before enabling your plugin:

```java
@Override
public void onEnable() {
    if (Server.checkVersion(new String[]{"1.16", "1.15"})) {
        // Server is running a supported version, continue startup
    } else {
        // Unsupported version — disable the plugin
        Bukkit.getPluginManager().disablePlugin(this);
    }
}
```

## Update checking

Use `Crucial.getVersion()` to check whether a newer version of your plugin is available:

```java
private final String CrucialLibVersion = "2.2.0";

@Override
public void onEnable() {
    Crucial.getVersion(CrucialLibVersion, this);
}
```

This will log a message to the console if an update is available.

## Custom items with CrucialItem

`CrucialItem` lets you create custom items with crafting recipes in a single call:

```java
new CrucialItem("Super shovel", Material.DIAMOND_SHOVEL, "item")
    .setCrafting(new String[]{
        "AIR", "AIR", "AIR",
        "DIAMOND", "DIAMOND", "DIAMOND",
        "AIR", "AIR", "AIR"
    });
```

The `setCrafting` method takes a 9-element `String[]` representing the 3x3 crafting grid, using Bukkit `Material` names.

## Auto-download pattern

You can auto-download CrucialLib in your plugin's `onLoad()` method so server admins don't need to install it manually:

```java
private final String CrucialLibVersion = "2.2.0";

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
                 | InvalidPluginException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
}
```

## Full example

Here is a complete plugin main class combining all of the above:

```java
public class Main extends JavaPlugin {
    private final String CrucialLibVersion = "2.2.0";

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
                     | InvalidPluginException e) {
                e.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
    }

    @Override
    public void onEnable() {
        if (Server.checkVersion(new String[]{"1.16", "1.15"})) {
            Crucial.getVersion(CrucialLibVersion, this);
        } else {
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

## Javadoc

For full API reference documentation, see the [Javadoc](javadoc.md) page.
