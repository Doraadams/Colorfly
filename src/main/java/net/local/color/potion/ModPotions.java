package net.local.color.potion;

import net.local.color.Colorfly;
import net.local.color.item.ModItems;
import net.local.color.mixin.BrewingRecipeRegistryMixin;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModPotions {
    public static Potion GLOWING_POTION;

    public static Potion registerPotion(String name) {
        return Registry.register(Registries.POTION, new Identifier(Colorfly.MOD_ID, name),
                new Potion(new StatusEffectInstance(StatusEffects.GLOWING, 2400, 2, false, false, true)));
    }

    public static void registerPotions() {
        GLOWING_POTION = registerPotion("glowing_potion");

        registerPotionRecipes();
    }

    private static void registerPotionRecipes() {
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, ModItems.GREENFLY_BOTTLE,
                ModPotions.GLOWING_POTION);
    }
}
