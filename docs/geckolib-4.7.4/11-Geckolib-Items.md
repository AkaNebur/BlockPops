# GeckoLib Items (GeckoLib4) Documentation

## Overview

This documentation covers creating animated items in Minecraft using GeckoLib4. The key distinction is that items require special handling because Minecraft maintains only one instance per item type, unlike entities or block entities.

## Core Steps

The process involves five main stages:

1. Create a Blockbench model
2. Build a Geo Model class
3. Generate an item display JSON file
4. Implement the item class
5. Create and register a renderer

## Item Class Implementation

### Basic Requirements

Your item must implement `GeoItem` interface. The essential components include:

- **AnimatableInstanceCache**: Created via `GeckoLibUtil.createInstanceCache(this)` and stored as a final field
- **getAnimatableInstanceCache()**: Override to return the cache instance
- **registerControllers()**: Define animation behavior through `AnimationController` instances
- **SingletonGeoAnimatable registration**: Call `registerSyncedAnimatable(this)` in the constructor to enable server-side animation syncing

### Advanced Features

**Triggerable Animations**: These allow server-side animation triggering using the `.triggerableAnim()` method, enabling animations to be initiated from game logic.

**Perspective-Aware Handling**: Override `isPerspectiveAware()` returning true, then access render perspective via `state.getData(DataTickets.ITEM_RENDER_PERSPECTIVE)` to differentiate first-person and third-person animations.

## Item Display JSON

The item model file requires minimal configuration:

```json
{
  "parent": "builtin/entity"
}
```

The `"parent": "builtin/entity"` directive signals Minecraft to delegate rendering to GeckoLib rather than using vanilla item rendering.

## Renderer Implementation

### Class Creation

Extend `GeoItemRenderer<YourItem>` and pass your `GeoModel` instance:

```java
public class ExampleItemRenderer extends GeoItemRenderer<ExampleItem> {
    public ExampleItemRenderer() {
        super(new ExampleItemModel());
    }
}
```

### Registration Methods

**Forge (1.21.1+)**: Override `createGeoRenderer()` accepting a `Consumer<GeoRenderProvider>`, instantiating and caching your renderer within the provider's `getGeoItemRenderer()` method.

**Fabric (1.21.1+)**: Same approach as Forge—override `createGeoRenderer()` with cache-lazy renderer instantiation.

**Legacy Forge (1.19.3-1.20.6)**: Use `initializeClient()` with `IClientItemExtensions` to provide `getCustomRenderer()`.

**Legacy Fabric (1.19.3-1.20.6)**: Override `createRenderer()` and `getRenderProvider()`, with the latter returning a cached `Supplier<Object>` created via `GeoItem.makeRenderer(this)`.

## Troubleshooting

| Issue | Cause | Solution |
|-------|-------|----------|
| Black & purple cube | Missing item model JSON | Create `.json` in `models/item` folder |
| Black & purple particles | Missing particle texture | Add particle texture reference to item JSON |
| Invisible item | Missing parent directive | Add `"parent": "builtin/entity"` to JSON |
| Animation won't replay | Animation not reset or locked | Check loop type isn't `hold_on_last_frame`; implement proper reset logic |

## Key Resources

- Full video tutorials available for Neo/Forge and Fabric implementations
- Example implementations provided for versions 1.19.3 through 1.21.1
- Related documentation covers Blockbench modeling, Geo Models, and triggerable animations
