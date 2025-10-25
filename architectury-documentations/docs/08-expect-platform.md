# @ExpectPlatform Annotation

The `@ExpectPlatform` annotation is part of Architectury Injectables and allows you to write platform-specific code cleanly without reflection or service loaders.

## What is @ExpectPlatform?

`@ExpectPlatform` marks methods that should have platform-specific implementations. At compile time, Architectury looks for matching implementations in platform-specific modules (forge/fabric) and wires them together.

## Basic Usage

### Common Module (Platform-Agnostic)

```java
package com.example.blockpops;

import dev.architectury.injectables.annotations.ExpectPlatform;
import java.nio.file.Path;

public class PlatformHelper {
    /**
     * Gets the config directory for the current platform.
     */
    @ExpectPlatform
    public static Path getConfigDirectory() {
        // This code is never executed
        // Architectury replaces this at compile time
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isModLoaded(String modId) {
        throw new AssertionError();
    }
}
```

### Forge Module (Platform-Specific)

```java
package com.example.blockpops.forge;

import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.ModList;
import java.nio.file.Path;

public class PlatformHelperImpl {
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
```

### Fabric Module (Platform-Specific)

```java
package com.example.blockpops.fabric;

import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.Path;

public class PlatformHelperImpl {
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
```

## Requirements

### For the Common Module Method

1. **Must be static**: Only static methods can use `@ExpectPlatform`
2. **Must throw AssertionError**: The body should only throw `AssertionError`
3. **Package structure**: Can be in any package

### For Platform Implementation Methods

1. **Package naming**: Must be in the common package + `.forge` or `.fabric`
2. **Class naming**: Class name must be the common class name + `Impl`
3. **Method signature**: Must match exactly (name, parameters, return type, modifiers)
4. **Must be static**: Implementation methods must also be static

## Package Naming Conventions

The implementation class must follow this pattern:

```
Common:      com.example.blockpops.PlatformHelper
Forge Impl:  com.example.blockpops.forge.PlatformHelperImpl
Fabric Impl: com.example.blockpops.fabric.PlatformHelperImpl
```

## Common Use Cases

### Registry Object Creation

```java
// Common
public class RegistryHelper {
    @ExpectPlatform
    public static <T> Supplier<T> registerBlock(String name, Supplier<T> block) {
        throw new AssertionError();
    }
}

// Forge
public class RegistryHelperImpl {
    public static <T> Supplier<T> registerBlock(String name, Supplier<T> block) {
        // Forge-specific registry logic
        return BLOCKS.register(name, block);
    }
}

// Fabric
public class RegistryHelperImpl {
    public static <T> Supplier<T> registerBlock(String name, Supplier<T> block) {
        // Fabric-specific registry logic
        T registered = Registry.register(Registry.BLOCK, new Identifier(MOD_ID, name), block.get());
        return () -> registered;
    }
}
```

### Platform-Specific Config Paths

```java
// Common
public class ConfigHelper {
    @ExpectPlatform
    public static Path getModConfigPath(String modId) {
        throw new AssertionError();
    }

    public static void loadConfig() {
        Path configPath = getModConfigPath("blockpops");
        // Common config loading logic
    }
}
```

### Checking Mod Loader Features

```java
// Common
public class LoaderQueries {
    @ExpectPlatform
    public static boolean isDevelopmentEnvironment() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static String getModVersion(String modId) {
        throw new AssertionError();
    }
}
```

### Creating Platform-Specific Objects

```java
// Common
public class EntityHelper {
    @ExpectPlatform
    public static <T extends Entity> EntityType<T> createEntityType(
        EntityType.EntityFactory<T> factory,
        MobCategory category
    ) {
        throw new AssertionError();
    }
}
```

## Advanced Example: Cross-Platform Networking

```java
// Common Module
public class NetworkHelper {
    @ExpectPlatform
    public static void registerServerReceiver(ResourceLocation id, ServerPacketHandler handler) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void sendToServer(ResourceLocation id, FriendlyByteBuf buf) {
        throw new AssertionError();
    }

    @FunctionalInterface
    public interface ServerPacketHandler {
        void handle(ServerPlayer player, FriendlyByteBuf buf);
    }
}

// Forge Implementation
public class NetworkHelperImpl {
    private static final Map<ResourceLocation, ServerPacketHandler> HANDLERS = new HashMap<>();

    public static void registerServerReceiver(ResourceLocation id, ServerPacketHandler handler) {
        HANDLERS.put(id, handler);
        // Forge networking registration
        // ... forge-specific code
    }

    public static void sendToServer(ResourceLocation id, FriendlyByteBuf buf) {
        // Forge packet sending
        // ... forge-specific code
    }
}

// Fabric Implementation
public class NetworkHelperImpl {
    public static void registerServerReceiver(ResourceLocation id, ServerPacketHandler handler) {
        ServerPlayNetworking.registerGlobalReceiver(id, (server, player, handler, buf, responseSender) -> {
            handler.handle(player, buf);
        });
    }

    public static void sendToServer(ResourceLocation id, FriendlyByteBuf buf) {
        ClientPlayNetworking.send(id, buf);
    }
}
```

## Debugging @ExpectPlatform

### Compilation Errors

If you see errors like "Method implementation not found":

1. **Check package naming**: Ensure impl is in `original.package.forge` or `original.package.fabric`
2. **Check class naming**: Implementation class must be `OriginalClassImpl`
3. **Check method signature**: Must match exactly
4. **Check it's static**: Both common and impl methods must be static

### Runtime Errors

If you get `AssertionError` at runtime:

- The platform implementation wasn't found at compile time
- Likely a packaging or naming issue

### IDE Support

Most IDEs won't understand the `@ExpectPlatform` magic. You may see:

- "Method never throws AssertionError" warnings (ignore these)
- "Implementation not found" errors (ignore if builds work)

## When to Use @ExpectPlatform

### Use When:

- You need truly platform-specific implementations
- The logic differs significantly between platforms
- You want type-safe platform code

### Don't Use When:

- Architectury API already provides what you need
- You can use `Platform.isForge()` / `Platform.isFabric()` with simple if statements
- The code is mostly the same with minor differences

## Alternatives

### Simple Platform Checks

```java
// Instead of @ExpectPlatform for simple cases:
if (Platform.isForge()) {
    // Forge code
} else {
    // Fabric code
}
```

### Architectury APIs

```java
// Instead of custom @ExpectPlatform:
Path config = Platform.getConfigFolder();
boolean loaded = Platform.isModLoaded("geckolib");
```

### Service Loaders

For more complex cases, consider using Java's ServiceLoader pattern.

## Best Practices

1. **Keep implementations simple**: @ExpectPlatform methods should be thin wrappers
2. **Document well**: Add Javadocs explaining what each platform does differently
3. **Consistent returns**: Ensure both platforms return equivalent data
4. **Avoid overuse**: Use Architectury APIs when available
5. **Test both platforms**: Always test on both Forge and Fabric

## Common Patterns

### Lazy Initialization

```java
// Common
public class LazyHelper {
    private static Supplier<ResourceLocation> lazyResource;

    static {
        lazyResource = createLazyResource();
    }

    @ExpectPlatform
    private static Supplier<ResourceLocation> createLazyResource() {
        throw new AssertionError();
    }
}
```

### Factory Methods

```java
// Common
public class ItemHelper {
    @ExpectPlatform
    public static Item.Properties createItemProperties() {
        throw new AssertionError();
    }

    public static Item createCustomItem() {
        return new CustomItem(createItemProperties());
    }
}
```

## See Also

- [Platform Abstraction](02-platform-abstraction.md) - Built-in platform utilities
- [Getting Started](01-getting-started.md) - Setting up Architectury
- Architectury Injectables: https://github.com/architectury/architectury-plugin

---

*Documentation generated from Architectury API 9.2.14 source code*
