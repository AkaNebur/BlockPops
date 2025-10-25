package com.theplumteam.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.theplumteam.BlockPopsMod;

@Mod(BlockPopsMod.MOD_ID)
public final class BlockPopsModForge {
    public BlockPopsModForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(BlockPopsMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        BlockPopsMod.init();
    }
}
