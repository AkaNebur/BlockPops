# GeckoLib4 Installation Documentation

## Overview
GeckoLib is a lightweight animation library for Minecraft mods. The installation process requires adding repository configurations and dependencies to your `build.gradle` file.

## Repository Configuration

All versions require adding the GeckoLib Maven repository:

```gradle
repositories {
    maven {
        name = 'GeckoLib'
        url 'https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/'
        content {
            includeGroup("software.bernie.geckolib")
        }
    }
}
```

## Platform-Specific Dependencies

### Fabric (1.20.5+)
```gradle
dependencies {
    modImplementation "software.bernie.geckolib:geckolib-fabric-${minecraft_version}:${geckolib_version}"
}
```

### Forge (1.20.5+)
```gradle
dependencies {
    implementation fg.deobf("software.bernie.geckolib:geckolib-forge-${minecraft_version}:${geckolib_version}")
}
```

Forge also requires the Sponge Mixin plugin in `settings.gradle`:
```gradle
pluginManagement {
    repositories {
        maven { url = 'https://repo.spongepowered.org/repository/maven-public/' }
    }
}
```

And in `build.gradle`:
```gradle
plugins {
    id 'org.spongepowered.mixin' version '0.7.+'
}
```

### NeoForge (1.20.5+)
```gradle
dependencies {
    implementation "software.bernie.geckolib:geckolib-neoforge-${minecraft_version}:${geckolib_version}"
}
```

## Version Property

Add to `gradle.properties`:
```properties
geckolib_version=???
```
Replace with the latest version from Curseforge for your Minecraft version.

## Older Versions (1.19.3-1.20.4)

For earlier versions, include additional MCLib dependency:
```gradle
dependencies {
    modImplementation("software.bernie.geckolib:geckolib-fabric-${minecraft_version}:${geckolib_version}")
    implementation("com.eliotlash.mclib:mclib:20")
}
```

## Multiloader Support

**1.20.5+:** GeckoLib offers full multiloader compatibility with 100% functionality in common sources.

**1.20.4 and below:** Use platform-specific sources in common configurations; platform-specific handling may be needed for items and armor.

## Additional Setup

After configuring Gradle dependencies, refresh your Gradle project and regenerate run configurations. For model creation, install the Blockbench plugin following the project's documentation.
