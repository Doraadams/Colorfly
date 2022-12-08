package net.local.color.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.local.color.Colorfly;
import net.local.color.entity.custom.AbstractColorflyEntity;
import net.local.color.entity.custom.BlueflyEntity;
import net.local.color.entity.custom.GreenflyEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;

//Entity Creation & Settings
public class ModEntities {
    public static final EntityType<GreenflyEntity> GREENFLY = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(Colorfly.MOD_ID, "greenfly"),
            FabricEntityTypeBuilder.createMob()
                    .spawnGroup(SpawnGroup.AMBIENT)
                    .spawnableFarFromPlayer()
                    .entityFactory(GreenflyEntity::new)
                    .spawnRestriction(
                            SpawnRestriction.Location.ON_GROUND,
                            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                            AbstractColorflyEntity::canSpawn)
                    // Hitbox Size
                    .dimensions(EntityDimensions.fixed(0.0475f,0.0375f))
                    .fireImmune()
                    .build());
    public static final EntityType<BlueflyEntity> BLUEFLY = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(Colorfly.MOD_ID, "bluefly"),
            FabricEntityTypeBuilder.createMob()
                    .spawnGroup(SpawnGroup.AMBIENT)
                    .spawnableFarFromPlayer()
                    .entityFactory(BlueflyEntity::new)
                    .spawnRestriction(
                            SpawnRestriction.Location.ON_GROUND,
                            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                            AbstractColorflyEntity::canSpawn)
                    // Hitbox Size
                    .dimensions(EntityDimensions.fixed(0.0625f,0.0625f))
                    .fireImmune()
                    .build());
}