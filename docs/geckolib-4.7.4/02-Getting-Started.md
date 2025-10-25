# GeckoLib Getting Started Documentation

## Overview

GeckoLib is a Minecraft modding library for creating animated models and entities. The Getting Started wiki page provides foundational guidance for new users.

## Prerequisites

The documentation assumes users possess:

1. **Java Fundamentals** - Basic programming concepts and syntax understanding
2. **Minecraft Knowledge** - General game mechanics and concepts
3. **Modding Experience** - Prior work with either Forge or Fabric modding frameworks

The wiki notes that users unfamiliar with these areas should "pick them up through some modding tutorials first" before proceeding.

## Key Development Path

### Model Creation
Users begin by creating models and animations using Blockbench, a dedicated modeling tool. The wiki dedicates "instructions/info on using Blockbench for Geckolib" to this phase.

### Code Implementation
After completing models, developers create a Geo Model class representing their assets. The documentation provides version-specific guidance:

- **GeckoLib 5** (MC 1.21.5+)
- **GeckoLib 4** (MC 1.19.3+)
- **GeckoLib 3** (MC 1.19.2 and earlier)

## Technical Notes

The library uses Mojmap with Parchment mappings. Users with Yarn-based projects may encounter unfamiliar class names (like `ResourceLocation` instead of `Identifier`) and should either convert their project or use Linkie for name translation.

## Community Recognition

The documentation provides a badge image for mod creators to display on CurseForge and Modrinth pages, indicating GeckoLib as a dependency.
