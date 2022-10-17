package net.local.color.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.local.color.Colorfly;
import net.local.color.entity.custom.BlueflyEntity;
import net.local.color.entity.custom.GreenflyEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    //Colorfly Entity Creation, Hitbox, & Other.
    public static final EntityType<GreenflyEntity> GREENFLY = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(Colorfly.MOD_ID, "greenfly"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GreenflyEntity::new)
                    .spawnableFarFromPlayer()
                    .trackRangeBlocks(64)
                    .dimensions(EntityDimensions.fixed(0.1f,0.3f))
                    .fireImmune()
                    .build());

    public static final EntityType<BlueflyEntity> BLUEFLY = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(Colorfly.MOD_ID, "bluefly"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, BlueflyEntity::new)
                    .spawnableFarFromPlayer()
                    .trackRangeBlocks(64)
                    .dimensions(EntityDimensions.fixed(0.1f,0.3f))
                    .fireImmune()
                    .build());
}