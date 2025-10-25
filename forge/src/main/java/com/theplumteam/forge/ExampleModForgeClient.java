package com.theplumteam.forge;

import com.theplumteam.client.renderer.PopBlockRenderer;
import com.theplumteam.registry.ModBlockEntities;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = "blockpops")
public class ExampleModForgeClient {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.POP_BLOCK.get(), context -> new PopBlockRenderer());
    }
}
