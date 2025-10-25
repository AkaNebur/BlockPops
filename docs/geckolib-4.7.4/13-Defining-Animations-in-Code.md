# Defining Animations in Code (Geckolib4) - Complete Documentation

## Overview

This guide covers the nuances of animation implementation in GeckoLib4, addressing edge cases and best practices for animation controllers and model animation.

## Animation Fundamentals

### GeoAnimatable Interface

All animatable objects in GeckoLib must implement the `GeoAnimatable` interface. It's recommended to use the most specific implementation for your object type—such as `GeoItem` for items or `GeoBlockEntity` for block entities.

Required method overrides:
1. `getAnimatableInstanceCache`
2. `registerControllers`

### AnimatableInstanceCache

Instance caches store and provide animatable instances, allowing differentiation between object instances so each animates independently. Different animatable types use different cache implementations (e.g., item caches contain instances for every unique ItemStack).

**Important:** Always instantiate factories using `GeckolibUtil.createInstanceCache` rather than cache constructors. This allows GeckoLib to select the appropriate cache type automatically.

### AnimationController

An AnimationController manages a single concurrent animation. Key characteristics:

- **Single Animation at a Time:** Running a new animation cancels the previous one
- **Multiple Concurrent Animations:** Require registering multiple controllers, one per animation
- **Bone Conflict Prevention:** Animations operate on the same model, so avoid having multiple animations control identical bones

Controllers execute in registration order, with later registrations having priority for bone positioning during render frames.

#### Controller States

| State | Description |
|-------|-------------|
| **Running** | Controller actively plays an animation |
| **Transitioning** | Controller transitions between stopped/running or between animations |
| **Stopped** | Controller not running; either static or lerping to original model state |

Check controller state via `AnimationController.getAnimationState`.

#### Animation Manipulation

Control animations through `AnimationController.setAnimation`, which caches the last provided builder. Calling this method repeatedly—even with identical animations—incurs no performance penalty.

**Non-Looping Animations:** These play only once. Replay requires calling `AnimationController.forceAnimationReset` to force the controller to load the next call as a new animation. This step is unnecessary if another animation has been run since the previous non-looping animation.

#### AnimationState

Controllers require an `AnimationState` function implementation, executed each render pass to set animations. This state instance is created fresh per frame.

Return values determine controller behavior:
- **PlayState.CONTINUE:** Start or continue the set animation
- **PlayState.STOP:** Immediately halt animation

#### Animation/Bone Transitions

By default, GeckoLib applies linear transitions between bone positions—whether between animation frames or animations. The `transitionLength` value in `AnimationController` controls this behavior (set during instantiation, but accessible for dynamic changes).

Setting transition length to `0` disables transitions, snapping to the next position immediately.

### Easings

Bone position transitions default to linear easing but can be configured for other effects like stepped or elastic transitions.

**Configuration Methods:**
- Default: Defined in animation JSON
- Override: Use `AnimationController.setOverrideEasingType` with an `EasingType` instance
- Reset: Set override to `null` to revert to JSON definitions

#### Custom Easing

Create custom easing by:

1. Implement a function taking a double (0-1, representing keyframe progress) and returning an eased double
2. Register via `GeckolibUtil#addCustomEasingType` at mod startup
3. Reference the easing guide at [easings.net](https://easings.net) for conceptual understanding

---

**Note:** This documentation is part of the GeckoLib animation library wiki and is current as of February 1, 2025.
