package net.local.color;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.local.color.block.ModBlocks;
import net.local.color.entity.ModEntities;
import net.local.color.entity.custom.ColorflyEntity;
import net.local.color.item.ModItemGroup;
import net.local.color.item.ModItems;
import net.local.color.world.gen.ModWorldGen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib3.GeckoLib;

// Very important comment
public class Colorfly implements ModInitializer {
	public static final String MOD_ID = "color";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		GeckoLib.initialize();

		ModItems.registerModItems();
		ModItemGroup.registerModItemGroup();
		ModBlocks.registerModBlocks();
		
		ModWorldGen.generateModWorldGen();

		FabricDefaultAttributeRegistry.register(ModEntities.COLORFLY, ColorflyEntity.setAttributes());
	}
}
