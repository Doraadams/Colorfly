package net.local.color.entity.client;

import com.google.common.collect.Maps;
import net.local.color.Colorfly;
import net.local.color.entity.custom.ColorflyEntity;
import net.local.color.entity.variant.ColorflyVariant;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.layer.LayerGlowingAreasGeo;

import java.util.Map;

public class ColorflyRenderer extends GeoEntityRenderer <ColorflyEntity> {

    public static final Map<ColorflyVariant, Identifier> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(ColorflyVariant.class), (map) -> {
                map.put(ColorflyVariant.GREEN,
                        new Identifier(Colorfly.MOD_ID, "textures/entity/colorfly_texture_green.png"));
                map.put(ColorflyVariant.BLUE,
                        new Identifier(Colorfly.MOD_ID, "textures/entity/colorfly_texture_blue.png"));
            });

    public ColorflyRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ColorflyModel());
        this.shadowRadius=0;
        this.addLayer(new LayerGlowingAreasGeo<>(this, this::getTextureLocation, this::getModelLocation,
                    RenderLayer::getEyes));
    }

    public Identifier getModelLocation(ColorflyEntity entity) {
        return this.modelProvider.getModelResource(entity);
    }

    @Override
    public Identifier getTextureResource(ColorflyEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public RenderLayer getRenderType(ColorflyEntity animatable, float partialTicks, MatrixStack stack,
                                     VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder,
                                     int packedLightIn, Identifier textureLocation) {
        stack.scale(0.75f, 0.75f, 0.75f);

        return RenderLayer.getEntityTranslucent(this.getTextureLocation(animatable));
    }
}

