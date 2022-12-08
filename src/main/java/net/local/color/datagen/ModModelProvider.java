package net.local.color.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.local.color.block.ModBlocks;
import net.local.color.item.ModItems;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

// Mod Model DataGenerator
public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    // BlockState Model DataGenerator
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerLantern(ModBlocks.GREENFLY_LANTERN);
        blockStateModelGenerator.registerLantern(ModBlocks.BLUEFLY_LANTERN);
    }

    // Item Model DataGenerator
    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.GREENFLY_BOTTLE, Models.GENERATED);
        itemModelGenerator.register(ModItems.BLUEFLY_BOTTLE, Models.GENERATED);
    }
}
