package net.local.color.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.local.color.Colorfly;
import net.local.color.entity.custom.ColorflyEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    //Colorfly Entity Creation, Hitbox, & Other.
    public static final EntityType<ColorflyEntity> COLORFLY = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(Colorfly.MOD_ID, "colorfly"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ColorflyEntity::new)
                    .spawnableFarFromPlayer()
                    .trackRangeBlocks(64)
                    .dimensions(EntityDimensions.fixed(0.1f,0.3f))
                    .fireImmune()
                    .build());
}