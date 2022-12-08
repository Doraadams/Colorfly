package net.local.color.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.local.color.entity.ModEntities;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.BiomeKeys;

// Colorfly Spawn Conditions & Restrictions
public class ModEntitySpawn {
    public static void addEntitySpawn() {
        // Greenfly Other Spawn Conditions
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                        BiomeKeys.FLOWER_FOREST,
                        BiomeKeys.DARK_FOREST,
                        BiomeKeys.LUSH_CAVES,
                        BiomeKeys.SWAMP,
                        BiomeKeys.MANGROVE_SWAMP,
                        BiomeKeys.MEADOW,
                        BiomeKeys.WINDSWEPT_FOREST,
                        BiomeKeys.TAIGA,
                        BiomeKeys.OLD_GROWTH_PINE_TAIGA,
                        BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA,
                        BiomeKeys.SUNFLOWER_PLAINS,
                        BiomeKeys.FOREST,
                        BiomeKeys.BIRCH_FOREST,
                        BiomeKeys.OLD_GROWTH_BIRCH_FOREST),
                SpawnGroup.AMBIENT, ModEntities.GREENFLY, 10, 10, 15);

        // Bluefly Other Spawn Conditions
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                        BiomeKeys.FLOWER_FOREST,
                        BiomeKeys.DARK_FOREST,
                        BiomeKeys.LUSH_CAVES),
                SpawnGroup.AMBIENT, ModEntities.BLUEFLY, 1, 1, 3);
    }
}