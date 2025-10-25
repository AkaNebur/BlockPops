# Keyframe Triggers (Geckolib4) Documentation

## Overview

GeckoLib enables custom animation callbacks through keyframe handlers, allowing developers to trigger effects like particles and sounds at specific animation points.

## Setup in BlockBench

To access keyframe functionality, navigate to `Animation` → `Animate Effects`. This opens a dedicated animation panel where you can add global keyframes to your model.

GeckoLib organizes keyframe triggers into three categories:
- Sounds
- Particles
- Custom Instructions

## Sound Keyframes

Sound keyframe handlers trigger audio at specified animation moments. Implement this by adding a `SoundKeyframeHandler` instance to your `AnimationController` via `setSoundKeyframeHandler`. The handler executes at times marked by animation keyframes.

For practical implementation examples, the Jack in the Box example item demonstrates sound keyframe usage.

### AutoPlayingSoundKeyframeHandler

GeckoLib provides a built-in handler that streamlines basic sound playback without boilerplate code. Simply pass a new `AutoPlayingSoundKeyframeHandler` instance to your animation controller.

In animation JSON files, use either format for automatic sound handling:
- `namespace:soundid`
- `namespace:soundid|volume|pitch`

## Particle Keyframes

Particle keyframe handlers execute callback functions at designated animation points. Add a `ParticleKeyframeHandler` instance to your `AnimationController` via `setParticleKeyframeHandler`.

## Custom Instruction Keyframes

For non-sound and non-particle effects requiring execution at specific animation moments, implement `ICustomInstructionListener`. Add instances to your `AnimationController` using `setCustomInstructionKeyframeHandler`.
