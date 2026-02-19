# Architecture

## Overview

CrucialLib is a shared Spigot plugin library that provides common utilities for ChafficPlugins. It runs as a Bukkit plugin on the server and exposes its API to other plugins that declare it as a dependency.

## High-Level Architecture

```
┌──────────────────────────────────────────────────────┐
│                  Spigot Server                        │
│                                                      │
│  ┌──────────────┐   depends on   ┌───────────────┐  │
│  │  MyTrip       │──────────────▶│               │  │
│  └──────────────┘                │  CrucialLib   │  │
│  ┌──────────────┐   depends on   │               │  │
│  │ MiningLevels  │──────────────▶│  (this repo)  │  │
│  └──────────────┘                │               │  │
│  ┌──────────────┐   depends on   │               │  │
│  │ Other Plugin  │──────────────▶│               │  │
│  └──────────────┘                └───────────────┘  │
│                                                      │
│                   Spigot API                          │
└──────────────────────────────────────────────────────┘
```

## Package Structure

### `io.github.chafficui.CrucialLib`
- **Main.java** — Plugin entry point. Handles lifecycle (enable/disable), config setup, event registration, and bStats initialization.

### `io.github.chafficui.CrucialLib.Events`
- **CrucialItemEvents** — Bukkit event listeners that enforce CrucialItem properties (usability, crafting restrictions) by intercepting interact and crafting events.

### `io.github.chafficui.CrucialLib.Utils`
- **Plugin** — Static helper to retrieve the plugin version from the plugin description.
- **Server** — Server version checking (`checkCompatibility`) and logging (`log`, `error`).
- **Stats** — Wrapper around bStats for sending custom pie chart data.

### `io.github.chafficui.CrucialLib.Utils.api`
- **BStats** — Embedded bStats metrics client (sends usage stats to bstats.org).
- **Border** — Sends per-player world border packets via NMS reflection (for blood/damage screen effects).
- **Bossbar** — Displays timed boss bars to players using Bukkit's BossBar API.
- **Package** — NMS reflection utilities (getHandle, sendPacket, getNmsClass).
- **Title** — Sends title/subtitle text to players.
- **Updater** — Checks Spiget API for new versions and optionally auto-downloads updates.

### `io.github.chafficui.CrucialLib.Utils.customItems`
- **CrucialItem** — Custom item definition with UUID identification, material, lore, crafting recipe registration/unregistration. Items are tracked in a global `CRUCIAL_ITEMS` set.
- **CrucialHead** — Extends CrucialItem for player head items with a head owner UUID.
- **Item** — Static helpers for creating shaped crafting recipes and registering them with Bukkit.
- **Stack** — ItemStack builder with convenience methods for display names, lore, enchantments, attribute modifiers, and hidden item flags.

### `io.github.chafficui.CrucialLib.Utils.localization`
- **Localized** — Abstract base for localization sources. Registers itself in the global Localizer on construction.
- **LocalizedFromYaml** — YAML-file-backed localization source.
- **Localizer** — Global resolver. Keys use `identifier_restOfKey` format. Supports `{0}`, `{1}` placeholder substitution.

### `io.github.chafficui.CrucialLib.Utils.player.effects`
- **Interface** — Title/subtitle display, tablist header/footer, and player name utilities.
- **VisualEffects** — Blood screen effect (via world border packets) and particle spawning.

### `io.github.chafficui.CrucialLib.Utils.player.inventory`
- **Page** — Custom GUI page backed by a Bukkit Inventory. Supports fill material, clickable items, and item management.
- **InventoryItem** — A slot in a custom GUI with a material, action callback, and movability flag.
- **InventoryClick** — Wrapper around InventoryClickEvent providing the Page context.
- **InventoryListener** — Bukkit event listener that routes inventory events to the Page system.

### `io.github.chafficui.CrucialLib.Utils.player.inventory.prefabs`
- **InventoryItemPrefab** — Interface for reusable inventory item groups.
- **TogglePrefab** — ON/OFF toggle button.
- **YesNoButtonsPrefab** — Yes/No button pair.

### `io.github.chafficui.CrucialLib.exceptions`
- **CrucialException** — Maps numeric error codes to descriptive messages.

### `io.github.chafficui.CrucialLib.io`
- **Json** — Gson-based JSON serialization, deserialization, and file I/O.
- **Yaml** — Bukkit YamlConfiguration file loading and saving.

## Data Flow: How Plugins Use CrucialLib

1. A plugin declares CrucialLib as a dependency in its `plugin.yml`
2. Optionally, the plugin auto-downloads CrucialLib in `onLoad()` if not present
3. In `onEnable()`, the plugin calls CrucialLib utilities directly via static methods (e.g., `Server.checkCompatibility(...)`, `new CrucialItem(...)`)
4. CrucialLib's event listeners enforce custom item behavior (usability, crafting) automatically

## Custom Item Identification

CrucialItems are identified by a UUID stored as a Bukkit `AttributeModifier` on the `MAX_HEALTH` attribute with the name `"CRUCIALITEM_ID"`. This allows identification of custom items from any ItemStack without additional NBT or persistent data.

## Auto-Download Mechanism

Plugins can auto-download CrucialLib in their `onLoad()` method by:
1. Checking if CrucialLib is already loaded (`getPluginManager().getPlugin("CrucialLib")`)
2. Downloading the JAR from GitHub releases to the `plugins/` directory
3. Loading it via `Bukkit.getPluginManager().loadPlugin()`

## Version Checking

The `Updater` class checks for new versions via the Spiget API (`api.spiget.org/v2/resources/`). It supports version-only checks, direct downloads, and check-then-download modes.

## External Dependencies

- **Spigot API** (provided) — The Minecraft server API
- **Gson** (shaded) — JSON serialization
- **bStats** (embedded) — Anonymous usage metrics
