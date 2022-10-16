package net.local.color;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.local.color.block.ModBlocks;
import net.local.color.entity.ModEntities;
import net.local.color.entity.client.ColorflyRenderer;
import net.minecraft.client.render.RenderLayer;

public class ColorflyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.COLORFLY, ColorflyRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GREENFLY_LAMP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLUEFLY_LAMP, RenderLayer.getTranslucent());
    }
}
