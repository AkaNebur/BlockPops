package com.theplumteam.blockentity;

import com.theplumteam.block.PopBlock;
import com.theplumteam.block.PopBlockColor;
import com.theplumteam.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PopBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // Define the pop animation
    private static final RawAnimation POP_ANIMATION = RawAnimation.begin().thenLoop("animation.pop_block.idle");

    public PopBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.POP_BLOCK.get(), pos, blockState);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> {
            state.getController().setAnimation(POP_ANIMATION);
            return state.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public PopBlockColor getColor() {
        if (getBlockState().getBlock() instanceof PopBlock popBlock) {
            return popBlock.getColor();
        }
        return PopBlockColor.ORIGINAL;
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        // Client-side tick for animations
        if (level.isClientSide && blockEntity instanceof PopBlockEntity popBlockEntity) {
            // Animation ticking is handled automatically by GeckoLib
        }
    }
}
