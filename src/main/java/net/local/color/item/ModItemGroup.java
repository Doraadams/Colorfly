package net.local.color.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.local.color.Colorfly;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {

    //Colorfly ItemGroup
    public static final ItemGroup COLORFLY = FabricItemGroupBuilder.build(
            new Identifier(Colorfly.MOD_ID, "colorfly"), () -> new ItemStack(ModItems.GREENFLY_SPAWN_EGG));

    //Debug Log
    public static void registerModItemGroup() {
        Colorfly.LOGGER.debug("Registering Mod Item Group for " + Colorfly.MOD_ID);
    }
}