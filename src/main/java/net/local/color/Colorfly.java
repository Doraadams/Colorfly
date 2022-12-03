package net.local.color;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.local.color.block.ModBlocks;
import net.local.color.entity.ModEntities;
import net.local.color.entity.custom.BlueflyEntity;
import net.local.color.entity.custom.GreenflyEntity;
import net.local.color.item.ModItemGroup;
import net.local.color.item.ModItems;
import net.local.color.potion.ModPotions;
import net.local.color.world.gen.ModWorldGen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.GeckoLib;

//Initialize Colorfly
public class Colorfly implements ModInitializer {
	public static final String MOD_ID = "color";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	//On Initialize, Register Needed
	@Override
	public void onInitialize() {
		GeckoLib.initialize();

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModItemGroup.registerModItemGroup();

		ModPotions.registerPotions();

		ModWorldGen.generateModWorldGen();

		FabricDefaultAttributeRegistry.register(ModEntities.GREENFLY, GreenflyEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.BLUEFLY, BlueflyEntity.setAttributes());
	}
}