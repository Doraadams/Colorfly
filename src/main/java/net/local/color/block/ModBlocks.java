package net.local.color.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.local.color.Colorfly;
import net.local.color.block.custom.BlueflyLanternBlock;
import net.local.color.block.custom.GreenflyLanternBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    //Custom Block register
    public static final Block GREENFLY_LANTERN = registerBlock("greenfly_lantern",
            new GreenflyLanternBlock(FabricBlockSettings.of(Material.STONE).strength(4f).requiresTool().sounds(BlockSoundGroup.LANTERN)
                    .luminance(state -> state.get(GreenflyLanternBlock.LIT))));

    public static final Block BLUEFLY_LANTERN = registerBlock("bluefly_lantern",
            new BlueflyLanternBlock(FabricBlockSettings.of(Material.STONE).strength(4f).requiresTool().sounds(BlockSoundGroup.LANTERN)
                    .luminance(state -> state.get(BlueflyLanternBlock.LIT))));

    //General Block Register
    //private static Block registerBlockWithoutItem(String name, Block block) {
    //    return Registry.register(Registries.BLOCK, new Identifier(Colorfly.MOD_ID, name), block);
    //}
    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(Colorfly.MOD_ID, name), block);
    }
    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(Colorfly.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
    }
    //private static Block registerBlockWithoutBlockItem(String name, Block block, ItemGroup group) {
    //    return Registry.register(Registries.BLOCK, new Identifier(Colorfly.MOD_ID, name), block);
    //}
    public static void registerModBlocks() {
        Colorfly.LOGGER.debug("Registering ModBlocks for " + Colorfly.MOD_ID);
    }
}
