package net.local.color.entity.client;

import net.local.color.Colorfly;
import net.local.color.entity.custom.ColorflyEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import net.minecraft.util.Identifier;

//Colorfly Model Resource Directories/Variables
public class ColorflyModel extends AnimatedGeoModel<ColorflyEntity> {
    @Override
    public Identifier getModelResource(ColorflyEntity object) {
        return new Identifier(Colorfly.MOD_ID, "geo/colorfly.geo.json");
    }

    @Override
    public Identifier getTextureResource(ColorflyEntity object) {
        return ColorflyRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public Identifier getAnimationResource(ColorflyEntity animatable) {
        return new Identifier(Colorfly.MOD_ID , "animations/colorfly.animation.json");
    }
}
