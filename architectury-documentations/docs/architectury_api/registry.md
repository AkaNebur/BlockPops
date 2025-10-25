# Registry - Architectury Documentation

## Why is Registering Content So Complicated?

Forge has stricter requirements than other platforms. The framework "disallows you to use vanilla `Registry` class for registering content and requires you to register content in the `RegistryEvent.Register<T>` event," making the process different from Fabric or vanilla implementations.

## Submitting Your Mod Event Bus in Forge to Architectury API

To enable Architectury API to listen to registry events on Forge, you must expose your mod event bus. In your Forge mod constructor, add:

```java
EventBuses.registerModEventBus(MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
```

**Note:** This applies only to mods using the `javafml` language provider.

## Registering Content Through Architectury's Registries

### Via Registrar

Create a lazy registries object to prevent static load order crashes:

```java
// 1.19.4
public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

// 1.19.3 or below
public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MOD_ID));
```

Register items during mod initialization:

```java
// 1.19.4
Registrar<Item> items = MANAGER.get().get(Registries.ITEM);
RegistrySupplier<Item> exampleItem = items.register(new ResourceLocation(MOD_ID, "example_item"), () -> new Item(new Item.Properties()));

// 1.19.3 or below
Registrar<Item> items = REGISTRIES.get().get(Registry.ITEM_KEY);
RegistrySupplier<Item> exampleItem = items.register(new ResourceLocation(MOD_ID, "example_item"), () -> new Item(new Item.Properties()));
```

The returned `RegistrySupplier` represents content that may not yet be registered.

### Via DeferredRegister

Create and define a deferred register:

```java
// 1.19.4
public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);

// 1.19.3 or below
public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_REGISTRY);
```

Add entries to the deferred register (without accessing them immediately):

```java
public static final RegistrySupplier<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Properties()));
```

Register entries in your initialization block:

```java
ITEMS.register();
```

---

**License:** CC Attribution-Noncommercial-Share Alike 4.0 International

*Source: https://docs.architectury.dev/api/registry*
