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

@SuppressWarnings("unused")
public class ModItemGroup {

    // Colorfly Item Group
    public static final ItemGroup COLORFLY = FabricItemGroup.builder(new Identifier(Colorfly.MOD_ID, "colorfly"))
            .displayName(Text.literal("Colorfly"))
            .icon(() -> new ItemStack(ModItems.GREENFLY_BOTTLE))
            .entries((enabledFeatures, entries, operatorEnabled) -> {
                // Mod Items
                entries.add(ModItems.GREENFLY_BOTTLE);
                entries.add(ModItems.BLUEFLY_BOTTLE);

                // Mod Blocks
                entries.add(ModBlocks.GREENFLY_LANTERN);
                entries.add(ModBlocks.BLUEFLY_LANTERN);

                // Mod Entity Eggs
                entries.add(ModItems.GREENFLY_SPAWN_EGG);
                entries.add(ModItems.BLUEFLY_SPAWN_EGG);
            }).build();

    // Custom register in Vanilla Item Groups
    @SuppressWarnings("UnstableApiUsage")
    public static void registerVanillaItemGroup() {
        // Mod Items
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries ->
                entries.addAfter(Items.GLASS_BOTTLE, ModItems.GREENFLY_BOTTLE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries ->
                entries.addAfter(ModItems.GREENFLY_BOTTLE, ModItems.BLUEFLY_BOTTLE));

        // Mod Blocks
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries ->
                entries.addAfter(Items.SOUL_LANTERN, ModBlocks.GREENFLY_LANTERN));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries ->
                entries.addAfter(ModBlocks.GREENFLY_LANTERN, ModBlocks.BLUEFLY_LANTERN));

        // Mod Entity Eggs
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries ->
                entries.addAfter(Items.FROG_SPAWN_EGG, ModItems.GREENFLY_SPAWN_EGG));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries ->
                entries.addAfter(ModItems.GREENFLY_SPAWN_EGG, ModItems.BLUEFLY_SPAWN_EGG)); }

    // Debug Log & Call registerVanillaItemGroup() on Launch
    public static void registerModItemGroup() {
        Colorfly.LOGGER.debug("Registering Mod Item Group for " + Colorfly.MOD_ID);
        registerVanillaItemGroup();
    }
}