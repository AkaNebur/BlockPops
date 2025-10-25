# Stateless Animations (Geckolib4) Documentation

## Overview

Stateless Animations represent an alternative animation handling method in GeckoLib4, distinct from the AnimationController approach. They provide flexibility for developers who prefer decentralized animation management rather than using a centralized controller.

## Key Characteristics

According to the documentation, stateless animations are "a less efficient and less responsive method of handling animations" compared to controller-based approaches. However, they remain a viable option for those with specific implementation preferences.

**Important Note:** The documentation indicates these features "are still somewhat experimental and may require updates," with developers encouraged to provide feedback through the project's Discord community.

## Implementation Guide

### Step 1: Choose the Appropriate Interface

Instead of standard animatable interfaces, implement their stateless equivalents:

| Standard Class | Stateless Equivalent |
|---|---|
| GeoBlockEntity | StatelessGeoBlockEntity |
| GeoEntity | StatelessGeoEntity |
| GeoItem | StatelessGeoSingletonAnimatable |
| GeoReplacedEntity | StatelessGeoReplacedEntity |
| SingletonGeoAnimatable | StatelessGeoSingletonAnimatable |

### Step 2: Basic Implementation Example

```java
public class MyEntity extends PathfinderMob implements StatelessGeoEntity {
    //...
}
```

### Step 3: Animation Control

Use `StatelessAnimatable` methods to control animations at runtime:

- **`playAnimation()`** - Initiates animation playback until completion (or indefinitely if looping)
- **`stopAnimation()`** - Terminates ongoing animations

## Practical Usage Example

The documentation provides this implementation pattern:

```java
public class MyEntity extends PathfinderMob implements StatelessGeoEntity {
    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()) {
            playAnimation(DefaultAnimations.LIVING);

            if (getDeltaMovement().horizontalDistanceSqr() > 0.01) {
                playAnimation(DefaultAnimations.WALK);
                stopAnimation(DefaultAnimations.IDLE);
            }
            else {
                playAnimation(DefaultAnimations.IDLE);
                stopAnimation(DefaultAnimations.WALK);
            }
        }
    }
}
```

## Critical Considerations

Developers must manually manage animation conflicts, ensuring incompatible animations don't run simultaneously. Each animation operates independently, requiring explicit management through play/stop calls.
