# Migration Guide

## Migrating from CrucialLib 2.x to 3.0.0

CrucialLib 3.0.0 is a **major version** with breaking changes. It requires **Java 21** and **Spigot 1.21+**.

### For server admins

1. Ensure your server is running **Spigot/Paper 1.21** or higher on **Java 21**
2. Remove the old CrucialLib (or CrucialAPI) jar from your `plugins/` folder
3. Download the [CrucialLib v3.0.0 release](https://github.com/ChafficPlugins/CrucialLib/releases/latest)
4. Place the new jar in `plugins/`
5. Restart the server

### For developers

1. **Update your dependency version** to `v3.0.0`:

    **Maven:**
    ```xml
    <dependency>
        <groupId>com.github.ChafficPlugins</groupId>
        <artifactId>CrucialLib</artifactId>
        <version>v3.0.0</version>
    </dependency>
    ```

    **Gradle:**
    ```groovy
    implementation 'com.github.ChafficPlugins:CrucialLib:v3.0.0'
    ```

2. **Update your Java source/target to 21** in your build configuration.

3. **API renames** — the following Spigot API constants changed in 1.21 and CrucialLib follows suit:

    | Old (2.x)                          | New (3.0.0)                          |
    | ---------------------------------- | ------------------------------------ |
    | `Attribute.GENERIC_MAX_HEALTH`     | `Attribute.MAX_HEALTH`               |
    | `Enchantment.DURABILITY`           | `Enchantment.UNBREAKING`             |
    | `ItemFlag.HIDE_POTION_EFFECTS`     | `ItemFlag.HIDE_ADDITIONAL_TOOLTIP`   |

4. **Method rename** — `Server.checkVersion()` is now `Server.checkCompatibility()` and accepts varargs instead of a `String[]`:

    ```java
    // Old
    Server.checkVersion(new String[]{"1.16", "1.15"})

    // New
    Server.checkCompatibility("1.21")
    ```

5. **Minimum server version** — CrucialLib 3.0.0 targets `api-version: 1.21`. Servers running older versions are not supported.

## Migrating from CrucialAPI to CrucialLib

CrucialLib is a direct fork of CrucialAPI. If you're migrating from the original CrucialAPI:

### For server admins

1. Remove the old `CrucialAPI` jar from your `plugins/` folder
2. Download the latest [CrucialLib release](https://github.com/ChafficPlugins/CrucialLib/releases/latest)
3. Place the new jar in `plugins/`
4. Restart the server

No configuration changes are needed — existing configs will continue to work.

### For developers

1. **Update your Maven or Gradle coordinates** to use JitPack with the new repository:

    **Maven:**
    ```xml
    <dependency>
        <groupId>com.github.ChafficPlugins</groupId>
        <artifactId>CrucialLib</artifactId>
        <version>v3.0.0</version>
    </dependency>
    ```

    **Gradle:**
    ```groovy
    implementation 'com.github.ChafficPlugins:CrucialLib:v3.0.0'
    ```

2. **Update `plugin.yml`** — change the dependency name to `CrucialLib`:

    ```yaml
    depend: [CrucialLib]
    ```

3. **Update package imports** — replace `io.github.chafficui.CrucialAPI` with `io.github.chafficui.CrucialLib` in your code.

## Version history

| Version | Changes |
| ------- | ------- |
| 3.0.0   | Java 21, Spigot 1.21+, API modernization (see breaking changes above) |
| 2.2.0   | Last CrucialAPI version, forked as CrucialLib |
