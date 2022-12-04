package net.local.color.entity.client;

import net.local.color.entity.custom.GreenflyEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

// Bluefly Rendering
public class GreenflyRenderer extends GeoEntityRenderer<GreenflyEntity> {

    // Render Factory, Shadow Radius, and Emissive Initialize
    public GreenflyRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new GreenflyModel());
        this.shadowRadius=0;
        this.withScale(1.0F);
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}