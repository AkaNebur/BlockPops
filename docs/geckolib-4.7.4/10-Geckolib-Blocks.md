# Geckolib Blocks (Geckolib4) Documentation

## Overview

GeckoLib enables animation of Minecraft blocks through a specialized system. Since blocks are static by default, animation requires an associated `BlockEntity` to function properly.

## Key Requirement

"Because blocks in Minecraft are static objects, it is not possible to outright animate a block without it also having a `BlockEntity`." The library leverages the `BlockEntityRenderer` system to achieve animated blocks.

## Implementation Steps

Creating an animated block involves five main phases:

1. **Create Blockbench Model** - Design your 3D model
2. **Create Geo Model** - Set up the geometry configuration
3. **Create Block and BlockEntity Classes** - Implement core logic
4. **Disable Vanilla Rendering** - Tell Minecraft to skip default rendering
5. **Create and Register Renderer** - Enable custom visualization

## Block Class Setup

Your block must implement `EntityBlock` or a subclass thereof. Override the `getRenderShape` method:

```java
@Override
public RenderShape getRenderShape(BlockState state) {
    return RenderShape.ENTITYBLOCK_ANIMATED;
}
```

This prevents Minecraft from searching for blockstate files.

## BlockEntity Implementation

The BlockEntity should implement `GeoBlockEntity` and include:

- An `AnimatableInstanceCache` instance created via `GeckoLibUtil.createInstanceCache(this)`
- Override of `getAnimatableInstanceCache()` returning the cache
- Override of `registerControllers()` for animation handling

## Renderer Registration

Create a class extending `GeoBlockRenderer<YourBlockEntity>` and register it through either Forge or Fabric event systems.

## Automatic Features

"GeckoLib handles block rotations automatically, so if your block is directional the GeckoLib renderer will turn it automatically." Manual overrides are available if needed via the `rotateBlock` method.
