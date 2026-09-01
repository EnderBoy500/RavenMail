package net.enderboy.ravenmail.client;

import net.enderboy.ravenmail.block.RavenEggBlockEntity;
import net.enderboy.ravenmail.client.entity.model.RavenModel;
import net.enderboy.ravenmail.client.entity.RavenRenderer;
import net.enderboy.ravenmail.RavenMailContent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.impl.client.indigo.renderer.helper.ColorHelper;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class RavenMailClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(RavenModel.RAVEN, RavenModel::createBodyLayer);
        EntityRenderers.register(RavenMailContent.RAVEN, RavenRenderer::new);

        BlockRenderLayerMap.putBlocks(ChunkSectionLayer.CUTOUT, RavenMailContent.RAVEN_EGG);

        ColorProviderRegistry.BLOCK.register(((state, world, pos, tintIndex) -> {
            if (world != null && world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof RavenEggBlockEntity ravenEggBlockEntity && ravenEggBlockEntity.getRavenVariant() != null) return ravenEggBlockEntity.getRavenVariant().value().color();
            return 4539729;
        }), RavenMailContent.RAVEN_EGG);
    }
}
