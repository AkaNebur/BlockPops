# Client Events Reference

Client events only run on the physical client side. Always use `@Environment(EnvType.CLIENT)` when registering these events.

Located in `dev.architectury.event.events.client.*`.

## Tick Events

Located in `ClientTickEvent` class.

### CLIENT_PRE / CLIENT_POST

Called before/after each client tick.

```java
@Environment(EnvType.CLIENT)
public class ClientTickHandler {
    public static void init() {
        ClientTickEvent.CLIENT_PRE.register(client -> {
            // Pre-tick logic
        });

        ClientTickEvent.CLIENT_POST.register(client -> {
            // Post-tick logic
        });
    }
}
```

### CLIENT_PLAYER_PRE / CLIENT_PLAYER_POST

Called before/after the client player ticks (only when player exists).

```java
ClientTickEvent.CLIENT_PLAYER_PRE.register(player -> {
    // Per-player client tick
});
```

### CLIENT_LEVEL_PRE / CLIENT_LEVEL_POST

Called before/after the client level ticks.

```java
ClientTickEvent.CLIENT_LEVEL_PRE.register(level -> {
    // Per-level client tick
});
```

## Lifecycle Events

Located in `ClientLifecycleEvent` class.

### CLIENT_SETUP

Called during client initialization.

```java
ClientLifecycleEvent.CLIENT_SETUP.register(client -> {
    // Client setup logic
});
```

### CLIENT_STARTED

Called when the client has fully started.

```java
ClientLifecycleEvent.CLIENT_STARTED.register(client -> {
    // Client fully initialized
});
```

### CLIENT_STOPPING

Called when the client is shutting down.

```java
ClientLifecycleEvent.CLIENT_STOPPING.register(client -> {
    // Save client settings, cleanup
});
```

### CLIENT_LEVEL_LOAD / CLIENT_LEVEL_UNLOAD

Called when the client loads/unloads a level.

```java
ClientLifecycleEvent.CLIENT_LEVEL_LOAD.register(level -> {
    // Client level loaded
});
```

## Player Events

Located in `ClientPlayerEvent` class.

### CLIENT_PLAYER_JOIN / CLIENT_PLAYER_QUIT

Called when the client player joins/leaves a world.

```java
ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(player -> {
    // Client player joined world
});

ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(player -> {
    // Client player left world
});
```

### CLIENT_PLAYER_RESPAWN

Called when the client player respawns.

```java
ClientPlayerEvent.CLIENT_PLAYER_RESPAWN.register((oldPlayer, newPlayer) -> {
    // Handle client respawn
});
```

## Input Events

Located in `ClientRawInputEvent` and `ClientScreenInputEvent` classes.

### KEY_PRESSED

Called when a key is pressed (raw input, even in GUIs).

```java
ClientRawInputEvent.KEY_PRESSED.register((client, keyCode, scanCode, action, modifiers) -> {
    if (keyCode == GLFW.GLFW_KEY_F && action == GLFW.GLFW_PRESS) {
        // Handle F key
        return EventResult.interruptTrue();
    }
    return EventResult.pass();
});
```

### MOUSE_CLICKED_PRE / MOUSE_CLICKED_POST

Called when mouse buttons are clicked (raw input).

```java
ClientRawInputEvent.MOUSE_CLICKED_PRE.register((client, button, action, modifiers) -> {
    if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
        // Handle right click
    }
    return EventResult.pass();
});
```

### MOUSE_SCROLLED

Called when the mouse wheel is scrolled.

```java
ClientRawInputEvent.MOUSE_SCROLLED.register((client, amount) -> {
    if (someCondition) {
        return EventResult.interruptTrue(); // Cancel scroll
    }
    return EventResult.pass();
});
```

### Screen Input Events

For input when a screen is open:

```java
ClientScreenInputEvent.KEY_PRESSED_PRE.register((client, screen, keyCode, scanCode, modifiers) -> {
    // Handle key in GUI
    return EventResult.pass();
});

ClientScreenInputEvent.MOUSE_CLICKED_PRE.register((client, screen, mouseX, mouseY, button) -> {
    // Handle mouse click in GUI
    return EventResult.pass();
});
```

## GUI/Screen Events

Located in `ClientGuiEvent` class.

### RENDER_HUD

Called when the HUD is rendered. Can cancel.

```java
ClientGuiEvent.RENDER_HUD.register((guiGraphics, tickDelta) -> {
    // Custom HUD rendering
    guiGraphics.drawString(Minecraft.getInstance().font, "Custom HUD", 10, 10, 0xFFFFFF);
});
```

### SET_SCREEN

Called when a screen is opened. Can modify or cancel.

```java
ClientGuiEvent.SET_SCREEN.register(screen -> {
    if (screen instanceof PauseScreen) {
        // Modify or replace pause screen
        return CompoundEventResult.interruptDefault(); // Use default behavior
    }
    return CompoundEventResult.pass();
});
```

### INIT_PRE / INIT_POST

Called before/after a screen is initialized.

```java
ClientGuiEvent.INIT_PRE.register((client, screen, scaledWidth, scaledHeight) -> {
    // Modify screen before widgets are added
});

ClientGuiEvent.INIT_POST.register((client, screen, scaledWidth, scaledHeight) -> {
    // Add custom widgets to screen
});
```

### RENDER_SCREEN

Called when a screen is rendered.

```java
ClientGuiEvent.RENDER.register((screen, guiGraphics, mouseX, mouseY, tickDelta) -> {
    // Custom rendering on screens
});
```

## Tooltip Events

Located in `ClientTooltipEvent` class.

### RENDER_PRE

Called before a tooltip is rendered. Can modify position or cancel.

```java
ClientTooltipEvent.RENDER_PRE.register((stack, lines, guiGraphics, x, y) -> {
    // Modify tooltip position or cancel
    return CompoundEventResult.pass();
});
```

### ITEM

Called to modify item tooltips.

```java
ClientTooltipEvent.ITEM.register((stack, lines, context) -> {
    if (stack.is(Items.DIAMOND)) {
        lines.add(Component.literal("Shiny!"));
    }
});
```

## Chat Events

Located in `ClientChatEvent` class.

### RECEIVED

Called when a chat message is received on the client.

```java
ClientChatEvent.RECEIVED.register((type, message, sender) -> {
    // Process received chat
    return EventResult.pass();
});
```

### SEND

Called when the client sends a chat message. Can cancel or modify.

```java
ClientChatEvent.SEND.register(message -> {
    if (message.startsWith("/custom")) {
        // Handle custom command
        return CompoundEventResult.interruptFalse();
    }
    return CompoundEventResult.pass();
});
```

## Texture Events

Located in `ClientTextureStitchEvent` class.

### PRE / POST

Called before/after texture atlases are stitched.

```java
ClientTextureStitchEvent.PRE.register((atlas, sprites) -> {
    if (atlas.location().equals(new ResourceLocation("minecraft", "textures/atlas/blocks.png"))) {
        sprites.accept(new ResourceLocation("blockpops", "custom_texture"));
    }
});
```

## Recipe Events

Located in `ClientRecipeUpdateEvent` class.

### EVENT

Called when recipes are updated on the client.

```java
ClientRecipeUpdateEvent.EVENT.register((recipeManager) -> {
    // Handle recipe sync
});
```

## Shader Events

Located in `ClientReloadShadersEvent` class.

### EVENT

Called when shaders are reloaded (F3+T).

```java
ClientReloadShadersEvent.EVENT.register((resourceManager, reloadState) -> {
    // Handle shader reload
});
```

## Command Registration (Client)

Located in `ClientCommandRegistrationEvent` class.

### EVENT

Register client-side commands.

```java
ClientCommandRegistrationEvent.EVENT.register(dispatcher -> {
    dispatcher.register(
        Commands.literal("clientcommand")
            .executes(context -> {
                // Client-only command
                return 1;
            })
    );
});
```

## Complete Examples

### Custom HUD Overlay

```java
@Environment(EnvType.CLIENT)
public class CustomHUD {
    public static void init() {
        ClientGuiEvent.RENDER_HUD.register((guiGraphics, tickDelta) -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;

            // Draw custom HUD element
            int x = 10;
            int y = 10;
            String text = "Health: " + (int)mc.player.getHealth();

            guiGraphics.drawString(mc.font, text, x, y, 0xFF0000);
        });
    }
}
```

### Custom Keybinding Handler

```java
@Environment(EnvType.CLIENT)
public class KeybindHandler {
    private static KeyMapping customKey;

    public static void init() {
        // Register keybinding
        customKey = new KeyMapping(
            "key.blockpops.custom",
            GLFW.GLFW_KEY_G,
            "key.categories.misc"
        );

        KeyMappingRegistry.register(customKey);

        // Handle key press
        ClientTickEvent.CLIENT_POST.register(client -> {
            while (customKey.consumeClick()) {
                // Key was pressed
                if (client.player != null) {
                    client.player.displayClientMessage(
                        Component.literal("Custom key pressed!"),
                        true
                    );
                }
            }
        });
    }
}
```

### Screen Modification

```java
@Environment(EnvType.CLIENT)
public class ScreenModifier {
    public static void init() {
        ClientGuiEvent.INIT_POST.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof InventoryScreen) {
                // Add custom button to inventory
                screen.addRenderableWidget(
                    Button.builder(
                        Component.literal("Custom"),
                        button -> {
                            // Button action
                        }
                    )
                    .bounds(scaledWidth / 2 + 10, 10, 60, 20)
                    .build()
                );
            }
        });
    }
}
```

### Tooltip Modifier

```java
@Environment(EnvType.CLIENT)
public class TooltipModifier {
    public static void init() {
        ClientTooltipEvent.ITEM.register((stack, lines, context) -> {
            if (stack.getItem() instanceof SwordItem) {
                int damage = /* calculate damage */;
                lines.add(Component.literal("Total Damage: " + damage)
                    .withStyle(ChatFormatting.RED));
            }
        });
    }
}
```

### Chat Filter

```java
@Environment(EnvType.CLIENT)
public class ChatFilter {
    private static final Set<String> FILTERED_WORDS = Set.of("spam", "ad");

    public static void init() {
        ClientChatEvent.RECEIVED.register((type, message, sender) -> {
            String messageText = message.getString().toLowerCase();

            for (String word : FILTERED_WORDS) {
                if (messageText.contains(word)) {
                    return EventResult.interruptFalse(); // Filter message
                }
            }

            return EventResult.pass();
        });
    }
}
```

## Best Practices

1. **Environment Annotations**: Always use `@Environment(EnvType.CLIENT)` for classes that register client events

2. **Null Checks**: Client player and level can be null. Always check before using:
   ```java
   if (minecraft.player != null && minecraft.level != null) {
       // Safe to use
   }
   ```

3. **Performance**: Client tick events fire 20 times per second. Keep handlers lightweight.

4. **Screen Events**: Don't assume screen types. Use `instanceof` checks:
   ```java
   if (screen instanceof InventoryScreen inventoryScreen) {
       // Work with inventory screen
   }
   ```

5. **Input Handling**: Use `consumeClick()` for key mappings to prevent double-firing:
   ```java
   while (keyMapping.consumeClick()) {
       // Handle press once
   }
   ```

6. **Rendering**: Use `GuiGraphics` for all rendering in modern Minecraft versions

## Common Pitfalls

### Wrong Event Type

```java
// BAD - Using common event on client
TickEvent.PLAYER_POST.register(player -> {
    // This runs on server player!
});

// GOOD - Using client event
@Environment(EnvType.CLIENT)
ClientTickEvent.CLIENT_PLAYER_POST.register(player -> {
    // This runs on client player
});
```

### Accessing Client from Server

```java
// BAD - Will crash on dedicated server
Minecraft.getInstance().player.sendSystemMessage(...);

// GOOD - Check environment first
if (Platform.getEnvironment() == Env.CLIENT) {
    // Client-only code in separate method
}
```

### Not Checking Null

```java
// BAD - Can crash
ClientTickEvent.CLIENT_POST.register(client -> {
    client.player.heal(1.0f); // player can be null!
});

// GOOD - Null check
ClientTickEvent.CLIENT_POST.register(client -> {
    if (client.player != null) {
        client.player.heal(1.0f);
    }
});
```

## See Also

- [Events Overview](05-events-overview.md)
- [Common Events](06-common-events.md)
- [Platform Abstraction](02-platform-abstraction.md)

---

*Documentation generated from Architectury API 9.2.14 source code*
