package com.theplumteam.blockentity;

import com.theplumteam.block.BoxBlock;
import com.theplumteam.block.PopBlockColor;
import com.theplumteam.figure.FigureType;
import com.theplumteam.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BoxBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation BOX_ANIMATION = RawAnimation.begin().thenLoop("animation.box_block.idle");
    private static final RawAnimation FIGURE_ANIMATION = RawAnimation.begin().thenLoop("animation.box_figure.idle");
    private FigureType figureType = FigureType.DEFAULT;

    public BoxBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.BOX_BLOCK.get(), pos, blockState);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // Controller for the box model animations
        controllers.add(new AnimationController<>(this, "box_controller", 0, state ->
            state.setAndContinue(BOX_ANIMATION)
        ));

        // Controller for the figure model animations
        controllers.add(new AnimationController<>(this, "figure_controller", 0, state -> {
            if (figureType.hasFigure()) {
                return state.setAndContinue(FIGURE_ANIMATION);
            }
            return state.setAndContinue(RawAnimation.begin());
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public PopBlockColor getColor() {
        if (getBlockState().getBlock() instanceof BoxBlock boxBlock) {
            return boxBlock.getColor();
        }
        return PopBlockColor.ORIGINAL;
    }

    public FigureType getFigureType() {
        return figureType;
    }

    public void setFigureType(FigureType figureType) {
        this.figureType = figureType;
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("FigureType", figureType.getSerializedName());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("FigureType")) {
            this.figureType = FigureType.fromString(tag.getString("FigureType"));
        }
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        if (level.isClientSide && blockEntity instanceof BoxBlockEntity boxBlockEntity) {
            // Animation ticking handled automatically by GeckoLib
        }
    }
}
