# Split Sources Support (Geckolib4) - Documentation

## Overview

GeckoLib is designed to encapsulate all code in one location by default. However, split-source environments—where client code is isolated from common code—require special handling. This affects Items and Armor primarily, as Entities and BlockEntities already register renderers in client-only code.

## Implementation Steps

To use the GeoRenderProvider system with split-sources:

1. Create your GeoItem class following standard procedures
2. Add a `public final` instance of `MutableObject<GeoRenderProvider>` as a final field
3. In the `createGeoRenderer` method, pass `getValue()` from the MutableObject to the consumer
4. In client setup, call `setValue()` on your registered item's cached `MutableObject`, passing your GeoRenderer instance

## Example Implementation

### Item Class

```java
public class ExampleItem extends Item implements GeoItem {
    public final MutableObject<GeoRenderProvider> renderProviderHolder =
        new MutableObject<>();
    private final AnimatableInstanceCache geoCache =
        GeckoLibUtil.createInstanceCache(this);

    public ExampleItem(Settings settings) {
        super(settings);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(this.renderProviderHolder.getValue());
    }

    @Override
    public void registerControllers(
        AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
```

### Client Setup

```java
@Override
public void onInitializeClient() {
    ExampleModItems.EXAMPLE_ITEM.renderProviderHolder.setValue(
        new GeoRenderProvider() {
            private ExampleItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new ExampleItemRenderer();
                return this.renderer;
            }
    });
}
```

This approach isolates client rendering code while maintaining clean architecture across sourceset boundaries.
