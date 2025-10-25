package com.theplumteam.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.theplumteam.blockentity.BoxBlockEntity;
import com.theplumteam.client.model.BoxBlockModel;
import com.theplumteam.client.model.FigureModel;
import com.theplumteam.figure.FigureType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class BoxBlockRenderer extends GeoBlockRenderer<BoxBlockEntity> {
    private final FigureModel figureModel;

    public BoxBlockRenderer() {
        super(new BoxBlockModel());
        this.figureModel = new FigureModel();
    }

    @Override
    public void actuallyRender(PoseStack poseStack, BoxBlockEntity animatable, BakedGeoModel model,
                              RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer,
                              boolean isReRender, float partialTick, int packedLight, int packedOverlay,
                              float red, float green, float blue, float alpha) {
        // First, render the box model (the main model)
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer,
                           isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        // Then, render the figure model if one exists
        FigureType figureType = animatable.getFigureType();
        if (figureType.hasFigure()) {
            // Get the figure's model
            BakedGeoModel figureGeoModel = figureModel.getBakedModel(figureModel.getModelResource(animatable));

            // Get the figure's texture
            ResourceLocation figureTexture = figureModel.getTextureResource(animatable);
            RenderType figureRenderType = RenderType.entityCutout(figureTexture);

            // Get a new vertex consumer for the figure
            VertexConsumer figureBuffer = bufferSource.getBuffer(figureRenderType);

            // Render the figure model - it already has the correct positioning from its pivot in the model file
            super.actuallyRender(poseStack, animatable, figureGeoModel, figureRenderType, bufferSource,
                               figureBuffer, isReRender, partialTick, packedLight, packedOverlay,
                               red, green, blue, alpha);
        }
    }
}
