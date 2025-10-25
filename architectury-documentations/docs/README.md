# Architectury API 9.2.14 Documentation for Minecraft 1.20.1

This directory contains comprehensive offline documentation for Architectury API version 9.2.14, which is used in the BlockPops project.

## What is Architectury API?

Architectury API is an intermediary API that provides abstractions for developing cross-platform Minecraft mods that work on both Fabric and Forge. It offers:

- **90+ Event Hooks** - Comprehensive event system
- **Unified Networking** - Cross-platform packet handling
- **Registry Abstraction** - Platform-agnostic registration
- **Platform Utilities** - Easy platform detection and information
- **@ExpectPlatform** - Clean platform-specific implementations

## Documentation Index

### Getting Started

1. **[Getting Started](01-getting-started.md)** - Introduction, setup, and when to use Architectury API
2. **[Platform Abstraction](02-platform-abstraction.md)** - Platform detection, mod queries, environment checks

### Core Systems

3. **[Registry System](03-registry-system.md)** - Registering blocks, items, entities, and other game content
4. **[Networking](04-networking.md)** - Sending custom packets between client and server

### Events System

5. **[Events Overview](05-events-overview.md)** - Event system basics, results, and best practices
6. **[Common Events Reference](06-common-events.md)** - Complete reference for server/common events
   - Player Events (join, quit, respawn, advancement, crafting, etc.)
   - Entity Events (spawn, death, damage, etc.)
   - Block Events (break, place)
   - Interaction Events (right-click, left-click, entity interaction)
   - Tick Events (server, player, world)
   - Lifecycle Events (server starting/stopping, world load/unload)
   - Command Events
   - Chat Events
   - Chunk Events
   - Explosion Events
   - Lightning Events
   - Loot Events

7. **[Client Events Reference](07-client-events.md)** - Complete reference for client-only events
   - Client Tick Events
   - Client Lifecycle Events
   - Client Player Events
   - Input Events (keyboard, mouse, scroll)
   - GUI/Screen Events
   - Tooltip Events
   - Chat Events (client-side)
   - Texture Events
   - Recipe Events
   - Shader Events
   - Client Command Registration

### Advanced Topics

8. **[ExpectPlatform Annotation](08-expect-platform.md)** - Writing platform-specific code cleanly

## Quick Reference

### Your Project Configuration

- **Minecraft Version**: 1.20.1
- **Forge Version**: 1.20.1-47.4.9
- **Architectury API Version**: 9.2.14
- **Mod ID**: blockpops
- **Enabled Platforms**: Forge

### Common Imports

```java
// Platform
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;

// Registry
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;

// Events
import dev.architectury.event.events.common.*;
import dev.architectury.event.events.client.*;
import dev.architectury.event.EventResult;

// Networking
import dev.architectury.networking.NetworkManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;
```

### Quick Start Examples

#### Register an Item

```java
public class ModItems {
    private static final String MOD_ID = "blockpops";

    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> EXAMPLE_ITEM =
        ITEMS.register("example_item", () -> new Item(new Item.Properties()));

    public static void init() {
        ITEMS.register();
    }
}
```

#### Register an Event

```java
public class ModEvents {
    public static void init() {
        PlayerEvent.PLAYER_JOIN.register(player -> {
            player.sendSystemMessage(Component.literal("Welcome!"));
        });
    }
}
```

#### Send a Network Packet

```java
// Register receiver (server-side)
NetworkManager.registerReceiver(
    NetworkManager.c2s(),
    new ResourceLocation("blockpops", "example"),
    (buf, context) -> {
        String message = buf.readUtf();
        context.queue(() -> {
            // Handle on main thread
        });
    }
);

// Send from client
@Environment(EnvType.CLIENT)
public class ClientHandler {
    public static void sendMessage(String msg) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUtf(msg);
        NetworkManager.sendToServer(new ResourceLocation("blockpops", "example"), buf);
    }
}
```

#### Check Platform

```java
if (Platform.isForge()) {
    // Forge-specific code
}

if (Platform.isModLoaded("geckolib")) {
    // GeckoLib integration
}

Path configDir = Platform.getConfigFolder();
```

## Event Categories Quick Reference

### Common Events (Both Sides)

| Category | Key Events |
|----------|-----------|
| **Player** | JOIN, QUIT, RESPAWN, ADVANCEMENT, CRAFT_ITEM, PICKUP_ITEM, DROP_ITEM |
| **Entity** | ADD, LIVING_DEATH, LIVING_HURT, LIVING_FALL |
| **Block** | BREAK, PLACE |
| **Interaction** | RIGHT_CLICK_BLOCK, LEFT_CLICK_BLOCK, INTERACT_ENTITY |
| **Tick** | SERVER_POST, PLAYER_POST, SERVER_LEVEL_POST |
| **Lifecycle** | SERVER_STARTING, SERVER_STARTED, SERVER_STOPPING |
| **Commands** | REGISTRATION |
| **Chat** | RECEIVED |
| **World** | CHUNK_LOAD, CHUNK_UNLOAD, EXPLOSION_DETONATE |

### Client Events (Client Only)

| Category | Key Events |
|----------|-----------|
| **Tick** | CLIENT_POST, CLIENT_PLAYER_POST, CLIENT_LEVEL_POST |
| **Lifecycle** | CLIENT_SETUP, CLIENT_STARTED, CLIENT_STOPPING |
| **Player** | CLIENT_PLAYER_JOIN, CLIENT_PLAYER_QUIT |
| **Input** | KEY_PRESSED, MOUSE_CLICKED, MOUSE_SCROLLED |
| **GUI** | RENDER_HUD, SET_SCREEN, INIT_POST |
| **Tooltip** | ITEM, RENDER_PRE |
| **Chat** | RECEIVED, SEND |

## Feature Comparison

### Architectury vs Platform-Specific

| Feature | Forge (Native) | Fabric (Native) | Architectury (Both) |
|---------|---------------|----------------|---------------------|
| Event System | Event Bus | Fabric API Events | Unified Event Interface |
| Networking | SimpleChannel | ServerPlayNetworking | NetworkManager |
| Registry | DeferredRegister | Registry.register | DeferredRegister/Registrar |
| Platform Detection | FMLLoader | FabricLoader | Platform class |
| Development | Forge-only | Fabric-only | Cross-platform |

## Best Practices

1. **Always use `@Environment(EnvType.CLIENT)`** for client-only code
2. **Register events during mod initialization**, not lazily
3. **Use `context.queue()`** in network handlers for thread safety
4. **Return `EventResult.pass()`** if not interrupting an event
5. **Keep tick event handlers lightweight** to avoid lag
6. **Use Architectury APIs** instead of platform-specific code when possible

## Common Patterns

### Mod Initialization Structure

```java
// Common Module - Main class
public class BlockPops {
    public static final String MOD_ID = "blockpops";

    public static void init() {
        ModRegistry.init();
        ModEvents.init();
        ModNetworking.init();
    }
}

// Forge Module - Forge entry point
@Mod("blockpops")
public class BlockPopsForge {
    public BlockPopsForge() {
        EventBuses.registerModEventBus(BlockPops.MOD_ID,
            FMLJavaModLoadingContext.get().getModEventBus());
        BlockPops.init();
    }
}
```

### Client Initialization

```java
@Environment(EnvType.CLIENT)
public class BlockPopsClient {
    public static void init() {
        ModClientEvents.init();
        ModClientNetworking.init();
        ModRenderers.init();
    }
}

// Call from Forge client setup
@Environment(EnvType.CLIENT)
public class BlockPopsForgeClient {
    public static void init() {
        BlockPopsClient.init();
    }
}
```

## Troubleshooting

### Common Issues

**Problem**: Events not firing
- **Solution**: Check you're registering in the correct initialization phase
- **Solution**: Verify you're using the correct event side (client vs common)

**Problem**: Networking crashes
- **Solution**: Always use `context.queue()` for game state access
- **Solution**: Ensure read/write operations match on both sides

**Problem**: Registry crashes
- **Solution**: Call `DeferredRegister.register()` during initialization
- **Solution**: Don't access registry objects before they're registered

**Problem**: Class not found on dedicated server
- **Solution**: Use `@Environment(EnvType.CLIENT)` for client-only classes
- **Solution**: Separate client and common code properly

## External Resources

- **Official Documentation**: https://docs.architectury.dev/
- **GitHub Repository**: https://github.com/architectury/architectury-api
- **Discord Community**: https://discord.gg/C2RdJDpRBP
- **Example Mod**: https://github.com/architectury/architectury-example-mod
- **Templates**: https://github.com/architectury/architectury-templates

## Documentation Structure

This documentation was generated by analyzing the Architectury API 9.2.14 source code. Each page includes:

- **Overview** of the system/feature
- **Code examples** with explanations
- **Common use cases** and patterns
- **Best practices** and pitfalls to avoid
- **Complete working examples**
- **Related documentation** links

## Integration with Your Project

The BlockPops project uses:

- **Architectury API** for cross-platform compatibility
- **GeckoLib** for entity/block animations (see `/docs/geckolib-4.7.4/`)
- **Forge** as the target platform

When writing code, refer to:
- This documentation for Architectury API usage
- GeckoLib docs for animation systems
- Minecraft Wiki for vanilla game mechanics

## Contributing to Documentation

Found an error or want to add examples? The documentation source files are in `/architectury-documentations/docs/`. Each file is written in Markdown.

## License

Architectury API is licensed under the GNU Lesser General Public License v3.0.

This documentation is based on the source code of Architectury API version 9.2.14 and is provided for educational purposes.

---

**Documentation Generated**: 2025-10-25
**For**: BlockPops Project
**Minecraft Version**: 1.20.1
**Architectury API Version**: 9.2.14

*Happy modding with Architectury!*
