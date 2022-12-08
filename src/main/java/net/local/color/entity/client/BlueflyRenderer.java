package net.local.color.entity.client;

import net.local.color.entity.custom.BlueflyEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

// Bluefly Renderer
public class BlueflyRenderer extends GeoEntityRenderer<BlueflyEntity> {

    // Renderer, Shadow Radius, & Glow Render Layer
    public BlueflyRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BlueflyModel());
        this.shadowRadius=0;
        this.withScale(1.0F);
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}