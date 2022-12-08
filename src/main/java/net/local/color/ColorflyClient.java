package net.local.color;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.local.color.block.ModBlocks;
import net.local.color.entity.ModEntities;
import net.local.color.entity.client.BlueflyRenderer;
import net.local.color.entity.client.GreenflyRenderer;
import net.minecraft.client.render.RenderLayer;

// Colorfly Client Initialize
public class ColorflyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Initialize Colorfly Entities
        EntityRendererRegistry.register(ModEntities.GREENFLY, GreenflyRenderer::new);
        EntityRendererRegistry.register(ModEntities.BLUEFLY, BlueflyRenderer::new);

        // Initialize Colorfly Lantern Translucent Block Render Layer
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GREENFLY_LANTERN, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLUEFLY_LANTERN, RenderLayer.getTranslucent());
    }
}