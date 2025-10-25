# Emissive Textures (Geckolib4) - Documentation

## Overview

GeckoLib provides built-in functionality for creating fullbright or glowing texture effects on animatable objects, similar to how vanilla spider eyes glow in darkness. This feature is implemented through a `GeoRenderLayer` called `AutoGlowingGeoLayer`.

## Usage

To implement emissive rendering in your animatable renderer, add an instance of `AutoGlowingGeoLayer` to the renderer's constructor, following the same pattern used for adding standard render layers.

## Texture Preparation

The implementation requires a specialized texture file for controlling which portions glow:

**Naming Convention:**
Create a duplicate of your base texture and rename it with `_glowmask` appended before the file extension.

Example:
- Base texture: `my_entity_texture.png`
- Glow mask: `my_entity_texture_glowmask.png`

**Creating the Glow Mask:**

Open the `_glowmask` file and remove all pixels that should *not* emit light. This leaves only the pixels intended to glow, creating an almost empty texture with isolated bright areas representing the emissive portions of your model.

---

**Documentation Version:** GeckoLib 4
**Last Updated:** April 22, 2024
