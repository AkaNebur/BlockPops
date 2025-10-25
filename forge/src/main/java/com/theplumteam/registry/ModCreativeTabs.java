package com.theplumteam.registry;

import com.theplumteam.BlockPopsMod;
import com.theplumteam.block.PopBlockColor;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
        DeferredRegister.create(BlockPopsMod.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> BLOCKPOPS_TAB = CREATIVE_TABS.register(
        "blockpops_tab",
        () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("itemGroup.blockpops.blockpops_tab"))
            .icon(() -> new ItemStack(ModItems.BOX_BLOCK_ITEMS.get(PopBlockColor.ORIGINAL).get()))
            .displayItems((parameters, output) -> {
                // Add all box blocks to the creative tab
                for (PopBlockColor color : PopBlockColor.values()) {
                    output.accept(ModItems.BOX_BLOCK_ITEMS.get(color).get());
                }
            })
            .build()
    );

    public static void register() {
        CREATIVE_TABS.register();
    }
}
