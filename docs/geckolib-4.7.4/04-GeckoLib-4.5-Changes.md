# GeckoLib 4.5 Changes (1.20.6) - Documentation

## Overview

GeckoLib 4.5 represents a moderately sized update emphasizing transition to Multiloader architecture while optimizing runtime efficiency and streamlining the developer API. This version targets Minecraft 1.20.5+, with some backports planned for 1.20.1.

## Key Changes Summary

### Multiloader Architecture

The primary objective involved migrating the entire library to Multiloader. This modernization enables developers to accomplish most tasks within the common module without requiring platform-specific handling. The migration encompassed event systems and networking infrastructure across all platforms.

### Enhanced Error Logging

GeckoLib 4.5 introduces comprehensive error reporting for resource loading issues. When problems occur, the system identifies the specific problematic file and provides detailed debugging information, including animation location, line number, and cause of failure.

### GeckoLib Examples Repository

Examples previously bundled within GeckoLib have been extracted to a dedicated repository. This separation addresses several concerns:

- Avoids testing complications with Maven and Gradle issues
- Eliminates unnecessary asset bloat from main artifacts
- Removes forced example injection into development environments
- Streamlines GeckoLib's codebase

The new examples repository includes platform-specific branches and multiloader deployments.

### Mathematical Expression Parsing Overhaul

The library underwent complete rewriting of mathematical parsing systems, replacing the previous McLib fork dependency with an optimized custom solution. Notable improvements include:

- Single-pass computation instead of previous 10+ passes
- Enhanced Molang extensibility and developer accessibility
- **Class renaming:** "MathBuilder" became "MathParser"; "IValue" functionally became "MathValue"
- Function memoization except for non-memoizable values (Molang queries)

### RenderProvider Improvements

The rendering system received substantial enhancements:

- **Full multiloader support** with native Sinytra Connector compatibility
- **Proper typing:** `createGeoRenderer` eliminates generic `Consumer<Object>`
- **Simplified API:** Eliminates need for `makeRenderer` and `getRenderProvider` methods
- **Interface clarification:** Multiple method and class renamings for enhanced clarity
- **Automatic handling:** `prepForRender` call no longer required on `GeoArmorRenderer`

#### Naming Changes:
- `RenderProvider` → `GeoRenderProvider`
- `createRenderer` → `createGeoRenderer`
- `RenderProvider#getRenderer` → `GeoRenderProvider#getGeoItemRenderer`
- `RenderProvider#getHumanoidArmorModel` → `GeoRenderProvider#getGeoArmorRenderer`

## Migration Notes

Developers should anticipate minimal disruption beyond method and class renamings. The update maintains backward compatibility considerations where feasible, though specific mod contents may require adjustments.
