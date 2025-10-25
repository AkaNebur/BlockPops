package com.theplumteam.client.model;

import com.theplumteam.BlockPopsMod;
import com.theplumteam.blockentity.BoxBlockEntity;
import com.theplumteam.figure.FigureType;
import net.minecraft.client.renderer.RenderType;
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
        // Use dedicated figure texture based on figure type
        FigureType figureType = animatable.getFigureType();
        if (figureType == FigureType.NONE) {
            return null;
        }
        return new ResourceLocation(BlockPopsMod.MOD_ID, "textures/figure/box_figure_" + figureType.getSerializedName() + ".png");
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

    @Override
    public RenderType getRenderType(BoxBlockEntity animatable, ResourceLocation texture) {
        // Use entityCutoutNoCull like Lineages does for the book
        // This ensures proper rendering without culling issues
        return RenderType.entityCutoutNoCull(getTextureResource(animatable));
    }
}
