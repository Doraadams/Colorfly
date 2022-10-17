package net.local.color.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.local.color.Colorfly;
import net.local.color.entity.ModEntities;
import net.local.color.item.custom.BlueflyBottleItem;
import net.local.color.item.custom.GreenflyBottleItem;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    //Custom Item Register
    public static final Item GREENFLY_BOTTLE = registerItem("greenfly_bottle",
            new GreenflyBottleItem(new FabricItemSettings().maxCount(16).group(ModItemGroup.COLORFLY)));
    public static final Item BLUEFLY_BOTTLE = registerItem("bluefly_bottle",
            new BlueflyBottleItem(new FabricItemSettings().maxCount(16).group(ModItemGroup.COLORFLY)));

    public static final Item COLORFLY_SPAWN_EGG = registerItem("colorfly_spawn_egg",
            new SpawnEggItem(ModEntities.COLORFLY,0xCECCBA, 0x51A03E,
                    new FabricItemSettings().group(ModItemGroup.COLORFLY)));

    //General Item Register
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Colorfly.MOD_ID, name), item);
    }
    public static void registerModItems() {
        Colorfly.LOGGER.debug("Registering Mod Items for " + Colorfly.MOD_ID);
    }
}