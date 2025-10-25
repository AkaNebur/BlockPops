package com.theplumteam.client.renderer;

import com.theplumteam.blockentity.PopBlockEntity;
import com.theplumteam.client.model.PopBlockModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class PopBlockRenderer extends GeoBlockRenderer<PopBlockEntity> {
    public PopBlockRenderer() {
        super(new PopBlockModel());
    }
}
