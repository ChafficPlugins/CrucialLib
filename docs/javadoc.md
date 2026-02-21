# Javadoc

CrucialLib provides full Javadoc documentation for all public classes and methods.

## Generating Javadoc Locally

Run the following Maven command from the project root:

```bash
mvn javadoc:javadoc
```

The generated HTML documentation will be in:

```
target/site/apidocs/index.html
```

Open `index.html` in a browser to view the API reference.

## Generating a Javadoc JAR

To generate a JAR file containing the Javadoc (useful for IDE integration):

```bash
mvn javadoc:jar
```

The JAR will be in `target/`.

## CI/CD

Javadoc is automatically generated on each GitHub release via the [Generate Javadoc workflow](../.github/workflows/javadoc.yml).

## Package Overview

| Package | Description |
| ------- | ----------- |
| `io.github.chafficui.CrucialLib` | Plugin entry point (`Main`) |
| `io.github.chafficui.CrucialLib.Events` | Event listeners for custom item interactions and crafting |
| `io.github.chafficui.CrucialLib.Utils` | Server utilities, plugin metadata, bStats wrapper |
| `io.github.chafficui.CrucialLib.Utils.api` | Low-level APIs: world border, boss bars, NMS packets, titles, auto-updater |
| `io.github.chafficui.CrucialLib.Utils.customItems` | Custom item system: `CrucialItem`, `CrucialHead`, `Stack`, recipe creation |
| `io.github.chafficui.CrucialLib.Utils.localization` | Key-based localization with YAML support |
| `io.github.chafficui.CrucialLib.Utils.player.effects` | Title display, tablist customization, blood effects, particles |
| `io.github.chafficui.CrucialLib.Utils.player.inventory` | Custom GUI system: `Page`, `InventoryItem`, click handling |
| `io.github.chafficui.CrucialLib.Utils.player.inventory.prefabs` | Reusable GUI components: toggles, yes/no buttons |
| `io.github.chafficui.CrucialLib.exceptions` | Error code-based exception (`CrucialException`) |
| `io.github.chafficui.CrucialLib.io` | JSON and YAML file I/O utilities |

## Key Classes

| Class | Description |
| ----- | ----------- |
| `CrucialItem` | Custom item with UUID identification via PersistentDataContainer, crafting recipes, and interaction flags |
| `CrucialHead` | Player head variant of CrucialItem, skinned to a specific player |
| `Stack` | ItemStack builder with display names, lore, enchantment glow, and clean tooltips |
| `Page` | Custom chest GUI with fill material, clickable items, and prefab support |
| `InventoryItem` | Clickable slot in a Page with action callbacks and PersistentDataContainer marker |
| `InventoryClick` | Click event context passed to InventoryItem action callbacks |
| `TogglePrefab` | ON/OFF toggle button prefab for Page GUIs |
| `YesNoButtonsPrefab` | Yes/No button pair prefab for Page GUIs |
| `Localizer` | Global localization resolver with `identifier_key` format and placeholder substitution |
| `LocalizedFromYaml` | YAML-backed localization source |
| `Json` | Gson-based JSON serialization and file I/O |
| `Yaml` | Bukkit YAML configuration file I/O with auto-creation |
| `Server` | Version compatibility checking and console logging |
| `Bossbar` | Timed boss bar display utility |
| `Updater` | Auto-update checker and downloader via Spiget API |
| `Interface` | Title, tablist, and player name display utilities |
| `VisualEffects` | Blood screen effect and particle spawning |
| `Stats` | bStats pie chart metrics wrapper |

For usage examples, see the [API Guide](api-guide.md).
