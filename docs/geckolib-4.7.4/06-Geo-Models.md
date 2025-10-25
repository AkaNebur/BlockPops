# Geo Models (Geckolib4) - Documentation

## Overview

Geo models serve as the foundational link between animatable resources, animation handling, and related data in GeckoLib. Every animatable object requires a corresponding Geo Model.

## Creating a Geo Model

To develop a new Geo Model, extend the `GeoModel` class and implement three required methods:

### Required Methods

**getModelResource**: Returns the `ResourceLocation` pointing to your `.geo.json` model file

**getTextureResource**: Returns the `ResourceLocation` pointing to your `.png` texture file

**getAnimationResource**: Returns the `ResourceLocation` pointing to your `.animation.json` animation file

### Example Implementation

```java
public class ExampleModel extends GeoModel<ExampleItem> {
    private final ResourceLocation model = new ResourceLocation(
        GeckoLib.ModID, "geo/example.geo.json");
    private final ResourceLocation texture = new ResourceLocation(
        GeckoLib.ModID, "textures/item/example.png");
    private final ResourceLocation animations = new ResourceLocation(
        GeckoLib.ModID, "animations/example.animation.json");

    @Override
    public ResourceLocation getModelLocation(ExampleItem object) {
        return this.model;
    }

    @Override
    public ResourceLocation getTextureLocation(ExampleItem object) {
        return this.texture;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ExampleItem object) {
        return this.animations;
    }
}
```

## File Organization

GeckoLib expects asset files in specific directory structures:

- **Models**: `resources/assets/<modid>/geo/` (or subdirectories)
- **Animations**: `resources/assets/<modid>/animations/` (or subdirectories)
- **Textures**: `resources/assets/<modid>/textures/` (or subdirectories)

## Defaulted Models

GeckoLib4 introduced pre-configured model classes that automatically establish file paths, encouraging consistent organization:

```java
new DefaultedGeoEntityModel(new ResourceLocation(GeckoLib.MOD_ID, "bat"));
```

This automatically sets:
- Texture: `textures/entity/bat.png`
- Model: `geo/entity/bat.geo.json`
- Animation: `animations/entity/bat.animation.json`

Using defaulted models is the recommended approach for simplifying implementation.
