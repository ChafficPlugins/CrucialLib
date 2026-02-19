# Runbook

## Building a Release

1. **Update version** in `pom.xml`:
   ```xml
   <version>X.Y.Z</version>
   ```

2. **Verify the build passes**:
   ```bash
   mvn clean verify
   ```

3. **Commit the version bump**:
   ```bash
   git add pom.xml
   git commit -m "release: bump version to X.Y.Z"
   git push origin master
   ```

4. **Create a Git tag**:
   ```bash
   git tag vX.Y.Z
   git push origin vX.Y.Z
   ```

5. **Create a GitHub Release**:
   - Go to https://github.com/ChafficPlugins/CrucialLib/releases/new
   - Select the tag `vX.Y.Z`
   - Title: `vX.Y.Z`
   - Attach the built JAR from `target/CrucialLib-vX.Y.Z.jar`
   - Publish the release

## Publishing to JitPack

JitPack automatically builds from GitHub tags/releases. Once a GitHub release is created:

1. Visit `https://jitpack.io/#ChafficPlugins/CrucialLib/vX.Y.Z`
2. JitPack will build the artifact on first request
3. Dependent projects can use:
   ```xml
   <dependency>
     <groupId>com.github.ChafficPlugins</groupId>
     <artifactId>CrucialLib</artifactId>
     <version>X.Y.Z</version>
   </dependency>
   ```

## Common Build Errors

### `cannot find symbol: variable GENERIC_MAX_HEALTH`
Spigot 1.21+ renamed `Attribute.GENERIC_MAX_HEALTH` to `Attribute.MAX_HEALTH`. Update all references.

### `cannot find symbol: variable DURABILITY`
Spigot 1.21+ renamed `Enchantment.DURABILITY` to `Enchantment.UNBREAKING`. Update the reference in `Stack.java`.

### `cannot find symbol: variable HIDE_POTION_EFFECTS`
Spigot 1.20.5+ renamed `ItemFlag.HIDE_POTION_EFFECTS` to `ItemFlag.HIDE_ADDITIONAL_TOOLTIP`. Update the reference in `Stack.java`.

### Tests fail with `MockBukkit.mock()` errors
Ensure MockBukkit version matches the Spigot API version. For Spigot 1.21.x, use `mockbukkit-v1.21`.

### `Plugin org.apache.maven.plugins:... could not be resolved`
Network/proxy issue. Clear failed downloads and retry:
```bash
find ~/.m2/repository -name "*.lastUpdated" -delete
mvn clean verify
```

## Reverting a Bad Release

1. **Delete the GitHub Release** (but keep the tag for history)
2. **Create a fix branch**:
   ```bash
   git checkout -b fix/revert-bad-release
   ```
3. Fix the issue and bump to a new PATCH version
4. Follow the standard release process with the new version

If the bad release has already been cached by JitPack:
- JitPack caches builds permanently for release tags
- Create a new tag with the next patch version instead
- Notify dependent plugin maintainers to update their dependency version

## Emergency Procedures

### Plugin crashes servers on load
1. Remove `CrucialLib-vX.Y.Z.jar` from the server's `plugins/` directory
2. Replace with the last known good version from [Releases](https://github.com/ChafficPlugins/CrucialLib/releases)
3. Restart the server

### Breaking API change shipped
1. Immediately release a new PATCH version reverting the breaking change
2. If the break was intentional, bump the MAJOR version instead and document the migration

### bStats data not reporting
1. Check `plugins/bStats/config.yml` on the server — ensure `enabled: true`
2. Verify the server has outbound HTTPS access to `bStats.org`
3. Check server console for bStats-related error messages
