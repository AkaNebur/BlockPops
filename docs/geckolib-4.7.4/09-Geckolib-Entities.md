# Geckolib Entities (Geckolib4) Documentation

## Overview
This documentation outlines how to create animated entities using GeckoLib4, a Minecraft modding library for animation handling.

## Setup Steps

Creating a GeckoLib entity requires four main steps:

1. Create a Blockbench model
2. Generate a Geo Model
3. Build the entity class
4. Create and register the renderer

Steps 1-2 are covered in separate documentation; this guide focuses on steps 3-4.

## Entity Class Requirements

The entity class must:

- Implement the `GeoEntity` interface
- Override `getAnimatableInstanceCache()` and `registerControllers()`
- Create an `AnimatableInstanceCache` instance using `GeckoLibUtil.createInstanceCache(this)`
- Define animation controllers in `registerControllers()`

### Key Implementation Pattern

```
public class ExampleEntity extends PathfinderMob implements GeoEntity {
    private final AnimatableInstanceCache geoCache =
        GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
```

## Renderer Setup

Entities require registration using `GeoEntityRenderer` rather than vanilla renderers. This approach eliminates the need for separate model layer and mesh registration. The renderer accepts the rendering context and a Geo Model instance.

## Common Issues and Solutions

**Spawn Crash**: Occurs when the renderer isn't registered. Verify renderer registration in your setup code.

**Attack Animation Not Swinging**: Requires calling `swing()` or using appropriate goals like `MeleeAttackGoal`. Non-Monster entities need `aiStep()` override with `updateSwingTime()` call.

**Animation Cuts Off Early**: If animations exceed default swing duration (6 ticks), override `getCurrentSwingDuration()` for appropriate timing.

**Cannot Replay Animation**: Reset animations after completion or verify animation JSON doesn't use `hold_on_last_frame` loop type.

## Video Resources

Complete tutorials available for both Neo/Forge and Fabric modloader implementations.
