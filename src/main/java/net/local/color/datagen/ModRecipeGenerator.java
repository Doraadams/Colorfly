package net.local.color.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.local.color.block.ModBlocks;
import net.local.color.item.ModItems;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ModRecipeGenerator extends FabricRecipeProvider {
    public ModRecipeGenerator(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(ModBlocks.GREENFLY_LANTERN)
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

        ShapedRecipeJsonBuilder.create(ModBlocks.BLUEFLY_LANTERN)
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
