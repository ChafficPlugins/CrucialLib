# CLAUDE.md - CrucialLib

## Project Overview

CrucialLib is a shared Spigot library used by all ChafficPlugins (MyTrip, MiningLevels, etc.). It is a fork of the no longer maintained CrucialAPI (github.com/Chafficui/CrucialAPI). The plugin registers as "CrucialLib" but maintains backwards compatibility with plugins that depend on CrucialAPI.

## Build & Test

```bash
# Build the project (produces JAR in target/)
mvn clean package

# Run tests only
mvn test

# Full verification (compile + test + package)
mvn clean verify
```

**Java version:** 21 (minimum)
**Build tool:** Maven

## Project Structure

```
src/main/java/io/github/chafficui/CrucialLib/
├── Main.java                          # Plugin entry point (extends JavaPlugin)
├── Events/
│   └── CrucialItemEvents.java         # Event listeners for custom item interactions & crafting
├── Utils/
│   ├── Plugin.java                    # Static accessor for plugin version
│   ├── Server.java                    # Server version checking & logging utilities
│   ├── Stats.java                     # bStats pie chart wrapper
│   ├── api/
│   │   ├── BStats.java               # Embedded bStats metrics client
│   │   ├── Border.java               # Per-player world border via Bukkit API
│   │   ├── Bossbar.java              # Timed boss bar display utility
│   │   ├── Package.java              # NMS reflection helpers (getHandle, sendPacket)
│   │   ├── Title.java                # Title/subtitle sender
│   │   └── Updater.java              # Auto-update checker via Spiget API
│   ├── customItems/
│   │   ├── CrucialItem.java          # Custom item with crafting recipe registration
│   │   ├── CrucialHead.java          # Custom player head item (extends CrucialItem)
│   │   ├── Item.java                 # Recipe creation helpers
│   │   └── Stack.java                # ItemStack builder utilities
│   ├── localization/
│   │   ├── Localized.java            # Abstract localization source
│   │   ├── LocalizedFromYaml.java    # YAML-based localization
│   │   └── Localizer.java            # Global localization resolver with placeholder support
│   └── player/
│       ├── effects/
│       │   ├── Interface.java        # Title/tablist/name display utilities
│       │   └── VisualEffects.java    # Blood effect (border) and particle utilities
│       └── inventory/
│           ├── InventoryClick.java    # Click event wrapper for custom inventories
│           ├── InventoryItem.java     # Clickable inventory slot with action callback
│           ├── InventoryListener.java # Bukkit event listener for custom inventories
│           ├── Page.java             # Custom GUI page with fill material & items
│           └── prefabs/
│               ├── InventoryItemPrefab.java # Interface for inventory item groups
│               ├── TogglePrefab.java        # ON/OFF toggle button prefab
│               └── YesNoButtonsPrefab.java  # Yes/No button pair prefab
├── exceptions/
│   └── CrucialException.java         # Error code-based exception
└── io/
    ├── Json.java                      # Gson-based JSON file I/O
    └── Yaml.java                      # Bukkit YAML file I/O
```

## Key Classes

- **Main**: Plugin lifecycle (onEnable/onDisable), registers events, sets up config and bStats
- **CrucialItem**: Core custom item system — items are identified by a UUID stored in the PersistentDataContainer. Items can register/unregister shaped crafting recipes
- **Stack**: Builder for ItemStack creation with display names, lore, enchantments, attribute modifiers, and hidden item flags
- **Server**: Version compatibility checking via `Bukkit.getVersion().contains(...)`, plus logging
- **Localizer**: Key-based localization with `identifier_key` format and `{0}`, `{1}` placeholder substitution
- **Page/InventoryItem**: Custom GUI system with clickable items, fill materials, and prefab components

## Important Notes

- This is a **library** — API stability matters. Don't break existing public methods without a MAJOR version bump.
- The `Package` class uses NMS reflection, which is version-specific and may not work on all server implementations.
- Custom items use a UUID stored in the `PersistentDataContainer` as their identification mechanism.
- CI must pass before merging any PR.
- Distribution is via JitPack (create a GitHub release/tag to publish).

## Adding New Utilities

1. Place new utility classes in the appropriate subpackage under `Utils/`
2. Follow existing naming conventions
3. Every new public method needs a corresponding test in `src/test/java/`
4. Use `Server.log()` and `Server.error()` for console output, not `System.out`

## Dependencies

- **Paper API 1.21** (provided scope — server supplies this at runtime; Paper API is a superset of Spigot API, used because MockBukkit requires it)
- **Gson 2.10.1** (shaded into the JAR)
- **JUnit 5 + MockBukkit** (test scope only)
