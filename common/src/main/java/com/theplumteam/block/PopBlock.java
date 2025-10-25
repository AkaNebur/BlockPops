package com.theplumteam.block;

import com.theplumteam.blockentity.PopBlockEntity;
import com.theplumteam.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PopBlock extends BaseEntityBlock {
    private final PopBlockColor color;

    public PopBlock(Properties properties, PopBlockColor color) {
        super(properties);
        this.color = color;
    }

    public PopBlockColor getColor() {
        return color;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PopBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? createTickerHelper(blockEntityType, ModBlockEntities.POP_BLOCK.get(), PopBlockEntity::tick) : null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
