package com.theplumteam.registry;

import com.theplumteam.ExampleMod;
import com.theplumteam.block.PopBlock;
import com.theplumteam.block.PopBlockColor;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.HashMap;
import java.util.Map;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ExampleMod.MOD_ID, Registries.BLOCK);

    // Map to store all pop block variants by color
    public static final Map<PopBlockColor, RegistrySupplier<Block>> POP_BLOCKS = new HashMap<>();

    // Register all 16 color variants
    static {
        for (PopBlockColor color : PopBlockColor.values()) {
            POP_BLOCKS.put(color, BLOCKS.register(
                "pop_block_" + color.getSerializedName(),
                () -> new PopBlock(
                    BlockBehaviour.Properties.of()
                        .mapColor(color.getMapColor())
                        .strength(1.5F, 6.0F)
                        .requiresCorrectToolForDrops()
                        .noOcclusion(),
                    color
                )
            ));
        }
    }

    public static void register() {
        BLOCKS.register();
    }
}
