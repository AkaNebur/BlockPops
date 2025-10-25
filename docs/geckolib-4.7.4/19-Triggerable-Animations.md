# Triggerable Animations (Geckolib4) - Documentation

## Overview

Geckolib4 introduces server-side animation triggering capabilities for animatable objects. This feature enables more sophisticated animation control by allowing animations to be triggered from the server, overriding standard `AnimationState` calls during execution.

## Key Characteristics

**Important Note:** Triggered animations override the `AnimationController`'s `AnimationState` calls while running. The system works best with non-looping animations, though it can be interrupted at any time using the controller's `stop()` method.

## Implementation Steps

### Step 1: Register the Triggerable Animation

During controller instantiation, declare triggerable animations by calling `.triggerableAnim()` on your controller, providing:
- A trigger identifier (string)
- The associated `RawAnimation` object

**Example registration:**
```java
public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    controllers.add(new AnimationController<>(this, "shoot_controller", state -> PlayState.STOP)
        .triggerableAnim("shoot", SHOOT_ANIM));
}
```

### Step 2: Trigger Animation from Server

Call `triggerAnim()` server-side, specifying:
- Controller name
- Trigger identifier

**Example usage:**
```java
public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    if (level instanceof ServerLevel serverLevel)
        triggerAnim(player, GeoItem.getOrAssignId(player.getItemInHand(hand), serverLevel),
                    "shoot_controller", "shoot");
    return super.use(level, player, hand);
}
```

### Step 3: Register Singleton Animatables

For singleton animatable types (items and replaced entities), register with GeckoLib in the class constructor:

```java
public ExampleItem(Properties properties) {
    super(properties);
    SingletonGeoAnimatable.registerSyncedAnimatable(this);
}
```

## Related Resources

For comprehensive animation controller documentation, consult the dedicated [Animation Controller wiki page](https://github.com/bernie-g/geckolib/wiki/The-Animation-Controller-\(Geckolib4\)#triggered-animations).
