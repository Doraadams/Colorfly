package net.local.color.entity.client;

import net.local.color.entity.custom.GreenflyEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

// Greenfly Renderer
public class GreenflyRenderer extends GeoEntityRenderer<GreenflyEntity> {

    // Renderer, Shadow Radius, & Glow Render Layer
    public GreenflyRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new GreenflyModel());
        this.shadowRadius=0;
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}