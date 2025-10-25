# Platform Abstraction

The `Platform` class provides utilities to detect which mod loader is running and access platform-specific information in a unified way.

## Platform Detection

### Checking the Current Platform

```java
import dev.architectury.platform.Platform;

// Check if running on Fabric
if (Platform.isFabric()) {
    // Fabric-specific logic
}

// Check if running on Forge
if (Platform.isForge()) {
    // Forge-specific logic
}
```

### Minecraft Version

```java
String minecraftVersion = Platform.getMinecraftVersion();
// Returns: "1.20.1"
```

## Environment Detection

### Getting the Current Environment

```java
import dev.architectury.utils.Env;
import net.fabricmc.api.EnvType;

// Platform-agnostic wrapper
Env environment = Platform.getEnvironment();

if (environment == Env.CLIENT) {
    // Client-side code
} else if (environment == Env.SERVER) {
    // Server-side code
}

// Or get the raw EnvType (Dist on Forge)
EnvType envType = Platform.getEnv();
```

### Development Environment Check

```java
if (Platform.isDevelopmentEnvironment()) {
    // This is a development environment
    // Useful for debug logging or dev-only features
}
```

## Filesystem Paths

All returned paths are **absolute** and **normalized**.

### Game Folder

```java
import java.nio.file.Path;

// Gets the root Minecraft directory
Path gameFolder = Platform.getGameFolder();
// Example: /home/user/.minecraft
```

### Config Folder

```java
// Gets the main config folder
Path configFolder = Platform.getConfigFolder();
// Example: /home/user/.minecraft/config
```

### Mods Folder

```java
// Gets the mods folder
Path modsFolder = Platform.getModsFolder();
// Example: /home/user/.minecraft/mods
```

## Mod Management

### Check if a Mod is Loaded

```java
if (Platform.isModLoaded("geckolib")) {
    // GeckoLib is present
}

if (Platform.isModLoaded("jei")) {
    // JEI is present, add integration
}
```

### Get Mod Information

```java
import dev.architectury.platform.Mod;
import java.util.Optional;

// Get a specific mod (throws NoSuchElementException if not found)
try {
    Mod geckoLib = Platform.getMod("geckolib");
    String version = geckoLib.getVersion();
    String name = geckoLib.getName();
} catch (NoSuchElementException e) {
    // Mod not found
}

// Safe optional variant
Optional<Mod> optionalMod = Platform.getOptionalMod("geckolib");
optionalMod.ifPresent(mod -> {
    System.out.println("GeckoLib version: " + mod.getVersion());
});
```

### List All Loaded Mods

```java
import java.util.Collection;

// Get all mod containers
Collection<Mod> allMods = Platform.getMods();

// Get all mod IDs
Collection<String> modIds = Platform.getModIds();

// Example: Print all loaded mods
for (Mod mod : allMods) {
    System.out.println(mod.getModId() + " - " + mod.getName() + " v" + mod.getVersion());
}
```

## Mod Container API

The `Mod` interface provides information about a loaded mod:

```java
public interface Mod {
    String getModId();           // e.g., "blockpops"
    String getVersion();         // e.g., "1.0.0"
    String getName();            // e.g., "BlockPops"
    String getDescription();     // Mod description
    Optional<String> getLogoFile(int preferredSize);  // Logo path
    Path getFilePath();          // Path to mod file
    // ... and more
}
```

## Practical Examples

### Conditional Integration with Another Mod

```java
public class ModIntegrations {
    public static void init() {
        if (Platform.isModLoaded("jei")) {
            initJEIIntegration();
        }

        if (Platform.isModLoaded("rei")) {
            initREIIntegration();
        }
    }

    private static void initJEIIntegration() {
        // JEI-specific integration code
    }

    private static void initREIIntegration() {
        // REI-specific integration code
    }
}
```

### Platform-Specific Configuration

```java
public class ModConfig {
    public static Path getConfigFile() {
        Path configDir = Platform.getConfigFolder();
        return configDir.resolve("blockpops.json");
    }

    public static void load() {
        Path configFile = getConfigFile();
        if (Files.exists(configFile)) {
            // Load configuration
        } else {
            // Create default configuration
        }
    }
}
```

### Debug Logging in Development

```java
public class ModLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger("BlockPops");

    public static void debugLog(String message) {
        if (Platform.isDevelopmentEnvironment()) {
            LOGGER.info("[DEBUG] " + message);
        }
    }

    public static void init() {
        if (Platform.isDevelopmentEnvironment()) {
            LOGGER.info("Running in development mode");
            LOGGER.info("Minecraft: " + Platform.getMinecraftVersion());
            LOGGER.info("Platform: " + (Platform.isForge() ? "Forge" : "Fabric"));
        }
    }
}
```

## Best Practices

1. **Cache Platform Checks**: If you need to check the platform multiple times, cache the result in a static field
2. **Use Environment Checks**: Always check the environment (client/server) before running side-specific code
3. **Soft Dependencies**: Use `isModLoaded()` for optional integrations with other mods
4. **Path Handling**: Always use the provided path methods instead of hardcoding filesystem paths

## See Also

- [Getting Started](01-getting-started.md)
- [@ExpectPlatform](08-expect-platform.md) - For implementing platform-specific code
- [Utilities](09-utilities.md) - Additional utility classes

---

*Documentation generated from Architectury API 9.2.14 source code*
