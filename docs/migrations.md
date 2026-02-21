# Migration Guide

## Migrating from CrucialLib 3.0.0 to 3.0.1

CrucialLib 3.0.1 is a **patch release** with important bug fixes for Bukkit 1.21+ compatibility. No API changes are required for most plugins.

### What Changed

1. **Custom item identification migrated from AttributeModifier to PersistentDataContainer**

   CrucialLib 3.0.0 identified custom items by attaching an `AttributeModifier` on `Attribute.MAX_HEALTH` with the item's UUID encoded in the modifier name. This broke on Bukkit 1.21.4+ because the `AttributeModifier(UUID, String, double, Operation)` constructor and `Attribute.MAX_HEALTH` were deprecated and the modifier name/UUID were no longer reliably preserved.

   CrucialLib 3.0.1 stores the item UUID in the `PersistentDataContainer` instead, which is stable across all Bukkit versions since 1.14.

   **Impact:** Items created with 3.0.0 that are already in player inventories will not be recognized by 3.0.1's `CrucialItem.getByStack()` and `CrucialItem.getId()` methods, since they still carry the old AttributeModifier-based ID. Newly created items will use the PDC format and work correctly.

2. **InventoryItem also migrated from AttributeModifier to PersistentDataContainer**

   `InventoryItem` used the same AttributeModifier hack to mark items as GUI elements. This has been replaced with a PDC byte marker.

3. **`Stack.addAttributeModifier()` deprecated**

   The method still exists for backwards compatibility but is marked `@Deprecated(since = "3.0.0")`. Use `CrucialItem` for custom item identification or `InventoryItem` for GUI items instead.

4. **NamespacedKey lowercase enforcement**

   `Item.createItem()` now lowercases the recipe key and name to comply with Bukkit 1.21+'s `NamespacedKey` validation rules (`[a-z0-9/._-]` only). Previously, uppercase characters in item names or types could cause `IllegalArgumentException`.

### For Server Admins

1. Replace `CrucialLib-v3.0.0.jar` with `CrucialLib-v3.0.1.jar` in `plugins/`
2. Restart the server
3. Existing custom items in player inventories (created with 3.0.0) will need to be re-created

### For Developers

1. Update your dependency version to `v3.0.1`:

    **Maven:**
    ```xml
    <dependency>
        <groupId>com.github.ChafficPlugins</groupId>
        <artifactId>CrucialLib</artifactId>
        <version>v3.0.1</version>
        <scope>provided</scope>
    </dependency>
    ```

    **Gradle:**
    ```groovy
    compileOnly 'com.github.ChafficPlugins:CrucialLib:v3.0.1'
    ```

2. If you were calling `Stack.addAttributeModifier()` directly, migrate to `CrucialItem` or `InventoryItem` (which handle identification automatically).

3. No other code changes required -- the `CrucialItem` and `InventoryItem` APIs are unchanged.

---

## Migrating from CrucialLib 2.x to 3.0.x

CrucialLib 3.0.0 is a **major version** with breaking changes. It requires **Java 21** and **Spigot 1.21+**.

### For Server Admins

1. Ensure your server is running **Spigot/Paper 1.21** or higher on **Java 21**
2. Remove the old CrucialLib (or CrucialAPI) jar from `plugins/`
3. Download the [latest CrucialLib release](https://github.com/ChafficPlugins/CrucialLib/releases/latest)
4. Place the new jar in `plugins/`
5. Restart the server

### For Developers

1. **Update your dependency version** to `v3.0.1`:

    **Maven:**
    ```xml
    <dependency>
        <groupId>com.github.ChafficPlugins</groupId>
        <artifactId>CrucialLib</artifactId>
        <version>v3.0.1</version>
        <scope>provided</scope>
    </dependency>
    ```

    **Gradle:**
    ```groovy
    compileOnly 'com.github.ChafficPlugins:CrucialLib:v3.0.1'
    ```

2. **Update your Java source/target to 21** in your build configuration.

3. **API renames** -- the following Spigot API constants changed in 1.21 and CrucialLib follows suit:

    | Old (2.x) | New (3.0.x) |
    | --------- | ----------- |
    | `Attribute.GENERIC_MAX_HEALTH` | `Attribute.MAX_HEALTH` |
    | `Enchantment.DURABILITY` | `Enchantment.UNBREAKING` |
    | `ItemFlag.HIDE_POTION_EFFECTS` | `ItemFlag.HIDE_ADDITIONAL_TOOLTIP` |

4. **Method rename** -- `Server.checkVersion()` is now `Server.checkCompatibility()` and accepts varargs instead of a `String[]`:

    ```java
    // Old (2.x)
    Server.checkVersion(new String[]{"1.16", "1.15"})

    // New (3.0.x)
    Server.checkCompatibility("1.21")
    ```

5. **Minimum server version** -- CrucialLib 3.0.x targets `api-version: 1.21`. Servers running older versions are not supported.

6. **Item identification** -- If you were reading AttributeModifiers to identify CrucialItems, use the official API instead:

    ```java
    // Use the official lookup methods
    CrucialItem item = CrucialItem.getByStack(itemStack);
    UUID itemId = CrucialItem.getId(itemStack);
    ```

---

## Migrating from CrucialAPI to CrucialLib

CrucialLib is a direct fork of CrucialAPI. If you're migrating from the original CrucialAPI:

### For Server Admins

1. Remove the old `CrucialAPI` jar from `plugins/`
2. Download the latest [CrucialLib release](https://github.com/ChafficPlugins/CrucialLib/releases/latest)
3. Place the new jar in `plugins/`
4. Restart the server

No configuration changes are needed -- existing configs will continue to work.

### For Developers

1. **Update your Maven or Gradle coordinates** to use JitPack with the new repository:

    **Maven:**
    ```xml
    <dependency>
        <groupId>com.github.ChafficPlugins</groupId>
        <artifactId>CrucialLib</artifactId>
        <version>v3.0.1</version>
        <scope>provided</scope>
    </dependency>
    ```

    **Gradle:**
    ```groovy
    compileOnly 'com.github.ChafficPlugins:CrucialLib:v3.0.1'
    ```

2. **Update `plugin.yml`** -- change the dependency name to `CrucialLib`:

    ```yaml
    depend: [CrucialLib]
    ```

3. **Update package imports** -- replace `io.github.chafficui.CrucialAPI` with `io.github.chafficui.CrucialLib` in your code.

---

## Version History

| Version | Changes |
| ------- | ------- |
| 3.0.1   | Bug fix: migrated item identification from AttributeModifier to PersistentDataContainer for Bukkit 1.21+ compatibility; fixed NamespacedKey lowercase enforcement; deprecated `Stack.addAttributeModifier()`; comprehensive Javadoc added to all public classes |
| 3.0.0   | Java 21, Spigot 1.21+, API modernization (see breaking changes above) |
| 2.2.0   | Last CrucialAPI version, forked as CrucialLib |
