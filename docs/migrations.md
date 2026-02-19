# Migration Guide

## Migrating from CrucialAPI to CrucialLib

CrucialLib is a direct fork of CrucialAPI. The migration is straightforward with no breaking changes.

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
        <version>TAG</version>
    </dependency>
    ```

    **Gradle:**
    ```groovy
    implementation 'com.github.ChafficPlugins:CrucialLib:TAG'
    ```

2. **Update `plugin.yml`** — change the dependency name to `CrucialLib`:

    ```yaml
    depend: [CrucialLib]
    ```

3. **Update package imports** — replace `io.github.chafficui.CrucialAPI` with `io.github.chafficui.CrucialLib` in your code.

## Version history

<!-- TODO: Add entries here as new versions are released -->

| Version | Changes |
| ------- | ------- |
| 2.2.0   | Current version, forked as CrucialLib |
