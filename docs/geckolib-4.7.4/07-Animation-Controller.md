# The Animation Controller (Geckolib4) - Complete Documentation

## Overview

The `AnimationController` manages animations for GeckoLib animatables, serving as the primary entry point for animation management through its `AnimationStateHandler`. Each controller handles one animation at a time, but multiple controllers can run simultaneously on a single animatable.

## How It Works

Animatables require a `registerControllers` method to add animation controllers:

```java
public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    // Register your controllers here
}
```

Controllers are optional if your animatable has no animations.

Example implementation:

```java
controllers.add(new AnimationController<>(this, "Walking", 0, state -> {
    return state.isMoving() ? state.setAndContinue(DefaultAnimations.WALK) : PlayState.STOP;
}));
```

## Multiple Controllers

Controller registration order matters significantly. GeckoLib executes controllers in registration sequence, with later controllers overriding earlier ones. Best practice involves registering broad animations (walk, idle) first, then specific ones (attacks) afterward. This allows animations to blend—for instance, attack motions can play while walking continues.

## Controller Settings

### Constructor Parameters

- **animatable**: The instance being animated
- **name**: Unique identifier for the controller (e.g., "Walking")
- **transitionTickTime**: Game ticks for animation interpolation (default recommended)
- **animationHandler**: Predicate determining what animation plays and when

### Factory Methods

Several configuration methods enhance controller behavior:

- `setSoundKeyframeHandler()` - Manages sound effects during animations
- `setParticleKeyframeHandler()` - Handles particle effects
- `setCustomInstructionKeyframeHandler()` - Enables custom keyframe logic
- `setAnimationSpeed()` - Modifies animation playback speed
- `setAnimationSpeedHandler()` - Dynamic speed adjustment
- `setOverrideEasingType()` - Forces easing curve behavior
- `setOverrideEasingTypeFunction()` - Customizable easing
- `receiveTriggeredAnimations()` - Enables triggered animation handling

## Triggered Animations

Controllers support triggered animations that bypass normal state checks during playback. Once triggered animations finish, the controller resumes checking animation state normally.

Using `.receiveTriggeredAnimations()` allows controllers to manage both triggered and state-based animations simultaneously, though the predicate must account for potential triggered animation playback.

## The AnimationStateHandler

The handler is a predicate receiving an `AnimationState` and returning a `PlayState`. It executes every render frame, representing the controller's current state rather than triggering state changes.

**Important**: This method runs at maximum frequency—treat it as a real-time state check, not an event trigger.

## Practical Examples

### Basic Walking

```java
controllers.add(new AnimationController<>(this, "Walking", state -> {
    return state.isMoving() ? state.setAndContinue(DefaultAnimations.WALK) : PlayState.STOP;
}));
```

### Walking, Running, and Idling

```java
controllers.add(new AnimationController<>(this, "Walk/Run/Idle", state -> {
    if (state.isMoving())
        return state.setAndContinue(MyEntity.this.isSprinting() ? DefaultAnimations.RUN : DefaultAnimations.WALK);
    return state.setAndContinue(DefaultAnimations.IDLE);
}));
```

### Time-Based Animation (Spawning)

```java
controllers.add(new AnimationController<>(this, "Spawning", state -> {
    if (MyEntity.this.tickCount < 100)
        return state.setAndContinue(MY_SPAWN_ANIMATION);
    return PlayState.STOP;
}));
```

### Attack Animation with Reset

```java
controllers.add(new AnimationController<>(this, "Attack", state -> {
    if (MyEntity.this.swinging)
        return state.setAndContinue(DefaultAnimations.ATTACK_SWING);
    state.resetCurrentAnimation();
    return PlayState.STOP;
}));
```

## DefaultAnimations

GeckoLib provides the `DefaultAnimations` class containing built-in controllers and generic animations. Using these maintains consistency and requires adherence to standard animation naming conventions—for example, `genericWalkIdleController` expects animations named "move.walk" and "misc.idle".
