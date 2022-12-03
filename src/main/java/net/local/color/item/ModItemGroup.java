package net.local.color.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.local.color.Colorfly;
import net.local.color.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroup {

    /*
    public static final ItemGroup COLORFLY = FabricItemGroup.builder(new Identifier(Colorfly.MOD_ID, "colorfly"))
            .displayName(Text.literal("Colorfly"))
            .icon(() -> new ItemStack(Items.CACTUS))
            .entries((enabledFeatures, entries, operatorEnabled) -> {
                entries.add(Items.DIAMOND);
                entries.add(ModItems.GREENFLY_BOTTLE);
                entries.add(ModBlocks.GREENFLY_LANTERN);
                entries.add(ModItems.BLUEFLY_BOTTLE);
                entries.add(ModBlocks.BLUEFLY_LANTERN);
            })
            .build();

     */

    //Debug Log
    public static void registerModItemGroup() {
        Colorfly.LOGGER.debug("Registering Mod Item Group for " + Colorfly.MOD_ID);

        ItemGroup COLORFLY = FabricItemGroup.builder(new Identifier(Colorfly.MOD_ID, "colorfly"))
                .displayName(Text.literal("Colorfly"))
                .icon(() -> new ItemStack(Items.CACTUS))
                .entries((enabledFeatures, entries, operatorEnabled) -> {
                    entries.add(Items.DIAMOND);
                    entries.add(ModItems.GREENFLY_BOTTLE);
                    entries.add(ModBlocks.GREENFLY_LANTERN);
                    entries.add(ModItems.BLUEFLY_BOTTLE);
                    entries.add(ModBlocks.BLUEFLY_LANTERN);
                })
                .build();

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> entries.add(ModItems.GREENFLY_SPAWN_EGG));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> entries.add(ModItems.BLUEFLY_SPAWN_EGG));

    }
}