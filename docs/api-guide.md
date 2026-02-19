# API Guide

This guide covers the main CrucialLib APIs with code examples.

## Server version checking

Use `Server.checkCompatibility()` to verify the server is running a supported Minecraft version before enabling your plugin:

```java
@Override
public void onEnable() {
    if (Server.checkCompatibility("1.21")) {
        // Server is running a supported version, continue startup
    } else {
        // Unsupported version — disable the plugin
        Bukkit.getPluginManager().disablePlugin(this);
    }
}
```

The method accepts varargs, so you can check multiple versions:

```java
Server.checkCompatibility("1.21", "1.22")
```

## Custom items with CrucialItem

`CrucialItem` lets you create custom items with crafting recipes:

```java
CrucialItem item = new CrucialItem("Super shovel", Material.DIAMOND_SHOVEL, "item");
item.setRecipe(new String[]{
    "AIR", "AIR", "AIR",
    "DIAMOND", "DIAMOND", "DIAMOND",
    "AIR", "AIR", "AIR"
});
item.register();
```

The recipe takes a 9-element `String[]` representing the 3x3 crafting grid, using Bukkit `Material` names.

### Item properties

```java
CrucialItem item = new CrucialItem("my_item");
item.setName("Magic Pickaxe");
item.setMaterial("DIAMOND_PICKAXE");
item.setLore(List.of("A very special pickaxe"));
item.isCraftable = true;
item.isUsable = true;
item.isAllowedForCrafting = false;
item.register();
```

### Looking up items

```java
// Get CrucialItem from an ItemStack
UUID itemId = CrucialItem.getId(itemStack);
CrucialItem item = CrucialItem.getByStack(itemStack);
CrucialItem item = CrucialItem.getById(uuid);
```

## Custom GUIs with Page

Create clickable inventory GUIs using `Page` and `InventoryItem`:

```java
Page page = new Page(3, "My GUI", Material.GRAY_STAINED_GLASS_PANE);

InventoryItem button = new InventoryItem(
    13,                           // slot
    Material.DIAMOND,             // material
    "Click me!",                  // display name
    List.of("Some lore"),         // lore
    click -> {                    // action callback
        click.getPlayer().sendMessage("You clicked!");
    }
);

page.addItem(button);
page.open(player);
```

### Prefab components

```java
// Toggle button (ON/OFF)
TogglePrefab toggle = new TogglePrefab(slot, "Feature", isEnabled, click -> {
    // handle toggle
});
page.addItems(toggle);

// Yes/No buttons
YesNoButtonsPrefab yesNo = new YesNoButtonsPrefab(yesSlot, noSlot, click -> {
    // handle yes
}, click -> {
    // handle no
});
page.addItems(yesNo);
```

## ItemStack builder with Stack

Build ItemStacks with convenience methods:

```java
// Basic item
ItemStack item = Stack.getStack(Material.DIAMOND_SWORD, "Excalibur");

// With lore
ItemStack item = Stack.getStack(Material.DIAMOND_SWORD, "Excalibur",
    List.of("A legendary sword", "Forged in fire"));

// With enchantment glow
ItemStack item = Stack.getStack(Material.DIAMOND_SWORD, "Excalibur",
    List.of("A legendary sword"), true);

// Player head
ItemStack head = Stack.getStack(playerUUID, "Player Head",
    List.of("Custom head item"));
```

## Localization

Use the `Localizer` for key-based localization with placeholder support:

```java
// Register a YAML-based localization source
new LocalizedFromYaml("myplugin", yamlFile);

// Look up localized strings (key format: "identifier_key")
String title = Localizer.getLocalizedString("myplugin_menu_title");

// With placeholders ({0}, {1}, etc.)
String msg = Localizer.getLocalizedString("myplugin_welcome", playerName, "5");
```

## Auto-download pattern

You can auto-download CrucialLib in your plugin's `onLoad()` method so server admins don't need to install it manually:

```java
private final String CrucialLibVersion = "3.0.0";

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

Here is a complete plugin main class combining version checking and custom items:

```java
public class Main extends JavaPlugin {
    private final String CrucialLibVersion = "3.0.0";

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
        if (Server.checkCompatibility("1.21")) {
            // Server is running a supported version
        } else {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        CrucialItem shovel = new CrucialItem("Super shovel", Material.DIAMOND_SHOVEL, "item");
        shovel.setRecipe(new String[]{
            "AIR", "AIR", "AIR",
            "DIAMOND", "DIAMOND", "DIAMOND",
            "AIR", "AIR", "AIR"
        });
        shovel.register();
    }
}
```

## Javadoc

For full API reference documentation, see the [Javadoc](javadoc.md) page.
