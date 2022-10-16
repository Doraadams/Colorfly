package net.local.color.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.local.color.Colorfly;
import net.local.color.block.custom.BlueflyLampBlock;
import net.local.color.block.custom.GreenflyLampBlock;
import net.local.color.item.ModItemGroup;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final Block GREENFLY_LAMP = registerBlock("greenfly_lamp",
            new GreenflyLampBlock(FabricBlockSettings.of(Material.STONE).strength(4f).requiresTool()
                    .luminance(state -> state.get(GreenflyLampBlock.LIT))), ModItemGroup.COLORFLY);

    public static final Block BLUEFLY_LAMP = registerBlock("bluefly_lamp",
            new BlueflyLampBlock(FabricBlockSettings.of(Material.STONE).strength(4f).requiresTool()
                    .luminance(state -> state.get(BlueflyLampBlock.LIT))), ModItemGroup.COLORFLY);

    private static Block registerBlockWithoutItem(String name, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(Colorfly.MOD_ID, name), block);
    }
    private static Block registerBlock(String name, Block block, ItemGroup tab) {
        registerBlockItem(name, block, tab);
        return Registry.register(Registry.BLOCK, new Identifier(Colorfly.MOD_ID, name), block);
    }
    private static Item registerBlockItem(String name, Block block, ItemGroup tab) {
        return Registry.register(Registry.ITEM, new Identifier(Colorfly.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(tab)));
    }
    private static Block registerBlockWithoutBlockItem(String name, Block block, ItemGroup group) {
        return Registry.register(Registry.BLOCK, new Identifier(Colorfly.MOD_ID, name), block);
    }
    public static void registerModBlocks() {
        Colorfly.LOGGER.debug("Registering ModBlocks for " + Colorfly.MOD_ID);
    }
}
