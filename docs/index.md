# CrucialLib

[![](https://jitpack.io/v/ChafficPlugins/CrucialLib.svg)](https://jitpack.io/#ChafficPlugins/CrucialLib)

CrucialLib is an open-source Spigot library that simplifies plugin development. It is maintained under [ChafficPlugins](https://github.com/ChafficPlugins) as a fork of the no longer maintained [CrucialAPI](https://github.com/Chafficui/CrucialAPI).

## Features

| Feature | Description |
| ------- | ----------- |
| [Custom Items](api-guide.md#custom-items) | Items with unique IDs, crafting recipes, and interaction control |
| [Custom GUIs](api-guide.md#custom-guis) | Clickable inventory UIs with prefab components (toggles, yes/no buttons) |
| [Localization](api-guide.md#localization) | Key-based translations with YAML files and `{0}` placeholder substitution |
| [JSON & YAML I/O](api-guide.md#json--yaml-io) | One-line file read/write for JSON objects and Bukkit YAML configs |
| [Player Effects](api-guide.md#player-effects) | Titles, tablist customization, blood screen effects, particles |
| [Boss Bars](api-guide.md#timed-boss-bars) | Timed boss bars with auto-removal |
| [Server Utilities](api-guide.md#server-utilities) | Version compatibility checking and console logging |
| [Auto-Updater](api-guide.md#auto-updater) | Automatic update checking and downloading via Spiget |
| [bStats](api-guide.md#bstats-integration) | Pie chart metrics wrapper |

## Requirements

- **Java 21** or higher
- **Spigot/Paper 1.21** or higher

## Quick Start

1. Add CrucialLib as a [dependency in your build tool](setup.md#developers)
2. Add `depend: [CrucialLib]` to your `plugin.yml`
3. Use any CrucialLib API in your plugin -- see the [API Guide](api-guide.md)

## Documentation

- [Setup Guide](setup.md) -- Installation for server admins and developers
- [API Guide](api-guide.md) -- Code examples for all major APIs
- [Javadoc](javadoc.md) -- Generated API reference
- [Migration Guide](migrations.md) -- Upgrading from CrucialAPI or older versions

## Supported Versions

| Version | Supported |
| ------- | --------- |
| 3.0.x   | Yes       |
| 2.2.x   | No        |
| 2.1.x   | No        |
| 2.0.x   | No        |
| 1.x.x   | No        |

## Links

- **Source code**: [github.com/ChafficPlugins/CrucialLib](https://github.com/ChafficPlugins/CrucialLib)
- **Releases**: [GitHub Releases](https://github.com/ChafficPlugins/CrucialLib/releases)
- **License**: [GPL-3.0](https://github.com/ChafficPlugins/CrucialLib/blob/master/LICENSE)
- **Discord**: [ChafficPlugins Discord](https://discord.gg/ARxYx4Z)
- **Discussions**: [GitHub Discussions](https://github.com/ChafficPlugins/CrucialLib/discussions)
