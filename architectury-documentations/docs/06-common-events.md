# Common Events Reference

Common events work on both client and server sides. These events are located in `dev.architectury.event.events.common.*`.

## Player Events

Located in `PlayerEvent` class.

### PLAYER_JOIN

Called when a player joins the server.

```java
PlayerEvent.PLAYER_JOIN.register(player -> {
    player.sendSystemMessage(Component.literal("Welcome!"));
});
```

### PLAYER_QUIT

Called when a player leaves the server.

```java
PlayerEvent.PLAYER_QUIT.register(player -> {
    // Save player data, cleanup, etc.
});
```

### PLAYER_RESPAWN

Called when a player respawns (death, dimension change, end return).

```java
PlayerEvent.PLAYER_RESPAWN.register((newPlayer, conqueredEnd) -> {
    if (conqueredEnd) {
        // Player just beat the dragon
    }
});
```

### PLAYER_CLONE

Called when a player is cloned (respawn, dimension change). Use this to copy custom data.

```java
PlayerEvent.PLAYER_CLONE.register((oldPlayer, newPlayer, wonGame) -> {
    // Copy custom data from old to new player
});
```

### PLAYER_ADVANCEMENT

Called when a player earns an advancement.

```java
PlayerEvent.PLAYER_ADVANCEMENT.register((player, advancement) -> {
    // React to advancement completion
});
```

### CRAFT_ITEM

Called when a player crafts an item.

```java
PlayerEvent.CRAFT_ITEM.register((player, crafted, container) -> {
    if (crafted.is(Items.DIAMOND_PICKAXE)) {
        // First diamond pickaxe logic
    }
});
```

### SMELT_ITEM

Called when a player smelts an item.

```java
PlayerEvent.SMELT_ITEM.register((player, smelted) -> {
    // React to smelting
});
```

### PICKUP_ITEM_PRE

Called before a player picks up an item. Can cancel pickup.

```java
PlayerEvent.PICKUP_ITEM_PRE.register((player, itemEntity, stack) -> {
    if (someCondition) {
        return EventResult.interruptFalse(); // Cancel pickup
    }
    return EventResult.pass();
});
```

### PICKUP_ITEM_POST

Called after a player picks up an item.

```java
PlayerEvent.PICKUP_ITEM_POST.register((player, itemEntity, stack) -> {
    // React to item pickup
});
```

### DROP_ITEM

Called when a player drops an item. Can cancel.

```java
PlayerEvent.DROP_ITEM.register((player, itemEntity) -> {
    if (importantItem) {
        return EventResult.interruptFalse(); // Prevent dropping
    }
    return EventResult.pass();
});
```

### CHANGE_DIMENSION

Called when a player changes dimensions.

```java
PlayerEvent.CHANGE_DIMENSION.register((player, oldDimension, newDimension) -> {
    // Handle dimension change
});
```

### OPEN_MENU / CLOSE_MENU

Called when containers are opened/closed.

```java
PlayerEvent.OPEN_MENU.register((player, menu) -> {
    // Track container opens
});

PlayerEvent.CLOSE_MENU.register((player, menu) -> {
    // Track container closes
});
```

### FILL_BUCKET

Called when a player fills a bucket. Can modify result.

```java
PlayerEvent.FILL_BUCKET.register((player, level, stack, hitResult) -> {
    if (customFluid) {
        return CompoundEventResult.interruptTrue(customBucket);
    }
    return CompoundEventResult.pass();
});
```

### ATTACK_ENTITY

Called when a player attacks an entity. Can cancel.

```java
PlayerEvent.ATTACK_ENTITY.register((player, level, entity, hand, hitResult) -> {
    if (entity instanceof Villager) {
        return EventResult.interruptFalse(); // Protect villagers
    }
    return EventResult.pass();
});
```

## Entity Events

Located in `EntityEvent` class.

### ADD

Called when an entity is added to the world.

```java
EntityEvent.ADD.register((entity, level) -> {
    // Track entity spawning
});
```

### LIVING_DEATH

Called when a living entity dies. Can cancel.

```java
EntityEvent.LIVING_DEATH.register((entity, damageSource) -> {
    if (entity.hasEffect(TOTEM_EFFECT)) {
        return EventResult.interruptFalse(); // Prevent death
    }
    return EventResult.pass();
});
```

### LIVING_HURT

Called when a living entity is hurt. Can cancel.

```java
EntityEvent.LIVING_HURT.register((entity, damageSource, amount) -> {
    if (entity.hasEffect(INVULNERABILITY)) {
        return EventResult.interruptFalse(); // Cancel damage
    }
    return EventResult.pass();
});
```

## Block Events

Located in `BlockEvent` class.

### BREAK

Called when a block is broken. Can cancel.

```java
BlockEvent.BREAK.register((level, pos, state, player, xp) -> {
    if (protectedArea) {
        return EventResult.interruptFalse(); // Cancel break
    }
    return EventResult.pass();
});
```

### PLACE

Called when a block is placed. Can cancel.

```java
BlockEvent.PLACE.register((level, pos, state, placer) -> {
    if (!canBuildHere) {
        return EventResult.interruptFalse();
    }
    return EventResult.pass();
});
```

## Interaction Events

Located in `InteractionEvent` class.

### RIGHT_CLICK_BLOCK

Called when a player right-clicks a block.

```java
InteractionEvent.RIGHT_CLICK_BLOCK.register((player, hand, pos, face) -> {
    if (player.getItemInHand(hand).is(CUSTOM_ITEM)) {
        // Custom interaction logic
        return EventResult.interruptTrue();
    }
    return EventResult.pass();
});
```

### LEFT_CLICK_BLOCK

Called when a player left-clicks a block.

```java
InteractionEvent.LEFT_CLICK_BLOCK.register((player, hand, pos, face) -> {
    // Handle left click
    return EventResult.pass();
});
```

### INTERACT_ENTITY

Called when a player interacts with an entity.

```java
InteractionEvent.INTERACT_ENTITY.register((player, entity, hand) -> {
    if (entity instanceof Villager) {
        // Custom villager interaction
        return EventResult.interruptTrue();
    }
    return EventResult.pass();
});
```

## Tick Events

Located in `TickEvent` class.

### SERVER_PRE / SERVER_POST

Called before/after each server tick.

```java
TickEvent.SERVER_PRE.register(server -> {
    // Pre-tick logic
});

TickEvent.SERVER_POST.register(server -> {
    // Post-tick logic
});
```

### PLAYER_PRE / PLAYER_POST

Called before/after each player tick.

```java
TickEvent.PLAYER_PRE.register(player -> {
    // Per-player pre-tick
});

TickEvent.PLAYER_POST.register(player -> {
    // Per-player post-tick
});
```

### SERVER_LEVEL_PRE / SERVER_LEVEL_POST

Called for each world tick.

```java
TickEvent.SERVER_LEVEL_PRE.register(level -> {
    // Per-world pre-tick
});
```

## Lifecycle Events

Located in `LifecycleEvent` class.

### SETUP

Called during mod initialization (after registry).

```java
LifecycleEvent.SETUP.register(() -> {
    // Setup logic
});
```

### SERVER_STARTING / SERVER_STARTED

Called when server is starting/started.

```java
LifecycleEvent.SERVER_STARTING.register(server -> {
    // Server is starting
});

LifecycleEvent.SERVER_STARTED.register(server -> {
    // Server fully started
});
```

### SERVER_STOPPING / SERVER_STOPPED

Called when server is stopping/stopped.

```java
LifecycleEvent.SERVER_STOPPING.register(server -> {
    // Save data before shutdown
});

LifecycleEvent.SERVER_STOPPED.register(server -> {
    // Cleanup after shutdown
});
```

### SERVER_LEVEL_LOAD / SERVER_LEVEL_UNLOAD

Called when worlds load/unload.

```java
LifecycleEvent.SERVER_LEVEL_LOAD.register(level -> {
    // World loaded
});
```

## Command Events

Located in `CommandRegistrationEvent` class.

### REGISTRATION

Called to register commands.

```java
CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
    LiteralArgumentBuilder<CommandSourceStack> command =
        Commands.literal("mycommand")
            .executes(context -> {
                // Command logic
                return 1;
            });

    dispatcher.register(command);
});
```

## Chat Events

Located in `ChatEvent` class.

### RECEIVED

Called when a chat message is received on the server. Can modify or cancel.

```java
ChatEvent.RECEIVED.register((player, message) -> {
    if (message.contains("badword")) {
        return CompoundEventResult.interruptFalse();
    }
    return CompoundEventResult.pass();
});
```

## Chunk Events

Located in `ChunkEvent` class.

### LOAD / UNLOAD

Called when chunks load/unload.

```java
ChunkEvent.LOAD.register((level, chunk) -> {
    // Chunk loaded
});

ChunkEvent.UNLOAD.register((level, chunk) -> {
    // Chunk unloaded
});
```

## Explosion Events

Located in `ExplosionEvent` class.

### PRE / DETONATE

Called before/during explosions. Can modify affected blocks.

```java
ExplosionEvent.DETONATE.register((level, explosion, affectedBlocks, affectedEntities) -> {
    affectedBlocks.removeIf(pos -> level.getBlockState(pos).is(PROTECTED_BLOCK));
});
```

## Lightning Events

Located in `LightningEvent` class.

### STRIKE

Called when lightning strikes. Can cancel.

```java
LightningEvent.STRIKE.register((entity, level, pos, state) -> {
    if (protectedArea) {
        return EventResult.interruptFalse();
    }
    return EventResult.pass();
});
```

## Loot Events

Located in `LootEvent` class.

### MODIFY_LOOT_TABLE

Called to modify loot tables.

```java
LootEvent.MODIFY_LOOT_TABLE.register((resourceManager, lootManager, id, context, builtin) -> {
    if (id.equals(new ResourceLocation("minecraft", "chests/simple_dungeon"))) {
        context.addPool(LootPool.lootPool()
            .add(LootItem.lootTableItem(Items.DIAMOND))
            .build());
    }
});
```

## Complete Example: Custom Game Mechanic

```java
public class CustomMechanics {
    private static final Map<UUID, Integer> playerPoints = new HashMap<>();

    public static void init() {
        // Award points for kills
        EntityEvent.LIVING_DEATH.register((entity, damageSource) -> {
            if (damageSource.getEntity() instanceof ServerPlayer player) {
                playerPoints.merge(player.getUUID(), 10, Integer::sum);
                player.sendSystemMessage(
                    Component.literal("Points: " + playerPoints.get(player.getUUID()))
                );
            }
            return EventResult.pass();
        });

        // Reset points on death
        EntityEvent.LIVING_DEATH.register((entity, damageSource) -> {
            if (entity instanceof ServerPlayer player) {
                playerPoints.put(player.getUUID(), 0);
            }
            return EventResult.pass();
        });

        // Cleanup on quit
        PlayerEvent.PLAYER_QUIT.register(player -> {
            playerPoints.remove(player.getUUID());
        });
    }
}
```

## See Also

- [Events Overview](05-events-overview.md)
- [Client Events](07-client-events.md)
- [Networking](04-networking.md)

---

*Documentation generated from Architectury API 9.2.14 source code*
