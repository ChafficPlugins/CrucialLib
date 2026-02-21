# API Guide

This guide covers all CrucialLib APIs with code examples. For full method-level documentation, see the [Javadoc](javadoc.md).

> All examples assume your plugin has `depend: [CrucialLib]` in `plugin.yml`.

## Table of Contents

- [Custom Items](#custom-items)
  - [CrucialItem](#crucialitem)
  - [CrucialHead](#crucialhead)
  - [Item Identification](#item-identification)
  - [ItemStack Builder (Stack)](#itemstack-builder)
- [Custom GUIs](#custom-guis)
  - [Page](#page)
  - [InventoryItem](#inventoryitem)
  - [InventoryClick](#inventoryclick)
  - [Prefab Components](#prefab-components)
- [Localization](#localization)
- [JSON & YAML I/O](#json--yaml-io)
- [Player Effects](#player-effects)
  - [Titles & Text](#titles--text)
  - [Tablist](#tablist)
  - [Player Names](#player-names)
  - [Blood Screen Effect](#blood-screen-effect)
  - [Particles](#particles)
- [Timed Boss Bars](#timed-boss-bars)
- [Server Utilities](#server-utilities)
- [Auto-Updater](#auto-updater)
- [bStats Integration](#bstats-integration)
- [Auto-Download Pattern](#auto-download-pattern)
- [Full Example Plugin](#full-example-plugin)

---

## Custom Items

### CrucialItem

`CrucialItem` lets you create custom items with unique IDs, shaped crafting recipes, and interaction control. Each item is identified by a UUID stored in the item's `PersistentDataContainer`, so it survives serialization reliably.

**Creating and registering an item:**

```java
// Define a 3x3 shaped crafting recipe (row-major order, left to right, top to bottom)
String[] recipe = {
    "AIR",     "DIAMOND", "AIR",
    "AIR",     "STICK",   "AIR",
    "AIR",     "STICK",   "AIR"
};

// Create a fully configured item
CrucialItem sword = new CrucialItem(
    "Super Sword",           // display name
    Material.DIAMOND_SWORD,  // base material
    List.of("A powerful blade", "Forged by the ancients"),  // lore lines
    recipe,                  // 9-element crafting recipe
    "weapon",                // type/category label
    true,                    // isCraftable - register the crafting recipe
    true,                    // isUsable - allow player interactions
    false                    // isAllowedForCrafting - can't be used as ingredient
);
sword.register();
```

**Fluent builder style:**

```java
CrucialItem pickaxe = new CrucialItem("tool")
    .setName("Magic Pickaxe")
    .setMaterial("DIAMOND_PICKAXE")
    .setRecipe(new String[]{
        "DIAMOND", "DIAMOND", "DIAMOND",
        "AIR",     "STICK",   "AIR",
        "AIR",     "STICK",   "AIR"
    });
pickaxe.setLore(List.of("Mines faster than light"));
pickaxe.isCraftable = true;
pickaxe.isUsable = true;
pickaxe.isAllowedForCrafting = false;
pickaxe.register();
```

**Item flags explained:**

| Flag | Default | Effect |
| ---- | ------- | ------ |
| `isCraftable` | `true` | When `true`, the shaped crafting recipe is registered with the server |
| `isUsable` | `true` | When `false`, player interactions (right-click, left-click) are cancelled by CrucialLib's event listener |
| `isAllowedForCrafting` | `false` | When `false`, this item cannot be used as an ingredient in other crafting recipes |

**Lifecycle methods:**

```java
item.register();    // Register the item and its crafting recipe
item.unregister();  // Remove from registry and remove crafting recipe
item.reload();      // Re-register the crafting recipe (after changing name, recipe, etc.)
item.delete();      // Permanently remove (same as unregister)
```

**Getting an ItemStack to give to a player:**

```java
ItemStack stack = sword.getItemStack();
if (stack != null) {
    player.getInventory().addItem(stack);
}
```

### CrucialHead

`CrucialHead` extends `CrucialItem` for player head items. The head is skinned to a specific player via their UUID.

```java
UUID headOwner = player.getUniqueId();
String[] recipe = {
    "GOLD_INGOT", "GOLD_INGOT", "GOLD_INGOT",
    "GOLD_INGOT", "PLAYER_HEAD", "GOLD_INGOT",
    "GOLD_INGOT", "GOLD_INGOT", "GOLD_INGOT"
};

CrucialHead trophy = new CrucialHead(
    "Trophy",       // display name
    headOwner,      // UUID of the player whose skin to use
    List.of("A champion's trophy"),  // lore
    recipe,         // crafting recipe
    "trophy",       // type
    true,           // isCraftable
    true,           // isUsable
    false           // isAllowedForCrafting
);
trophy.register();
```

### Item Identification

CrucialLib stores a UUID in each item's `PersistentDataContainer`. You can look up items from any `ItemStack`:

```java
// Check if an ItemStack is a CrucialItem and get its UUID
UUID itemId = CrucialItem.getId(itemStack);
if (itemId != null) {
    // It's a CrucialItem
}

// Get the CrucialItem object from an ItemStack
CrucialItem item = CrucialItem.getByStack(player.getInventory().getItemInMainHand());
if (item != null) {
    String name = item.getName();
    String type = item.getType();
}

// Look up by UUID directly
CrucialItem item = CrucialItem.getById(someUUID);

// Iterate all registered items
for (CrucialItem registered : CrucialItem.CRUCIAL_ITEMS) {
    // ...
}
```

### ItemStack Builder

The `Stack` utility class builds `ItemStack` instances with display names, lore, and enchantment glow. All items created via `Stack` have clean tooltips (hidden attributes, enchants, etc.).

```java
// Basic item
ItemStack item = Stack.getStack(Material.DIAMOND_SWORD);

// With display name
ItemStack item = Stack.getStack(Material.DIAMOND_SWORD, "Excalibur");

// With display name and lore
ItemStack item = Stack.getStack(Material.DIAMOND_SWORD, "Excalibur",
    List.of("A legendary sword", "Forged in fire"));

// With enchantment glow effect
ItemStack item = Stack.getStack(Material.DIAMOND_SWORD, "Excalibur",
    List.of("A legendary sword"), true);

// Player head with specific player's skin
ItemStack head = Stack.getStack(playerUUID, "Player Head",
    List.of("Custom head item"));
```

---

## Custom GUIs

Build interactive chest-based GUIs with clickable items, fill materials, and prefab components.

### Page

A `Page` represents a custom inventory GUI. Create a subclass and override `populate()` to define the layout:

```java
Page settingsPage = new Page(3, "Settings", Material.GRAY_STAINED_GLASS_PANE) {
    @Override
    public void populate() {
        // Add a clickable diamond at slot 13
        addItem(new InventoryItem(13, Material.DIAMOND, "Click me",
            List.of("Click to toggle"),
            click -> click.getPlayer().sendMessage("Clicked!")));

        // Add a close button at slot 22
        addItem(new InventoryItem(22, Material.BARRIER, "Close",
            List.of("Click to close"),
            click -> click.getPlayer().closeInventory()));
    }
};

// Open for a player
settingsPage.open(player);
```

**Page constructor parameters:**

| Parameter | Description |
| --------- | ----------- |
| `size` | Number of rows (1-6). Multiplied by 9 for total slots. |
| `title` | Inventory title shown to the player |
| `fillMaterial` | Material used to fill empty slots as background |

**Key methods:**

```java
page.open(player);              // Create and open the inventory
page.reloadInventory();         // Clear and re-populate (call after changing items)
page.addItem(inventoryItem);    // Add a single item
page.addItems(item1, item2);    // Add multiple items
page.addItems(prefab);          // Add items from a prefab component
page.removeItem(inventoryItem); // Remove an item

// Store custom data on a page
page.extraData.put("selectedTab", "general");
String tab = (String) page.extraData.get("selectedTab");

// Static lookups
Page found = Page.get(bukkitInventory);  // Find page by Bukkit Inventory
boolean isPage = Page.exists(inventory); // Check if an inventory is a Page
```

### InventoryItem

An `InventoryItem` is a clickable slot in a `Page`. It has a slot position, an item to display, and an optional click action.

```java
// Full constructor with material, name, lore, and action
InventoryItem button = new InventoryItem(
    13,                                    // slot index
    Material.EMERALD,                      // material
    "Confirm",                             // display name
    List.of("Click to confirm"),           // lore lines
    click -> {                             // action callback
        click.getPlayer().sendMessage("Confirmed!");
        click.getPlayer().closeInventory();
    }
);

// With movability control (default is immovable)
InventoryItem movableItem = new InventoryItem(
    10, Material.DIAMOND, "Draggable", List.of(),
    click -> { /* action */ },
    true  // isMovable - player can pick up this item
);

// From a pre-built ItemStack
ItemStack customStack = Stack.getStack(Material.GOLD_INGOT, "Gold Button", List.of("Shiny"));
InventoryItem fromStack = new InventoryItem(15, customStack, click -> {
    click.getPlayer().sendMessage("Gold clicked!");
});

// Decoration only (no click action)
InventoryItem decoration = new InventoryItem(0, new ItemStack(Material.RED_STAINED_GLASS_PANE));
```

**InventoryItem properties:**

```java
int slot = item.getSlot();
Material mat = item.getMaterial();
ItemStack stack = item.getItem();
String name = item.getName();
List<String> lore = item.getLore();
boolean movable = item.isMovable;

// Store custom data on an item
item.extraData.put("price", 100);
```

### InventoryClick

When a player clicks an `InventoryItem`, the action callback receives an `InventoryClick` with context about the click:

```java
InventoryItem item = new InventoryItem(13, Material.DIAMOND, "Info", List.of(),
    click -> {
        Player player = click.getPlayer();           // Who clicked
        Page page = click.getPage();                  // The page containing this item
        int slot = click.getSlot();                   // Slot that was clicked
        Inventory inv = click.getClickedInventory();  // The Bukkit Inventory
        InventoryClickEvent event = click.getEvent(); // Raw Bukkit event

        // Example: reload the page after a change
        page.reloadInventory();
    }
);
```

### Prefab Components

CrucialLib provides reusable UI components that produce multiple `InventoryItem`s.

**TogglePrefab** -- an ON/OFF toggle button that swaps between two states:

```java
// Default style (green wool = ON, red wool = OFF)
TogglePrefab toggle = new TogglePrefab(
    10,                                    // slot
    click -> enableFeature(),              // action when toggling ON
    click -> disableFeature()              // action when toggling OFF
);
page.addItems(toggle);

// With initial state
TogglePrefab toggleOn = new TogglePrefab(
    10,
    click -> enableFeature(),
    click -> disableFeature(),
    true  // start in ON state
);

// With custom item stacks
TogglePrefab customToggle = new TogglePrefab(
    10,
    Stack.getStack(Material.LIME_DYE, "Enabled"),   // ON item
    Stack.getStack(Material.GRAY_DYE, "Disabled"),   // OFF item
    click -> enableFeature(),
    click -> disableFeature(),
    false  // start in OFF state
);
```

**YesNoButtonsPrefab** -- a Yes/No button pair at two separate slots:

```java
// Default style (green wool = YES, red wool = NO)
YesNoButtonsPrefab yesNo = new YesNoButtonsPrefab(
    11,                          // Yes button slot
    15,                          // No button slot
    click -> confirm(),          // Yes action
    click -> cancel()            // No action
);
page.addItems(yesNo);

// With custom item stacks
YesNoButtonsPrefab customYesNo = new YesNoButtonsPrefab(
    11, 15,
    Stack.getStack(Material.EMERALD, "Accept"),
    Stack.getStack(Material.REDSTONE, "Decline"),
    click -> confirm(),
    click -> cancel()
);
```

---

## Localization

CrucialLib provides key-based localization with YAML files and `{0}`, `{1}`, ... placeholder substitution.

### Setting Up

Create a YAML language file (e.g., `lang.yml`) in your plugin's data folder:

```yaml
# lang.yml
welcome: "Welcome, {0}!"
goodbye: "Goodbye, {0}. You played for {1} hours."
menu_title: "Settings Menu"
no_permission: "You don't have permission to do that."
```

Register the localization source in your plugin's `onEnable()`:

```java
@Override
public void onEnable() {
    // Save default lang.yml from resources if it doesn't exist
    saveResource("lang.yml", false);

    // Register the YAML file as a localization source
    // First parameter is a unique identifier prefix
    try {
        new LocalizedFromYaml("myplugin", getDataFolder(), "lang.yml");
    } catch (IOException e) {
        getLogger().severe("Failed to load language file: " + e.getMessage());
    }
}
```

### Looking Up Strings

Keys use the format `identifier_yamlkey`:

```java
// Simple lookup
String title = Localizer.getLocalizedString("myplugin_menu_title");
// Returns: "Settings Menu"

// With placeholders
String welcome = Localizer.getLocalizedString("myplugin_welcome", player.getName());
// Returns: "Welcome, Steve!"

String goodbye = Localizer.getLocalizedString("myplugin_goodbye", player.getName(), "5");
// Returns: "Goodbye, Steve. You played for 5 hours."
```

### Reloading

You can reload the YAML file at runtime (e.g., on a `/reload` command):

```java
// If you keep a reference to the LocalizedFromYaml instance:
try {
    localizedFromYaml.reloadYaml();
} catch (IOException e) {
    // handle error
}
```

### Custom Localization Sources

You can create your own localization source by extending `Localized`:

```java
public class DatabaseLocalized extends Localized {
    public DatabaseLocalized(String identifier) {
        super(identifier);
    }

    @Override
    public String getLocalizedString(String key) {
        // Look up the key in your database
        return database.getString(key);
    }
}
```

---

## JSON & YAML I/O

### JSON

The `Json` class provides Gson-based JSON serialization with pretty printing:

```java
// Serialize an object to JSON and save to file
MyConfig config = new MyConfig();
Json.saveFile(Json.toJson(config), "config.json");

// Load and deserialize from file
MyConfig loaded = Json.fromJson("config.json", MyConfig.class);

// For generic types (e.g., List<MyObject>)
Type listType = new TypeToken<List<MyObject>>(){}.getType();
List<MyObject> list = Json.fromJson("data.json", listType);

// Raw JSON string operations
String json = Json.toJson(myObject);     // Object -> JSON string
String raw = Json.loadFile("data.json"); // File -> raw JSON string
```

### YAML

The `Yaml` class wraps Bukkit's `YamlConfiguration` with automatic file/directory creation:

```java
// Load a YAML file (creates the file and parent directories if they don't exist)
YamlConfiguration config = Yaml.loadFile(getDataFolder(), "settings.yml");

// Read values
String name = config.getString("name");
int level = config.getInt("level", 1);

// Modify and save
config.set("name", "New Value");
config.set("level", 5);
Yaml.saveFile(config, getDataFolder(), "settings.yml");
```

---

## Player Effects

### Titles & Text

Display title and subtitle text to players:

```java
// Show title with default timing
Interface.showText(player, "Welcome", "Enjoy your stay");

// Show title for a specific duration (seconds)
Interface.showText(player, "Welcome", "Enjoy your stay", 3);

// Show title with custom fade (seconds)
Interface.showText(player, "Welcome", "Enjoy your stay", 3, 1);

// Show title with separate fade in/out (seconds)
Interface.showText(player, "Level Up!", "You reached level 10", 3, 1, 2);

// Clear any displayed title
Interface.clearText(player);
```

### Tablist

Customize the tab list header and footer:

```java
// Set header and footer (each line is an array element)
String[] header = {"My Server", "Welcome!"};
String[] footer = {"Players online: " + Bukkit.getOnlinePlayers().size()};
Interface.setTablistTitle(player, header, footer);

// Remove custom tablist
Interface.removeTablist(player);
```

### Player Names

Customize how a player's name appears in different contexts:

```java
// Set all name displays at once (chat, tab, overhead)
Interface.setName(player, "CustomName");

// Set individual name displays
Interface.setTablistName(player, "TabName");
Interface.setChatName(player, "ChatName");
Interface.setDisplayName(player, "DisplayName");
```

### Blood Screen Effect

Create a red vignette effect on the player's screen using the world border system:

```java
// Set blood effect at 50% intensity
VisualEffects.setBlood(player, 50);

// Set blood effect with auto-removal after 2 seconds
VisualEffects.setBlood(player, 50, 2.0f);

// Manually remove the blood effect
VisualEffects.removeBlood(player);
```

### Particles

Spawn particles at a player's location or a specific location:

```java
// Spawn 1 particle at player's location
VisualEffects.showParticles(player, Particle.HEART);

// Spawn multiple particles at player's location
VisualEffects.showParticles(player, Particle.HEART, 20);

// Spawn particles for a duration (seconds)
VisualEffects.showParticles(player, Particle.FLAME, 10, 5);

// Spawn particles at a specific location
Location loc = new Location(world, x, y, z);
VisualEffects.showParticles(loc, Particle.VILLAGER_HAPPY);
VisualEffects.showParticles(loc, Particle.VILLAGER_HAPPY, 20);
VisualEffects.showParticles(loc, Particle.VILLAGER_HAPPY, 20, 5);
```

---

## Timed Boss Bars

Display a timed boss bar that automatically disappears:

```java
// Show a boss bar to a player
Bossbar.sendBossbar(
    player,           // target player
    "Quest Progress", // text
    BarColor.GREEN,   // color (RED, GREEN, BLUE, YELLOW, PURPLE, PINK, WHITE)
    75f,              // progress (0.0 to 100.0)
    100L              // duration in ticks (20 ticks = 1 second)
);
```

If you call `sendBossbar` again for the same player, the existing bar is updated rather than creating a new one.

---

## Server Utilities

### Version Checking

Verify the server is running a compatible Minecraft version:

```java
@Override
public void onEnable() {
    if (!Server.checkCompatibility("1.21", "1.22")) {
        getLogger().severe("This plugin requires Minecraft 1.21 or 1.22!");
        Bukkit.getPluginManager().disablePlugin(this);
        return;
    }
    // Continue startup...
}
```

`checkCompatibility` returns `true` if any of the given version strings are found in `Bukkit.getVersion()`. It also returns `true` if version detection fails (to avoid false negatives).

### Console Logging

```java
Server.log("Plugin enabled successfully");    // INFO level
Server.error("Something went wrong");         // SEVERE level

// Get the raw server version string
String version = Server.getVersion();
```

---

## Auto-Updater

Automatically check for updates and optionally download them from Spiget:

```java
// Check for updates only
new Updater(this, SPIGET_RESOURCE_ID, getFile(),
    Updater.UpdateType.VERSION_CHECK, true);

// Download updates automatically
new Updater(this, SPIGET_RESOURCE_ID, getFile(),
    Updater.UpdateType.CHECK_DOWNLOAD, true);
```

**UpdateType options:**

| Type | Behavior |
| ---- | -------- |
| `VERSION_CHECK` | Only check if a new version is available and log a message |
| `DOWNLOAD` | Download the update without checking the version first |
| `CHECK_DOWNLOAD` | Check for a new version and download it if available |

The last parameter (`logger`) controls whether update status messages are logged to the console.

---

## bStats Integration

Send custom pie chart metrics via bStats:

```java
Stats stats = new Stats(this, BSTATS_PLUGIN_ID);
stats.sendPieChart("server_type", "survival");
stats.sendPieChart("game_mode", "creative");
```

---

## Auto-Download Pattern

You can auto-download CrucialLib in your plugin's `onLoad()` so server admins don't need to install it manually:

```java
private static final String CRUCIAL_LIB_VERSION = "3.0.1";

@Override
public void onLoad() {
    if (getServer().getPluginManager().getPlugin("CrucialLib") == null) {
        try {
            URL url = new URL(
                "https://github.com/ChafficPlugins/CrucialLib/releases/download/v"
                + CRUCIAL_LIB_VERSION + "/CrucialLib-v" + CRUCIAL_LIB_VERSION + ".jar"
            );
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("plugins/CrucialLib.jar");
            fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
            fos.close();
            Bukkit.getPluginManager().loadPlugin(new File("plugins/CrucialLib.jar"));
        } catch (IOException | InvalidDescriptionException
                 | InvalidPluginException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
}
```

---

## Full Example Plugin

A complete plugin that uses version checking, custom items, a GUI, and localization:

```java
public class MyPlugin extends JavaPlugin {

    private CrucialItem magicSword;

    @Override
    public void onEnable() {
        // Check server version
        if (!Server.checkCompatibility("1.21")) {
            Server.error("MyPlugin requires Minecraft 1.21+!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Set up localization
        saveResource("lang.yml", false);
        try {
            new LocalizedFromYaml("myplugin", getDataFolder(), "lang.yml");
        } catch (IOException e) {
            Server.error("Failed to load language file!");
        }

        // Register a custom item
        magicSword = new CrucialItem(
            "Magic Sword", Material.DIAMOND_SWORD,
            List.of("A blade of pure magic"),
            new String[]{
                "AIR",     "DIAMOND",  "AIR",
                "AIR",     "BLAZE_ROD","AIR",
                "AIR",     "BLAZE_ROD","AIR"
            },
            "weapon", true, true, false
        );
        magicSword.register();

        // Set up bStats
        new Stats(this, 12345);

        Server.log("MyPlugin enabled!");
    }

    // Command handler that opens a settings GUI
    public void openSettings(Player player) {
        Page settings = new Page(3, "Settings", Material.GRAY_STAINED_GLASS_PANE) {
            @Override
            public void populate() {
                // Toggle button
                addItems(new TogglePrefab(11,
                    click -> click.getPlayer().sendMessage("Feature ON"),
                    click -> click.getPlayer().sendMessage("Feature OFF")
                ));

                // Info item
                addItem(new InventoryItem(13, Material.BOOK, "Info",
                    List.of(Localizer.getLocalizedString("myplugin_menu_title")),
                    click -> {}
                ));

                // Close button
                addItem(new InventoryItem(15, Material.BARRIER, "Close", List.of(),
                    click -> click.getPlayer().closeInventory()
                ));
            }
        };
        settings.open(player);
    }

    // Give the custom sword to a player
    public void giveSword(Player player) {
        ItemStack stack = magicSword.getItemStack();
        if (stack != null) {
            player.getInventory().addItem(stack);
            String msg = Localizer.getLocalizedString("myplugin_welcome", player.getName());
            player.sendMessage(msg);
        }
    }
}
```

---

## Javadoc

For full method-level API reference documentation, see the [Javadoc](javadoc.md) page.
