# ADR-002: Update to Spigot 1.21.4 and Java 21

## Status
Accepted

## Context
CrucialLib was targeting Spigot 1.20.1 with Java 1.8 (source/target). Modern Minecraft servers run 1.21.x, and Java 21 is the current LTS release. The older configuration prevents use of modern Java features and newer Spigot API additions.

## Decision
Update to Paper API 1.21 and Java 21 as the minimum compiler source/target. Paper API is used instead of Spigot API because MockBukkit v1.21 is built against Paper. Paper API is a superset of Spigot API, so this is fully backwards-compatible with Spigot-based plugins.

## API Changes Required

The following breaking API changes in Spigot 1.21.x required code updates:

1. **`Attribute.GENERIC_MAX_HEALTH` → `Attribute.MAX_HEALTH`**
   - Spigot 1.21.2 removed the `GENERIC_` prefix from attribute constants
   - Affected files: `CrucialItem.java`, `CrucialHead.java`, `InventoryItem.java`

2. **`Enchantment.DURABILITY` → `Enchantment.UNBREAKING`**
   - The enchantment constant was renamed to match its in-game name
   - Affected file: `Stack.java`

3. **`ItemFlag.HIDE_POTION_EFFECTS` → `ItemFlag.HIDE_ADDITIONAL_TOOLTIP`**
   - The item flag was renamed in Spigot 1.20.5
   - Affected file: `Stack.java`

## Consequences
- **Minimum Java version is now 21** — servers must run Java 21+
- **Minimum Spigot version is 1.21** — the `api-version` in plugin.yml is set to `1.21`
- **NMS-dependent classes (`Border`, `Package`) may need further updates** — these use reflection to access CraftBukkit/NMS internals which change between versions
- **Dependent plugins must also use Java 21** to compile against this library
- **MockBukkit v1.21 is now used for testing** (added as test dependency)
- **Paper API is used instead of Spigot API** — MockBukkit requires Paper API classes; Paper is a superset of Spigot so all Spigot-based code continues to work
- **`Main` class is no longer `final`** — MockBukkit needs to create a proxy subclass via ByteBuddy
- **`Plugin.getVersion()` uses `Bukkit.getPluginManager()`** instead of `JavaPlugin.getPlugin()` for compatibility with MockBukkit's test classloader
- **CI pipeline runs on Java 21** with `mvn verify`
