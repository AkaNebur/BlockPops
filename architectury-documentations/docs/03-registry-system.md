# Registry System

Architectury provides a unified registry system that works across both Fabric and Forge, abstracting the differences in how each platform handles registration.

## Why is Registration Complicated?

Forge has stricter requirements than Fabric. Forge requires you to register content through `RegistryEvent.Register<T>` events and disallows direct use of the vanilla `Registry` class. Architectury API handles these platform differences automatically.

## Setup for Forge

If you're using Forge, you must expose your mod event bus to Architectury API in your Forge mod constructor:

```java
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.javafml.FMLJavaModLoadingContext;

public class BlockPopsForge {
    public BlockPopsForge() {
        // Register mod event bus with Architectury
        EventBuses.registerModEventBus(MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Initialize common code
        BlockPops.init();
    }
}
```

**Note**: This only applies to mods using the `javafml` language provider.

## Registration Methods

Architectury provides two main approaches for registering content:

1. **Registrar** - Direct registration interface
2. **DeferredRegister** - Deferred registration (similar to Forge's system)

## Method 1: Using Registrar

### Creating a Registrar Manager

For Minecraft 1.19.4+:

```java
import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;

public class ModItems {
    private static final String MOD_ID = "blockpops";

    // Create lazy registrar manager to prevent static load order crashes
    public static final Supplier<RegistrarManager> MANAGER =
        Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    // Get the item registrar
    private static final Registrar<Item> ITEMS = MANAGER.get().get(Registries.ITEM);

    // Register items
    public static final RegistrySupplier<Item> EXAMPLE_ITEM =
        ITEMS.register(
            new ResourceLocation(MOD_ID, "example_item"),
            () -> new Item(new Item.Properties())
        );

    public static void init() {
        // Items are registered automatically when accessed
    }
}
```

For Minecraft 1.19.3 and below:

```java
import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;

public class ModItems {
    private static final String MOD_ID = "blockpops";

    public static final Supplier<Registries> REGISTRIES =
        Suppliers.memoize(() -> Registries.get(MOD_ID));

    private static final Registrar<Item> ITEMS =
        REGISTRIES.get().get(Registry.ITEM_KEY);

    public static final RegistrySupplier<Item> EXAMPLE_ITEM =
        ITEMS.register(
            new ResourceLocation(MOD_ID, "example_item"),
            () -> new Item(new Item.Properties())
        );

    public static void init() {
        // Items are registered automatically
    }
}
```

### Registering Blocks

```java
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.registries.Registries;

public class ModBlocks {
    private static final String MOD_ID = "blockpops";

    public static final Supplier<RegistrarManager> MANAGER =
        Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    private static final Registrar<Block> BLOCKS =
        MANAGER.get().get(Registries.BLOCK);

    public static final RegistrySupplier<Block> EXAMPLE_BLOCK =
        BLOCKS.register(
            new ResourceLocation(MOD_ID, "example_block"),
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE))
        );

    public static void init() {}
}
```

### Registering Block Entities

```java
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.registries.Registries;

public class ModBlockEntities {
    private static final Registrar<BlockEntityType<?>> BLOCK_ENTITIES =
        MANAGER.get().get(Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<ExampleBlockEntity>> EXAMPLE_BLOCK_ENTITY =
        BLOCK_ENTITIES.register(
            new ResourceLocation(MOD_ID, "example_block_entity"),
            () -> BlockEntityType.Builder.of(
                ExampleBlockEntity::new,
                ModBlocks.EXAMPLE_BLOCK.get()
            ).build(null)
        );

    public static void init() {}
}
```

## Method 2: Using DeferredRegister

DeferredRegister is similar to Forge's registration system and may be more familiar if you're coming from Forge.

### Creating a DeferredRegister

For Minecraft 1.19.4+:

```java
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public class ModItems {
    private static final String MOD_ID = "blockpops";

    // Create deferred register
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(MOD_ID, Registries.ITEM);

    // Register items (don't access them immediately!)
    public static final RegistrySupplier<Item> EXAMPLE_ITEM =
        ITEMS.register("example_item", () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> ANOTHER_ITEM =
        ITEMS.register("another_item", () -> new Item(new Item.Properties()));

    public static void init() {
        // Must call register() to actually register the entries
        ITEMS.register();
    }
}
```

For Minecraft 1.19.3 and below:

```java
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Registry;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(MOD_ID, Registry.ITEM_REGISTRY);

    public static final RegistrySupplier<Item> EXAMPLE_ITEM =
        ITEMS.register("example_item", () -> new Item(new Item.Properties()));

    public static void init() {
        ITEMS.register();
    }
}
```

### Multiple Registry Types

```java
public class ModRegistry {
    private static final String MOD_ID = "blockpops";

    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(MOD_ID, Registries.BLOCK);

    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(MOD_ID, Registries.ITEM);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    // Register content...
    public static final RegistrySupplier<Block> MY_BLOCK =
        BLOCKS.register("my_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistrySupplier<Item> MY_BLOCK_ITEM =
        ITEMS.register("my_block", () -> new BlockItem(MY_BLOCK.get(), new Item.Properties()));

    public static void init() {
        BLOCKS.register();
        ITEMS.register();
        BLOCK_ENTITIES.register();
    }
}
```

## RegistrySupplier

Both methods return `RegistrySupplier<T>`, which represents content that may not yet be registered:

```java
// Get the registered object
Item item = ModItems.EXAMPLE_ITEM.get();

// Get the registry ID
ResourceLocation id = ModItems.EXAMPLE_ITEM.getId();

// Check if registered
boolean isPresent = ModItems.EXAMPLE_ITEM.isPresent();

// Use in recipes or other places that need a Supplier
Supplier<Item> itemSupplier = ModItems.EXAMPLE_ITEM;
```

## Registrar Interface Methods

The `Registrar<T>` interface provides additional utility methods:

```java
Registrar<Item> items = MANAGER.get().get(Registries.ITEM);

// Create a delegate for an existing registry entry
RegistrySupplier<Item> delegate = items.delegate(new ResourceLocation("minecraft", "diamond"));

// Wrap an existing object
RegistrySupplier<Item> wrapped = items.wrap(Items.DIAMOND);

// Get registry information
ResourceLocation id = items.getId(someItem);
int rawId = items.getRawId(someItem);
Optional<ResourceKey<Item>> key = items.getKey(someItem);

// Query the registry
Item item = items.get(new ResourceLocation("minecraft", "diamond"));
boolean contains = items.contains(new ResourceLocation("blockpops", "example_item"));
Set<ResourceLocation> allIds = items.getIds();
```

## Listening to Registration

You can listen for when registry entries are registered:

```java
// Listen to a specific entry
ITEMS.listen(ModItems.EXAMPLE_ITEM, item -> {
    System.out.println("Example item was registered: " + item);
});

// Listen to an ID
ITEMS.listen(new ResourceLocation(MOD_ID, "example_item"), item -> {
    System.out.println("Item registered: " + item);
});
```

**Note**: On Fabric, the callback is called when the registry entry is registered. On Forge, it's called when the entry is registered OR when Minecraft has started.

## Best Practices

1. **Use Lazy Initialization**: Always use `Suppliers.memoize()` to create your registrar manager to avoid static initialization order issues

2. **Don't Access Too Early**: With DeferredRegister, don't call `.get()` on entries before calling `register()`

3. **Initialize in Order**: Register blocks before items if you have BlockItems that reference blocks

4. **Single Source of Truth**: Keep all registration in one place per registry type

## Complete Example

```java
import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModRegistry {
    private static final String MOD_ID = "blockpops";

    // Deferred registers
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(MOD_ID, Registries.BLOCK);
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(MOD_ID, Registries.ITEM);

    // Blocks
    public static final RegistrySupplier<Block> EXAMPLE_BLOCK =
        BLOCKS.register("example_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    // Items
    public static final RegistrySupplier<Item> EXAMPLE_BLOCK_ITEM =
        ITEMS.register("example_block",
            () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> EXAMPLE_ITEM =
        ITEMS.register("example_item",
            () -> new Item(new Item.Properties()));

    public static void init() {
        // Register all deferred registers
        BLOCKS.register();
        ITEMS.register();
    }
}
```

## See Also

- [Getting Started](01-getting-started.md)
- [Platform Abstraction](02-platform-abstraction.md)
- Official Architectury Docs: https://docs.architectury.dev/api/registry

---

*Documentation generated from Architectury API 9.2.14 source code*
