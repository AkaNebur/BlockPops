# Making Your Models (Blockbench) - GeckoLib Wiki Documentation

## Overview

This guide covers creating and animating 3D models for GeckoLib using Blockbench, a popular modeling and animation tool.

## Installing the Plugin

To work with GeckoLib models, you must first install the GeckoLib plugin:

1. Open Blockbench and navigate to `File` → `Plugins...`
2. Search for "GeckoLib Animation Utils" in the Available tab
3. Click the Install button

## Creating and Converting Models

**New Models:** Start by selecting `File` → `New` → `GeckoLib Animated Model`, then choose your model type via `File` → `Project`.

**Existing Models:** If you already have a bedrock or modded entity model, convert it by going to `File` → `Convert Project` → `Geckolib Animated Model`. The documentation emphasizes: "make sure you save your .bbmodel project files so you always have them!"

## Rigging Your Model

Rigging involves preparing a model for animation by creating a skeleton structure.

### Grouping

Models consist of cubes and groups. Only groups can be animated, so organize all cubes within groups before animating.

### Parenting and Pivots

The rig functions as a skeleton where groups represent bones, pivots serve as joints, and cubes form the flesh. Most models benefit from a single root group containing nested child groups. When a group moves, it carries its children along.

Pivot points determine where groups rotate. Setting proper pivot locations creates natural-looking animations.

## Animation Features

### Molang Support

GeckoLib supports Molang, an "expression-based math system for advanced animations, such as sine-functions."

### Easing Curves

GeckoLib 2.0 introduced easing curves, enabling smooth animations without relying solely on linear interpolation. These mathematical functions create effects like smooth acceleration/deceleration, overshooting, and bouncing.

Easing directions include:
- **In:** Smooth-looking start
- **Out:** Smooth-looking end
- **InOut:** Symmetrical application to both start and end

## Exporting Your Work

After completing your model, export three components:

1. The model file (`File` → `Export` → `GeckoLib Model`)
2. The texture (right-click texture in Textures panel → `Save As`)
3. Display settings for items, if applicable (`File` → `Export` → `GeckoLib Display Settings`)

## Troubleshooting

**Missing Project Type:** If you cannot find the GeckoLib Animated Model option, the plugin may not be installed or failed to load. Install the plugin and restart Blockbench.
