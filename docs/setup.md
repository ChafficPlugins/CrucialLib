# Setup

## Server admins

1. Download the latest CrucialLib jar from [Releases](https://github.com/ChafficPlugins/CrucialLib/releases/latest)
2. Place the jar in your server's `plugins/` folder
3. Restart the server

> **Note:** CrucialLib requires a Spigot/Paper 1.21+ server running Java 21.

## Developers

CrucialLib is available via [JitPack](https://jitpack.io/#ChafficPlugins/CrucialLib).

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

Then add the dependency:

```xml
<dependency>
    <groupId>com.github.ChafficPlugins</groupId>
    <artifactId>CrucialLib</artifactId>
    <version>v3.0.0</version>
</dependency>
```

### Gradle

Add the JitPack repository:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

Then add the dependency:

```groovy
dependencies {
    implementation 'com.github.ChafficPlugins:CrucialLib:v3.0.0'
}
```

### plugin.yml

Add CrucialLib as a dependency in your `plugin.yml`:

```yaml
depend: [CrucialLib]
```
