package net.local.color.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.local.color.entity.ModEntities;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;

public class ModEntitySpawn {
    public static void addEntitySpawn() {
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.LUSH_CAVES),
                SpawnGroup.CREATURE, ModEntities.COLORFLY, 2000, 10, 20);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                        BiomeKeys.FLOWER_FOREST,
                        BiomeKeys.DARK_FOREST,
                        BiomeKeys.SWAMP,
                        BiomeKeys.MANGROVE_SWAMP,
                        BiomeKeys.MEADOW,
                        BiomeKeys.DEEP_DARK),
                SpawnGroup.CREATURE, ModEntities.COLORFLY, 2500, 10, 20);

        SpawnRestriction.register(ModEntities.COLORFLY, SpawnRestriction.Location.NO_RESTRICTIONS,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn);
    }
}
