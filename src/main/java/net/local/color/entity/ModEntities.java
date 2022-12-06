package net.local.color.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.local.color.Colorfly;
import net.local.color.entity.custom.BlueflyEntity;
import net.local.color.entity.custom.GreenflyEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

//Entity Creation & Settings
public class ModEntities {
    public static final EntityType<GreenflyEntity> GREENFLY = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(Colorfly.MOD_ID, "greenfly"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GreenflyEntity::new)
                    .spawnableFarFromPlayer()
                    .trackRangeBlocks(30)
                    // Hitbox Size
                    .dimensions(EntityDimensions.fixed(0.0475f,0.0375f))
                    .fireImmune()
                    .build());
    public static final EntityType<BlueflyEntity> BLUEFLY = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(Colorfly.MOD_ID, "bluefly"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, BlueflyEntity::new)
                    .spawnableFarFromPlayer()
                    .trackRangeBlocks(30)
                    // Hitbox Size
                    .dimensions(EntityDimensions.fixed(0.0625f,0.0625f))
                    .fireImmune()
                    .build());
}