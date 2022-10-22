package net.local.color.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.local.color.entity.ModEntities;
import net.local.color.entity.custom.BlueflyEntity;
import net.local.color.entity.custom.GreenflyEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;

public class ModEntitySpawn {

    //Colorfly Custom Spawn Data/Restrictions
    public static void addEntitySpawn() {

        //Greenfly Spawn Conditions
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.LUSH_CAVES),
                SpawnGroup.CREATURE, ModEntities.GREENFLY, 2500, 10, 20);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                        BiomeKeys.FLOWER_FOREST,
                        BiomeKeys.DARK_FOREST,
                        BiomeKeys.SWAMP,
                        BiomeKeys.MANGROVE_SWAMP,
                        BiomeKeys.MEADOW,
                        BiomeKeys.DEEP_DARK),
                SpawnGroup.CREATURE, ModEntities.GREENFLY, 2000, 5, 10);
        SpawnRestriction.register(ModEntities.GREENFLY, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GreenflyEntity::canCustomSpawn);

        //Bluefly Spawn Conditions
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.LUSH_CAVES),
                SpawnGroup.CREATURE, ModEntities.BLUEFLY, 250, 3, 5);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                        BiomeKeys.FLOWER_FOREST,
                        BiomeKeys.DARK_FOREST,
                        BiomeKeys.SWAMP,
                        BiomeKeys.MANGROVE_SWAMP,
                        BiomeKeys.MEADOW,
                        BiomeKeys.DEEP_DARK),
                SpawnGroup.CREATURE, ModEntities.BLUEFLY, 200, 1, 3);
        SpawnRestriction.register(ModEntities.BLUEFLY, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BlueflyEntity::canCustomSpawn);
    }
}