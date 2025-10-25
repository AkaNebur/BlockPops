# Geckolib Armor (Geckolib4) Documentation

## Pre-word

When creating armor models in Blockbench, ensure you select the `Armor` model type within the Geckolib Model Settings panel. This selection is essential for generating the appropriate project template and output files.

## Steps to Create GeckoLib Armor

The process involves five key stages:

1. Creating your Blockbench Model
2. Creating your Geo Model
3. Creating your item display JSON
4. Creating your Item class
5. Creating and providing your renderer

## The Item Class

### Overview

Armor item classes follow the same creation pattern as standard animatable items. The primary distinction is extending `ArmorItem` instead of the base `Item` class, which automatically handles equip functionality. Alternatively, implement the `Equipable` interface if extending `ArmorItem` isn't feasible.

### Core Requirements

The armor item must:
- Implement `GeoItem`
- Create an `AnimatableInstanceCache`
- Define animation controllers
- Register the renderer appropriately

### Key Implementation Details

Armor items require platform-specific renderer initialization:

**Neo/Forge (1.19.3-1.20.5):** Override `initializeClient()` to accept an `IClientItemExtensions` consumer and instantiate the renderer within `getHumanoidArmorModel()`

**Fabric:** Cache a renderer supplier via `GeoItem.makeRenderer()` and override `createRenderer()` to provide the renderer implementation

**Modern Versions (1.20.6+):** Override `createGeoRenderer()` with a `GeoRenderProvider` implementation

## The Renderer

### Creating the Renderer Class

Create a class extending `GeoArmorRenderer` and provide it with a `GeoModel` instance:

```java
public final class ExampleArmorRenderer extends GeoArmorRenderer<ExampleArmorItem> {
    public ExampleArmorRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ExampleMod.MOD_ID, "armor/example_armor")));
    }
}
```

### Registration Process

**Neo/Forge:** Instantiate the renderer within `getHumanoidArmorModel()` and call "prepForRender()" before returning

**Fabric:** Follow similar patterns using `RenderProvider` instead of `IClientItemExtensions`

**Modern Versions:** Use `createGeoRenderer()` with `GeoRenderProvider` for cleaner integration

The renderer must call "prepForRender() to prepare for each render frame" before being returned.

## Common Issues

### BipedEntityModel Not Found

This error typically indicates a Fabric environment using split sources where client code is separated from common code. The `GeoArmorRenderer` and related code must reside in client sources, not the common module.

## Additional Resources

- Video guides available for Neo/Forge and Fabric implementations
- Comprehensive examples demonstrating animation controller setup
- References to related documentation for Geo Models and item creation
