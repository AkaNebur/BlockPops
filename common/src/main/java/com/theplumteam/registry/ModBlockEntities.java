package com.theplumteam.registry;

import com.theplumteam.ExampleMod;
import com.theplumteam.blockentity.PopBlockEntity;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ExampleMod.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<PopBlockEntity>> POP_BLOCK =
        BLOCK_ENTITIES.register("pop_block", () ->
            BlockEntityType.Builder.of(
                PopBlockEntity::new,
                ModBlocks.POP_BLOCKS.values().stream()
                    .map(RegistrySupplier::get)
                    .toArray(Block[]::new)
            ).build(null)
        );

    public static void register() {
        BLOCK_ENTITIES.register();
    }
}
