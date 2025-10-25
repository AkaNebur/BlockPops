# Render Layers (Geckolib4) - Documentation

## Overview

GeckoLib enables additional rendering layers beyond standard rendering through `GeoRenderLayer` instances, allowing developers to add extra visual elements to their animated models.

## Usage

Render layers are added during renderer instantiation using the `addRenderLayer()` method:

```java
public class ExampleRenderer extends GeoEntityRenderer<ExampleEntity> {
	public ExampleRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new ExampleEntityModel());
		addRenderLayer(new ExampleGeoRenderLayer(this));
	}
}
```

## Built-In Render Layers

### AutoGlowingGeoLayer

Provides "easy-to-use emissive rendering" for all GeckoLib Animatables. Requires minimal setup—simply add it to your renderer with a corresponding glowmask texture.

### BlockAndItemGeoLayer

Renders ItemStack or Block objects on animated models (such as held items). Key methods to override:

- **`getStackForBone`**: Returns ItemStack for specific bones
- **`getBlockForBone`**: Returns BlockState for specific bones
- **`getTransformTypeForStack`**: Alters render perspective for ItemStack
- **`renderStackForBone`**: Manually manipulates PoseStack for ItemStack rendering
- **`renderBlockForBone`**: Manually manipulates PoseStack for BlockState rendering

### FastBoneFilterGeoLayer

Manipulates specific bones at render time. Commonly used for conditional bone visibility. Important: "reset the bone in the event your condition does not apply."

### ItemArmorGeoLayer

Enables worn-item rendering on entities. Key overridable methods:

- **`getEquipmentSlotForBone`**: Maps bones to equipment slots
- **`getModelPartForBone`**: Maps bones to HumanoidModel parts
- **`getArmorItemForBone`**: Returns relevant ItemStack for bones

**Note**: This layer relies on the first cube in GeoBone to determine armor positioning. Don't return ItemStacks for boneless cubes.
