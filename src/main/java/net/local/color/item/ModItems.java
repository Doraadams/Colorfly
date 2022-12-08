package net.local.color.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.local.color.Colorfly;
import net.local.color.entity.ModEntities;
import net.local.color.item.custom.BlueflyBottleItem;
import net.local.color.item.custom.GreenflyBottleItem;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    // Mod Items
    public static final Item GREENFLY_BOTTLE = registerItem("greenfly_bottle",
            new GreenflyBottleItem(new FabricItemSettings().maxCount(16)));
    public static final Item BLUEFLY_BOTTLE = registerItem("bluefly_bottle",
            new BlueflyBottleItem(new FabricItemSettings().maxCount(16)));

    // Mod Entity Spawn Eggs
    public static final Item GREENFLY_SPAWN_EGG = registerItem("greenfly_spawn_egg",
            new SpawnEggItem(ModEntities.GREENFLY,0x959B9B, 0x85F1BC, new FabricItemSettings()));
    public static final Item BLUEFLY_SPAWN_EGG = registerItem("bluefly_spawn_egg",
            new SpawnEggItem(ModEntities.BLUEFLY,0x959B9B, 0x9fedf4, new FabricItemSettings()));

    // Mod Item Register
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Colorfly.MOD_ID, name), item);
    }
    public static void registerModItems() {
        Colorfly.LOGGER.debug("Registering Mod Items for " + Colorfly.MOD_ID);
    }
}