# CrucialLib
[![](https://jitpack.io/v/ChafficPlugins/CrucialLib.svg)](https://jitpack.io/#ChafficPlugins/CrucialLib)

# CrucialLib

CrucialLib is an open-source, easy-to-use Spigot library that aims to simplify plugin development.

> Note: If you have any suggestions, questions, or feedback for this wiki, please use [Discussions](https://github.com/ChafficPlugins/CrucialLib/discussions).

## Setup

> **Attention!** Do not forget to add CrucialLib as a dependency in your "plugin.yml" file!

### Maven

```XML
<dependency>
  <groupId>com.github.ChafficPlugins</groupId>
  <artifactId>CrucialLib</artifactId>
  <version>2.2.0</version>
</dependency>
```

### Gradle

```Gradle
implementation 'com.github.ChafficPlugins:CrucialLib:2.2.0'
```

## Examples

```Java
public class Main extends JavaPlugin {
    private final String CrucialLibVersion = "2.2.0";

    /** Auto-download CrucialLib */
    @Override
    public void onLoad(){
        if(getServer().getPluginManager().getPlugin("CrucialLib") == null){
            try {
                URL website = new URL("https://github.com/ChafficPlugins/CrucialLib/releases/download/v" + CrucialLibVersion + "/CrucialLib-v" + CrucialLibVersion + ".jar");
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream("plugins/CrucialLib.jar");
                fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
                Bukkit.getPluginManager().loadPlugin(new File("plugins/CrucialLib.jar"));
            } catch (IOException | InvalidDescriptionException | org.bukkit.plugin.InvalidPluginException e) {
                e.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
    }

    /** Auto-update CrucialLib */
    @Override
    public void onEnable(){
        if(Server.checkVersion(new String[]{"1.16", "1.15"})){
            Crucial.getVersion(CrucialLibVersion, this);
        } else {
            //CrucialLib only supports 1.16 and 1.15
            Bukkit.getPluginManager().disablePlugin(this);
        }
        new CrucialItem("Super shovel", Material.DIAMOND_SHOVEL, "item").setCrafting(new String[]{"Air", "AIR", "AIR", "DIAMOND", "DIAMOND", "DIAMOND", "AIR", "AIR", "AIR"});
    }
}
```

## Download

You can get the latest stable release build here: [Stable Build](https://github.com/ChafficPlugins/CrucialLib/releases/latest)
If you want the most up-to-date builds, you can find them here: [Releases](https://github.com/ChafficPlugins/CrucialLib/releases)

## Documentation

[Documentation](https://www.javadoc.io/doc/io.github.chafficui/CrucialLib/latest/)
Currently, some parts of CrucialLib lack proper documentation. However, we are working hard to improve it!

## Getting Help

If you need help or want to chat with the CAPI team or other developers, you can join the [Official ChafficPlugins Discord Server](https://discord.gg/ARxYx4Z).
Alternatively, you can use the [Discussions](https://github.com/ChafficPlugins/CrucialLib/discussions) section.

## Dependencies

This project requires Spigot 1.15 or higher.

Here are some additional information about LTS versions:

| Version | Supported          |
| ------- | ------------------ |
| 2.2.x   | :white_check_mark: |
| 2.1.x   | :x:                |
| 2.0.x   | :x:                |
| 1.x.x   | :x:                |
