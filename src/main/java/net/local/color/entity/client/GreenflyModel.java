package net.local.color.entity.client;

import net.local.color.Colorfly;
import net.local.color.entity.custom.GreenflyEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

//Bluefly Model Resource Directories/Variables
public class GreenflyModel extends AnimatedGeoModel<GreenflyEntity> {
    @Override
    public Identifier getModelResource(GreenflyEntity object) {
        return new Identifier(Colorfly.MOD_ID, "geo/colorfly.geo.json");
    }

    @Override
    public Identifier getTextureResource(GreenflyEntity object) {
        return new Identifier(Colorfly.MOD_ID, "textures/entity/greenfly_texture.png");
    }

    @Override
    public Identifier getAnimationResource(GreenflyEntity animatable) {
        return new Identifier(Colorfly.MOD_ID , "animations/colorfly.animation.json");
    }
}
