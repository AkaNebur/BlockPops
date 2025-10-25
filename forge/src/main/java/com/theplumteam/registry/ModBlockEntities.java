package com.theplumteam.registry;

import com.theplumteam.BlockPopsMod;
import com.theplumteam.blockentity.BoxBlockEntity;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(BlockPopsMod.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<BoxBlockEntity>> BOX_BLOCK =
        BLOCK_ENTITIES.register("box_block", () -> {
            // Extract blocks array inside the lambda, after blocks are registered
            Block[] blocks = ModBlocks.BOX_BLOCKS.values().stream()
                .map(RegistrySupplier::get)
                .toArray(Block[]::new);
            return BlockEntityType.Builder.of(
                BoxBlockEntity::new,
                blocks
            ).build(null);
        });

    public static void register() {
        BLOCK_ENTITIES.register();
    }
}
