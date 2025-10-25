# Animated Textures (Geckolib4) - Documentation

## Overview

GeckoLib4 introduced support for animating textures used by GeoAnimatable objects, applicable across all GeckoLib object types including entities, blocks, and items.

**Important Limitation:** "GeckoLib animated textures are currently not compatible with GeckoLib emissive textures."

## Implementation Process

GeckoLib handles animated texture processing automatically when properly configured. The setup mirrors vanilla Minecraft's animated block and item texture approach.

### Setup Steps

1. **Create metadata file**: In your texture directory, create a text file named `[texturename].png.mcmeta`
   - Example: For `zombie.png`, create `zombie.png.mcmeta`

2. **Add animation configuration**:
   ```json
   {
     "animation": {

     }
   }
   ```

3. **Design texture frames**: Create your PNG with animation frames following vanilla texture standards (reference fire or water textures as examples)

4. **Apply texture**: Use the animated texture in your GeoAnimatable object

GeckoLib supports all standard `.mcmeta` properties for texture animations.

## Troubleshooting

**Issue: Texture appears stretched without animation**
- Cause: Missing, misnamed, or misplaced `.png.mcmeta` file

**Issue: Animated texture misaligns with model**
- Cause: "Animated textures must have each frame be square in size"
- Solution: If base texture is 64x32 pixels, expand to 64x64 per frame

**Issue: Glowing animated textures appear incorrect**
- Cause: "GeckoLib does not support animating textures that also utilise a Glowmask"
