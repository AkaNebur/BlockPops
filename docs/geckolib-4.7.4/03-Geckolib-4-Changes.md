# GeckoLib 4 Changes - Complete Documentation

## Upgrading from GeckoLib 3.1.x to 4.0

GeckoLib 4.0 represents a substantial overhaul requiring modifications to existing mods for compatibility.

---

## Change Summary

### Class Renames

The following classes received new names:

| Old Name | New Name |
|----------|----------|
| `AnimationData` | `AnimatableManager` |
| `AnimationFactory` | `AnimatableInstanceCache` |
| `IGeoRenderer` | `GeoRenderer` |
| `GeoLayerRenderer` | `GeoRenderLayer` |
| `ExtendedGeoEntityRenderer` | `DynamicGeoEntityRenderer` |
| `LayerGlowingAreasGeo` | `AutoGlowingGeoLayer` |
| `AnimatedGeoModel` | `GeoModel` |
| `GeckoLibIdTracker` | `AnimatableIdCache` |
| `AnimationBuilder` | `RawAnimation` |
| `IAnimatable` | `GeoAnimatable` |
| `IBone` | `CoreGeoBone` |
| `ILoopType` | `Animation$LoopType` |
| `AnimationEvent` | `AnimationState` |

### Class Merges/Removals

Several classes were consolidated or removed:

- `AbstractLayerGeo` merged into `GeoRenderLayer`
- `IAnimatableModelProvider` removed; functionality moved to renderers
- `GeoModelProvider` removed; functionality moved to renderers
- `AnimatedTickingGeoModel` removed; functionality moved to renderers
- `EasingManager` merged into `EasingType`
- `EasingFunctionArgs` merged into `EasingType`
- `IAnimationTickable` removed; functionality moved to renderers
- `IAnimatableModel` merged into `GeoModel`
- `EDefaultLoopTypes` merged into `Animation$LoopType`
- `GeoProjectilesRenderer` merged into `GeoEntityRenderer`

### Conceptual Changes

Key architectural shifts include:

- Animatable classes now specify their subtype via interface
- GeoModels define their own default render type
- Multiple model classes consolidated into one
- Native data syncing functionality added
- Server-triggerable animations introduced
- Expanded render layer functionality across all geo renderers
- Defaulted model classes now available
- Animation controller/manager extra data now uses generics
- Glowlayers support pre-made mask images
- LoopTypes support JSON-defined configurations
- Custom LoopTypes and EasingTypes supported natively
- AnimationFactory split into two classes
- Bedrock Animation Format support added with catmullRom/smooth easing
- AnimationControllers now registered to registrar with reliable ordering

---

## Creating Your Animatable Class

GeckoLib 4 requires implementing specific interfaces based on animatable type:

| Type | Interface |
|------|-----------|
| Entity | `GeoEntity` |
| Projectile | `GeoEntity` |
| BlockEntity | `GeoBlockEntity` |
| Item | `GeoItem` |
| Armor | `GeoItem` |
| Existing Entity | `GeoReplacedEntity` |

**Example:**
```java
public class ExampleEntity extends PathfinderMob implements GeoEntity
```

---

## Creating Your AnimatableInstanceCache

Replace the constructor-based instantiation with the factory method:

```java
private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
```

This allows GeckoLib to create an appropriately typed cache for your animatable.

---

## Creating Your RawAnimation

Animations are now created using a builder pattern:

```java
private static final RawAnimation SPIN_ANIM = RawAnimation.begin().thenPlay("misc.spin");
```

### Default Loop Type

"By default; animations will now use the loop type defined in the animation json," allowing resourcepack customization. Cache animations in static fields to avoid recreation each frame.

---

## Making a GeoArmor

### Forge Implementation

```java
public void initializeClient(Consumer<IClientItemExtensions> consumer) {
    consumer.accept(new IClientItemExtensions() {
        private GeoArmorRenderer<?> renderer;

        @Override
        public @NotNull HumanoidModel<?> getHumanoidArmorModel(
            LivingEntity livingEntity, ItemStack itemStack,
            EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
            if (this.renderer == null)
                this.renderer = new MyArmorRenderer();

            this.renderer.prepForRender(livingEntity, itemStack,
                equipmentSlot, original);

            return this.renderer;
        }
    });
}
```

### Fabric/Quilt Implementation

Documentation marked as "TBA" (To Be Announced).

---

## Other Notable Changes

### Custom RenderType for Animatables

GeoModels now define their default `RenderType`, eliminating the need to extend renderers for render type changes.

### Custom LoopTypes

Register custom loop types during mod construction:

```java
GeckoLibUtil.addCustomLoopType(String, LoopType);
```

Registration must occur during mod constructor before JSON deserialization.

### Custom EasingTypes

Register custom easing types similarly:

```java
GeckoLibUtil.addCustomEasingType(String, EasingType);
```

Must be registered during mod constructor.

### Render Layers

Render layers now provide "much more information" at render time with additional hooks for:

- Pre-render operations
- Mid-bone rendering operations

Several pre-built layers are bundled, including:
- Bone visibility filters
- Item/block rendering alongside models

### DynamicGeoEntityRenderer

Renamed from `ExtendedGeoEntityRenderer`, this class now primarily handles per-bone render overrides. Other advanced functionality split into various `GeoRenderLayers`.

### Defaulted GeoModels

Pre-built model subclasses encourage consistent asset path handling:

```java
public class MyEntityModel extends DefaultedEntityGeoModel<MyEntity> {
    public MyEntityModel() {
        super(new ResourceLocation(MyMod.MOD_ID, "monster/my_entity"));
    }
}
```

Asset structure generated:
- Animation JSON: `assets/mymod/animations/entity/monster/my_entity.animation.json`
- Model JSON: `assets/mymod/geo/entity/monster/my_entity.geo.json`
- Texture: `assets/mymod/textures/entity/monster/my_entity.png`

`DefaultedEntityGeoModel` can optionally auto-handle head rotation via constructor boolean.

### Default Animations

Pre-built animations available in `DefaultAnimations` class for common behaviors, encouraging naming consistency and reducing boilerplate code.

### DataTickets

Generified objects serve as keys for transient data, replacing previous filter-based retrieval:

```java
Entity entity = state.getData(DataTickets.ENTITY);
```

Custom tickets can be created without registration.

### Native Data Syncing

New `SerializableDataTickets` enable efficient client-server data transfer:

```java
setAnimData(DataTickets.ACTIVE, true);
```

For singleton animatables, register during construction:

```java
public ExampleItem(Properties properties) {
    super(properties);
    SingletonGeoAnimatable.registerSyncedAnimatable(this);
}
```

### Server-Triggerable Animations

Server-side animation triggering now supported. Define triggerable animations:

```java
public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    controllers.add(new AnimationController<>(this, "shoot_controller",
        state -> PlayState.STOP)
        .triggerableAnim("shoot", SHOOT_ANIM));
}
```

Trigger from server:

```java
triggerAnim(player, GeoItem.getOrAssignId(player.getItemInHand(hand),
    serverLevel), "shoot_controller", "shoot");
```

For singletons, register during construction as noted above.

---

## Examples

### Example Entity

```java
public class ExampleEntity extends PathfinderMob implements GeoEntity {
    private final AnimatableInstanceCache cache =
        GeckoLibUtil.createInstanceCache(this);

    public ExampleEntity(EntityType<? extends ExampleEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", 5,
            state -> state.setAndContinue(DefaultAnimations.IDLE)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
```

### Example Item

```java
public final class ExampleItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache =
        GeckoLibUtil.createInstanceCache(this);

    public ExampleItem(Properties properties) {
        super(properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private ExampleItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new ExampleItemRenderer();
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "shoot_controller",
            state -> PlayState.STOP)
            .triggerableAnim("shoot", DefaultAnimations.ATTACK_SHOOT));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player,
        InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel)
            triggerAnim(player, GeoItem.getOrAssignId(
                player.getItemInHand(hand), serverLevel),
                "shoot_controller", "shoot");
        return super.use(level, player, hand);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
```

### Example Armor

```java
public final class ExampleArmor extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache =
        GeckoLibUtil.createInstanceCache(this);

    public ExampleArmor(ArmorMaterial armorMaterial, EquipmentSlot slot,
        Properties properties) {
        super(armorMaterial, slot, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private ExampleArmorRenderer<?> renderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(
                LivingEntity livingEntity, ItemStack itemStack,
                EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null)
                    this.renderer = new ExampleArmorRenderer();
                this.renderer.prepForRender(livingEntity, itemStack,
                    equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(DefaultAnimations.genericIdleController(this));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
```

### Example BlockEntity

```java
public class ExampleBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache =
        GeckoLibUtil.createInstanceCache(this);

    public ExampleBlockEntity(BlockPos pos, BlockState state) {
        super(TileRegistry.EXAMPLE_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, state -> {
            if (getLevel().getDayTime() > 23000 ||
                getLevel().getDayTime() < 13000) {
                return state.setAndContinue(DefaultAnimations.REST);
            } else {
                return state.setAndContinue(DefaultAnimations.IDLE);
            }
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
```

---

## Summary

GeckoLib 4 modernizes the animation framework through architectural improvements, expanded functionality, and enhanced developer ergonomics. The changes promote consistency while offering greater flexibility for advanced use cases.
