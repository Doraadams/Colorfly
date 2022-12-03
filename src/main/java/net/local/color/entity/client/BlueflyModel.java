package net.local.color.entity.client;

import net.local.color.Colorfly;
import net.local.color.entity.custom.BlueflyEntity;
import software.bernie.geckolib.model.GeoModel;
import net.minecraft.util.Identifier;

//Bluefly Model Resource Directories/Variables
public class BlueflyModel extends GeoModel<BlueflyEntity> {
    @Override
    public Identifier getModelResource(BlueflyEntity object) {
        return new Identifier(Colorfly.MOD_ID, "geo/colorfly.geo.json");
    }

    @Override
    public Identifier getTextureResource(BlueflyEntity object) {
        return new Identifier(Colorfly.MOD_ID, "textures/entity/bluefly_texture.png");
    }

    @Override
    public Identifier getAnimationResource(BlueflyEntity animatable) {
        return new Identifier(Colorfly.MOD_ID , "animations/colorfly.animation.json");
    }
}
