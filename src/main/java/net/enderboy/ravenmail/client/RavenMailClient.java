package net.enderboy.ravenmail.client;

import net.enderboy.ravenmail.client.entity.RavenModel;
import net.enderboy.ravenmail.client.entity.RavenRenderer;
import net.enderboy.ravenmail.RavenMailContent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class RavenMailClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(RavenModel.RAVEN, RavenModel::createBodyLayer);
        EntityRenderers.register(RavenMailContent.RAVEN, RavenRenderer::new);
    }
}
