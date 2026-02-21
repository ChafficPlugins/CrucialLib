# Setup

## Server Admins

CrucialLib is a shared library plugin -- it must be installed on your server for any plugin that depends on it to work.

### Requirements

- **Spigot** or **Paper** server version **1.21** or higher
- **Java 21** or higher

### Installation

1. Download the latest CrucialLib jar from [Releases](https://github.com/ChafficPlugins/CrucialLib/releases/latest)
2. Place the jar in your server's `plugins/` folder
3. Restart the server

CrucialLib will create a `plugins/CrucialLib/` folder with a `config.yml` on first startup. No manual configuration is needed.

### Verifying Installation

After starting the server, you should see:

```
CrucialLib is now enabled (Version: 3.0.1) made by [ChafficPlugins].
```

You can also check with `/plugins` -- CrucialLib should appear in green.

---

## Developers

CrucialLib is distributed via [JitPack](https://jitpack.io/#ChafficPlugins/CrucialLib). You need to add the JitPack repository and the CrucialLib dependency to your build tool, then declare CrucialLib as a plugin dependency.

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

Then add the dependency with `provided` scope (CrucialLib is supplied by the server at runtime):

```xml
<dependency>
    <groupId>com.github.ChafficPlugins</groupId>
    <artifactId>CrucialLib</artifactId>
    <version>v3.0.1</version>
    <scope>provided</scope>
</dependency>
```

### Gradle (Groovy)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.ChafficPlugins:CrucialLib:v3.0.1'
}
```

### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.ChafficPlugins:CrucialLib:v3.0.1")
}
```

### plugin.yml

Add CrucialLib as a dependency in your `plugin.yml` so the server loads it before your plugin:

```yaml
name: MyPlugin
version: 1.0.0
main: com.example.myplugin.Main
depend: [CrucialLib]
```

If CrucialLib is optional for your plugin, use `softdepend` instead:

```yaml
softdepend: [CrucialLib]
```

### Importing Classes

All CrucialLib classes are under the `io.github.chafficui.CrucialLib` package:

```java
import io.github.chafficui.CrucialLib.Utils.customItems.CrucialItem;
import io.github.chafficui.CrucialLib.Utils.customItems.CrucialHead;
import io.github.chafficui.CrucialLib.Utils.customItems.Stack;
import io.github.chafficui.CrucialLib.Utils.player.inventory.Page;
import io.github.chafficui.CrucialLib.Utils.player.inventory.InventoryItem;
import io.github.chafficui.CrucialLib.Utils.player.inventory.InventoryClick;
import io.github.chafficui.CrucialLib.Utils.player.inventory.prefabs.TogglePrefab;
import io.github.chafficui.CrucialLib.Utils.player.inventory.prefabs.YesNoButtonsPrefab;
import io.github.chafficui.CrucialLib.Utils.localization.LocalizedFromYaml;
import io.github.chafficui.CrucialLib.Utils.localization.Localizer;
import io.github.chafficui.CrucialLib.Utils.Server;
import io.github.chafficui.CrucialLib.Utils.Stats;
import io.github.chafficui.CrucialLib.Utils.api.Bossbar;
import io.github.chafficui.CrucialLib.Utils.api.Updater;
import io.github.chafficui.CrucialLib.Utils.player.effects.Interface;
import io.github.chafficui.CrucialLib.Utils.player.effects.VisualEffects;
import io.github.chafficui.CrucialLib.io.Json;
import io.github.chafficui.CrucialLib.io.Yaml;
```

### Next Steps

See the [API Guide](api-guide.md) for code examples covering all CrucialLib features.
