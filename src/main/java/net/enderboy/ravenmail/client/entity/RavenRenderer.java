package net.enderboy.ravenmail.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.enderboy.ravenmail.RavenMail;
import net.enderboy.ravenmail.entity.Raven;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class RavenRenderer extends AgeableMobRenderer<Raven, RavenRenderState, RavenModel> {
    public static final Identifier TEXTURE = RavenMail.id("textures/entity/raven/raven_black.png");

    public RavenRenderer(EntityRendererProvider.Context context) {
        super(context, new RavenModel(context.bakeLayer(RavenModel.RAVEN)),  new RavenModel(context.bakeLayer(RavenModel.RAVEN)), 0.3f);
        this.addLayer(new RavenHeldItemLayer(this));
    }



    @Override
    protected void scale(RavenRenderState livingEntityRenderState, PoseStack poseStack) {
        super.scale(livingEntityRenderState, poseStack);
        if (livingEntityRenderState.isBaby) poseStack.scale(0.7f, 0.7f, 0.7f);
        else poseStack.scale(1, 1, 1);
    }

    @Override
    public void extractRenderState(Raven livingEntity, RavenRenderState livingEntityRenderState, float f) {
        super.extractRenderState(livingEntity, livingEntityRenderState, f);
        livingEntityRenderState.texture = livingEntity.getVariant().value().assetInfo().texturePath();
        RavenRenderState.extractHoldingEntityRenderState(livingEntity, livingEntityRenderState, this.itemModelResolver);
        livingEntityRenderState.flying = livingEntity.isFlying();
    }

    @Override
    public Identifier getTextureLocation(RavenRenderState livingEntityRenderState) {
        return livingEntityRenderState.texture;
    }

    @Override
    public RavenRenderState createRenderState() {
        return new RavenRenderState();
    }
}
