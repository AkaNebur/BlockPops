# GeckoLib Examples (Geckolib 4) - Documentation

## Overview

GeckoLib provides multiple example implementations demonstrating its animation and rendering capabilities across different Minecraft versions.

## Version-Specific Implementation

### GeckoLib 4.0-4.4 (Minecraft 1.19.3 → 1.20.4)

Example implementations are integrated into the base library and available during development environments.

**Disabling Examples:**
To remove built-in example content during development, add this to your `build.gradle` configuration:
```
property 'geckolib.disable_examples', 'true'
```

### GeckoLib 4.5+ (Minecraft 1.20.5+)

Example code was extracted into a [separate repository](https://github.com/bernie-g/geckolib-examples) with branches for each loader type and multiloader support. These implementations emphasize minimal scaffolding for clarity.

## Example Implementations

### Entities (8 examples)

- **Bat**: Demonstrates Molang animations, worldspace bone positioning, and head rotation mechanics
- **Bike**: Showcases advanced Molang query functionality
- **CoolKid**: Illustrates GeoLayer integration with Geckolib models
- **DynamicExample**: Features advanced capabilities via DynamicGeoEntityRenderer
- **FakeGlass**: Demonstrates per-bone texture handling through DynamicGeoEntityRenderer
- **Parasite**: Combines multiple animation controllers for walking and attack behaviors
- **RaceCart**: Exhibits animated texture implementation
- **ReplacedCreeper**: Shows model replacement of vanilla entities using GeoReplacedEntityRenderer

### Blocks

Two block examples showcase different animation patterns:
- **GeckoHabitat**: Single static animation implementation
- **Fertilizer**: Dynamic switching of models, textures, and animations based on environmental state (rain)

### Items

**JackInTheBox**: Demonstrates animated item functionality with audio and animation triggering on right-click interaction.

### Armor

Both implementations play idle animations exclusively when all four armor pieces are equipped:
- GeckoArmorItem
- WolfArmorItem

---

*Documentation sourced from the GeckoLib Wiki on GitHub*
