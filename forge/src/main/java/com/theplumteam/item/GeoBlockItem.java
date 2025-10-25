package com.theplumteam.item;

import com.theplumteam.block.BoxBlock;
import com.theplumteam.client.renderer.BoxBlockItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class GeoBlockItem extends BlockItem {
    private BlockEntityWithoutLevelRenderer renderer;

    public GeoBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    public BoxBlock getBoxBlock() {
        return (BoxBlock) getBlock();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = new BoxBlockItemRenderer();
                }
                return renderer;
            }
        });
    }
}
