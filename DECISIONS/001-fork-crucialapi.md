# ADR-001: Fork CrucialAPI as CrucialLib

## Status
Accepted

## Context
CrucialAPI (github.com/Chafficui/CrucialAPI) is no longer actively maintained. Multiple ChafficPlugins (MyTrip, MiningLevels) depend on it as a shared library for custom items, localization, GUI utilities, and more. Without maintenance, bugs go unfixed and the library cannot be updated for newer Spigot versions.

## Decision
Fork CrucialAPI as CrucialLib under the ChafficPlugins GitHub organization. The fork will be maintained by the ChafficPlugins team.

## Consequences
- **We are responsible for maintenance** — bug fixes, Spigot version updates, and new features
- **plugin.yml name stays "CrucialLib"** — the Maven artifact is `CrucialLib` and the plugin name matches
- **Distribution moves to JitPack** via the ChafficPlugins/CrucialLib repo
- **Existing plugins need to update their dependency coordinates** from the old CrucialAPI to the new CrucialLib JitPack coordinates
- **The package structure remains `io.github.chafficui.CrucialLib`** for binary compatibility
