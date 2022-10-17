package net.local.color.entity.client;

import net.local.color.entity.custom.BlueflyEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.layer.LayerGlowingAreasGeo;

// Bluefly Rendering
public class BlueflyRenderer extends GeoEntityRenderer <BlueflyEntity> {

    // Render Factory, Shadow Radius, and Emissive Initialize
    public BlueflyRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BlueflyModel());
        this.shadowRadius=0;
        this.addLayer(new LayerGlowingAreasGeo<>(this, this::getTextureLocation, this::getModelLocation,
                    RenderLayer::getEyes));
    }

    // Get Model & Texture Directory
    public Identifier getModelLocation(BlueflyEntity entity) {
        return this.modelProvider.getModelResource(entity);
    }

    // RenderType, Entity Scale, and Translucent/Opaque Layer
    @Override
    public RenderLayer getRenderType(BlueflyEntity animatable, float partialTicks, MatrixStack stack,
                                     VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder,
                                     int packedLightIn, Identifier textureLocation) {
        stack.scale(0.75f, 0.75f, 0.75f);
        return RenderLayer.getEntityTranslucent(this.getTextureLocation(animatable));
    }
}