package com.theplumteam.client.model;

import com.theplumteam.BlockPopsMod;
import com.theplumteam.blockentity.BoxBlockEntity;
import com.theplumteam.figure.FigureType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FigureModel extends GeoModel<BoxBlockEntity> {
    @Override
    public ResourceLocation getModelResource(BoxBlockEntity animatable) {
        FigureType figureType = animatable.getFigureType();
        if (figureType == FigureType.NONE || figureType.getModelPath() == null) {
            return null;
        }
        return new ResourceLocation(BlockPopsMod.MOD_ID, figureType.getModelPath());
    }

    @Override
    public ResourceLocation getTextureResource(BoxBlockEntity animatable) {
        // Use the same texture as the box
        String textureName = animatable.getColor().getTextureName();
        return new ResourceLocation(BlockPopsMod.MOD_ID, "textures/block/box/" + textureName + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(BoxBlockEntity animatable) {
        FigureType figureType = animatable.getFigureType();
        if (figureType == FigureType.NONE) {
            return null;
        }
        // Each figure type can have its own animation file
        return new ResourceLocation(BlockPopsMod.MOD_ID, "animations/figure/box_figure_" + figureType.getSerializedName() + ".animation.json");
    }
}
