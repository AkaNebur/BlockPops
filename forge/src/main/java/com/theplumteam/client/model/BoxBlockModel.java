package com.theplumteam.client.model;

import com.theplumteam.BlockPopsMod;
import com.theplumteam.blockentity.BoxBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BoxBlockModel extends GeoModel<BoxBlockEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(BlockPopsMod.MOD_ID, "geo/block/box_block.geo.json");
    private static final ResourceLocation ANIMATION = new ResourceLocation(BlockPopsMod.MOD_ID, "animations/block/box_block.animation.json");

    @Override
    public ResourceLocation getModelResource(BoxBlockEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(BoxBlockEntity animatable) {
        String textureName = animatable.getColor().getTextureName();
        return new ResourceLocation(BlockPopsMod.MOD_ID, "textures/block/box/" + textureName + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(BoxBlockEntity animatable) {
        return ANIMATION;
    }
}
