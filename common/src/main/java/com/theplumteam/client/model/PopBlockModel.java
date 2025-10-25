package com.theplumteam.client.model;

import com.theplumteam.ExampleMod;
import com.theplumteam.blockentity.PopBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PopBlockModel extends GeoModel<PopBlockEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(ExampleMod.MOD_ID, "geo/block/pop_block.geo.json");
    private static final ResourceLocation ANIMATION = new ResourceLocation(ExampleMod.MOD_ID, "animations/block/pop_block.animation.json");

    @Override
    public ResourceLocation getModelResource(PopBlockEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(PopBlockEntity animatable) {
        // Return texture based on the block's color variant
        String textureName = animatable.getColor().getTextureName();
        return new ResourceLocation(ExampleMod.MOD_ID, "textures/block/" + textureName + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(PopBlockEntity animatable) {
        return ANIMATION;
    }
}
