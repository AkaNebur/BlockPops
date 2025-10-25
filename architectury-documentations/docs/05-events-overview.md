# Events Overview

Architectury API provides over 90 event hooks that work across both Fabric and Forge. Events allow you to hook into various game mechanics without modifying vanilla code.

## Event System Basics

### Registering Event Listeners

Events use a simple registration pattern:

```java
import dev.architectury.event.events.common.TickEvent;

public class ModEvents {
    public static void init() {
        // Register a listener for server tick events
        TickEvent.SERVER_POST.register(server -> {
            // Your code here runs every server tick
        });
    }
}
```

### Event Interface

All events implement the `Event<T>` interface:

```java
public interface Event<T> {
    T invoker();                    // Get the event invoker
    void register(T listener);      // Register a listener
    void unregister(T listener);    // Remove a listener
    boolean isRegistered(T listener); // Check if registered
    void clearListeners();          // Remove all listeners
}
```

## Event Results

Some events allow you to control execution flow using result types:

### EventResult

Simple three-state result:

```java
public enum EventResult {
    interrupt(true),    // Stop further processing, cancel the action
    interruptDefault(), // Stop further processing, use default behavior
    interruptTrue(),    // Stop further processing, return true
    interruptFalse(),   // Stop further processing, return false
    pass();            // Continue to next listener
}
```

Example:

```java
InteractionEvent.RIGHT_CLICK_BLOCK.register((player, hand, pos, face) -> {
    if (someCondition) {
        return EventResult.interruptTrue(); // Cancel and return true
    }
    return EventResult.pass(); // Let other listeners handle it
});
```

### CompoundEventResult

For events that need to return both a result and a value:

```java
CompoundEventResult<ItemStack> result = CompoundEventResult.interruptTrue(itemStack);
// or
CompoundEventResult<ItemStack> result = CompoundEventResult.pass();
```

## Event Categories

Architectury organizes events into two main categories:

### Common Events (Both Client and Server)

Located in `dev.architectury.event.events.common.*`:

- **[Block Events](06-common-events.md#block-events)** - Block breaking, placing, etc.
- **[Entity Events](06-common-events.md#entity-events)** - Entity spawning, loading, damage, etc.
- **[Player Events](06-common-events.md#player-events)** - Player joining, respawning, advancement, etc.
- **[Interaction Events](06-common-events.md#interaction-events)** - Right-click, left-click, entity interaction
- **[Tick Events](06-common-events.md#tick-events)** - Server and world ticking
- **[Lifecycle Events](06-common-events.md#lifecycle-events)** - Server starting, stopping, data pack sync
- **[Command Events](06-common-events.md#command-events)** - Command registration
- **[Chat Events](06-common-events.md#chat-events)** - Chat message processing
- **[Chunk Events](06-common-events.md#chunk-events)** - Chunk loading/unloading
- **[Explosion Events](06-common-events.md#explosion-events)** - Explosion handling
- **[Lightning Events](06-common-events.md#lightning-events)** - Lightning strikes
- **[Loot Events](06-common-events.md#loot-events)** - Loot table modification

### Client-Only Events

Located in `dev.architectury.event.events.client.*`:

- **[Client Tick Events](07-client-events.md#tick-events)** - Client-side ticking
- **[Client Lifecycle Events](07-client-events.md#lifecycle-events)** - Client starting/stopping
- **[Client Player Events](07-client-events.md#player-events)** - Client player joining/leaving
- **[Input Events](07-client-events.md#input-events)** - Keyboard and mouse input
- **[Screen Events](07-client-events.md#screen-events)** - GUI interactions
- **[Rendering Events](07-client-events.md#rendering-events)** - GUI rendering, tooltips
- **[Client Chat Events](07-client-events.md#chat-events)** - Client chat sending/receiving
- **[Texture Events](07-client-events.md#texture-events)** - Texture atlas stitching
- **[Recipe Events](07-client-events.md#recipe-events)** - Recipe updates
- **[Shader Events](07-client-events.md#shader-events)** - Shader reloading

## Basic Examples

### Server Tick Event

```java
import dev.architectury.event.events.common.TickEvent;

public class TickHandler {
    private static int tickCounter = 0;

    public static void init() {
        TickEvent.SERVER_POST.register(server -> {
            tickCounter++;

            if (tickCounter % 20 == 0) { // Every second
                // Do something every second
            }
        });
    }
}
```

### Block Break Event

```java
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.EventResult;

public class BlockHandler {
    public static void init() {
        BlockEvent.BREAK.register((level, pos, state, player, xp) -> {
            if (state.getBlock() == Blocks.DIAMOND_ORE) {
                // Custom diamond ore logic
                return EventResult.interruptFalse(); // Cancel breaking
            }
            return EventResult.pass(); // Allow breaking
        });
    }
}
```

### Player Join Event

```java
import dev.architectury.event.events.common.PlayerEvent;

public class PlayerHandler {
    public static void init() {
        PlayerEvent.PLAYER_JOIN.register(player -> {
            player.sendSystemMessage(
                Component.literal("Welcome to the server!")
            );
        });
    }
}
```

### Client Input Event

```java
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.event.EventResult;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class InputHandler {
    public static void init() {
        ClientRawInputEvent.KEY_PRESSED.register((client, keyCode, scanCode, action, modifiers) -> {
            if (keyCode == GLFW.GLFW_KEY_F && action == GLFW.GLFW_PRESS) {
                // Custom key F handling
                return EventResult.interruptTrue();
            }
            return EventResult.pass();
        });
    }
}
```

## Event Lifecycle

### When are Events Called?

Events are called at specific points during game execution. The exact timing depends on the event:

- **PRE events** - Called before an action
- **POST events** - Called after an action
- **No suffix** - Called during or around an action

### Event Priority

Listeners are called in the order they are registered. If you need specific ordering:

1. Register important listeners first
2. Use `EventResult.interrupt*()` to stop processing
3. Use `EventResult.pass()` to allow other listeners to run

### Unregistering Listeners

```java
// Store the listener if you need to unregister it later
var listener = (Consumer<ServerLevel>) level -> {
    // Do something
};

TickEvent.SERVER_POST.register(listener);

// Later, unregister
TickEvent.SERVER_POST.unregister(listener);
```

## Best Practices

1. **Environment Annotations**: Use `@Environment(EnvType.CLIENT)` for client-only events

2. **Performance**: Keep event handlers lightweight. Heavy processing can cause lag.

3. **Event Results**: Always return `EventResult.pass()` if you don't want to interrupt the event

4. **Null Checks**: Some event parameters may be null. Always check before using.

5. **Thread Safety**: Most events run on the main thread, but some may not. Check documentation.

6. **Static Registration**: Register events during mod initialization, not lazily

## Event Registration Pattern

Recommended pattern for organizing events:

```java
public class ModEvents {
    public static void init() {
        registerCommonEvents();
    }

    private static void registerCommonEvents() {
        // Server tick
        TickEvent.SERVER_POST.register(server -> {
            // Handle server tick
        });

        // Player events
        PlayerEvent.PLAYER_JOIN.register(player -> {
            // Handle player join
        });

        // Block events
        BlockEvent.BREAK.register((level, pos, state, player, xp) -> {
            // Handle block break
            return EventResult.pass();
        });
    }
}

@Environment(EnvType.CLIENT)
public class ModClientEvents {
    public static void init() {
        registerClientEvents();
    }

    private static void registerClientEvents() {
        // Client tick
        ClientTickEvent.CLIENT_POST.register(client -> {
            // Handle client tick
        });

        // Input events
        ClientRawInputEvent.KEY_PRESSED.register((client, keyCode, scanCode, action, modifiers) -> {
            // Handle key press
            return EventResult.pass();
        });
    }
}
```

## Alternatives to Events

### When to Use Events vs. Networking

| Use Events When | Use Networking When |
|----------------|---------------------|
| Hooking into existing game mechanics | Sending custom data between client/server |
| Modifying vanilla behavior | Implementing custom protocols |
| Reacting to game state changes | Synchronizing custom data |
| The event already exists | You need fine control over packet timing |

### When to Use Events vs. Mixins

| Use Events When | Use Mixins When |
|----------------|-----------------|
| Architectury provides the event | No event exists for what you need |
| You want platform compatibility | You need to modify method internals |
| The event is sufficient | You need precise injection points |
| You want cleaner code | Events don't provide enough control |

## Debugging Events

### Checking if Events Fire

```java
TickEvent.SERVER_POST.register(server -> {
    System.out.println("Server tick event fired!");
});
```

### Checking Event Registration

```java
boolean isRegistered = TickEvent.SERVER_POST.isRegistered(myListener);
```

### Clearing All Listeners (for debugging)

```java
TickEvent.SERVER_POST.clearListeners();
```

## See Also

- **[Common Events Reference](06-common-events.md)** - Detailed documentation of all common events
- **[Client Events Reference](07-client-events.md)** - Detailed documentation of all client events
- [Networking](04-networking.md) - Alternative to events for custom data
- [Platform Abstraction](02-platform-abstraction.md) - Platform detection for conditional event registration

## Performance Considerations

### High-Frequency Events

Some events fire very frequently (like tick events). Keep these handlers optimized:

```java
// BAD - This creates objects every tick
TickEvent.SERVER_POST.register(server -> {
    List<ServerPlayer> players = new ArrayList<>(server.getPlayerList().getPlayers());
    // Process players...
});

// GOOD - Reuse data structures
private static List<ServerPlayer> playerCache = new ArrayList<>();

TickEvent.SERVER_POST.register(server -> {
    playerCache.clear();
    playerCache.addAll(server.getPlayerList().getPlayers());
    // Process players...
});
```

### Conditional Processing

Use early returns to avoid unnecessary work:

```java
TickEvent.SERVER_POST.register(server -> {
    // Only process every 20 ticks
    if (server.getTickCount() % 20 != 0) return;

    // Heavy processing here...
});
```

---

*Documentation generated from Architectury API 9.2.14 source code*
