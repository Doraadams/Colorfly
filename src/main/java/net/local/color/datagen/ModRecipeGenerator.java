package net.local.color.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.local.color.block.ModBlocks;
import net.local.color.item.ModItems;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

// Mod Recipe DataGenerator
public class ModRecipeGenerator extends FabricRecipeProvider {
    public ModRecipeGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        // Greenfly Lantern Recipe DataGenerator
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModBlocks.GREENFLY_LANTERN, 1)
                .pattern("XXX")
                .pattern("X#X")
                .pattern("XXX")
                .input('X', Items.IRON_NUGGET)
                .input('#', ModItems.GREENFLY_BOTTLE)
                .criterion(RecipeProvider.hasItem(Items.IRON_NUGGET),
                        RecipeProvider.conditionsFromItem(Items.IRON_NUGGET))
                .criterion(RecipeProvider.hasItem(ModItems.GREENFLY_BOTTLE),
                        RecipeProvider.conditionsFromItem(ModItems.GREENFLY_BOTTLE))
                .offerTo(exporter, new Identifier(RecipeProvider.getRecipeName(ModBlocks.GREENFLY_LANTERN)));

        // Bluefly Lantern Recipe DataGenerator
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModBlocks.BLUEFLY_LANTERN)
                .pattern("XXX")
                .pattern("X#X")
                .pattern("XXX")
                .input('X', Items.IRON_NUGGET)
                .input('#', ModItems.BLUEFLY_BOTTLE)
                .criterion(RecipeProvider.hasItem(Items.IRON_NUGGET),
                        RecipeProvider.conditionsFromItem(Items.IRON_NUGGET))
                .criterion(RecipeProvider.hasItem(ModItems.BLUEFLY_BOTTLE),
                        RecipeProvider.conditionsFromItem(ModItems.BLUEFLY_BOTTLE))
                .offerTo(exporter, new Identifier(RecipeProvider.getRecipeName(ModBlocks.BLUEFLY_LANTERN)));
    }
}
