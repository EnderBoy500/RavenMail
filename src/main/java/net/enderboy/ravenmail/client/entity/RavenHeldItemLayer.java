package net.enderboy.ravenmail.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class RavenHeldItemLayer extends RenderLayer<RavenRenderState, RavenModel> {
    public RavenHeldItemLayer(RenderLayerParent<RavenRenderState, RavenModel> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, RavenRenderState entityRenderState, float f, float g) {
        ItemStackRenderState itemStackRenderState = entityRenderState.heldItem;
        if (!itemStackRenderState.isEmpty()) {
            boolean bl2 = entityRenderState.isBaby;
            boolean bl = entityRenderState.isSitting;
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.translate(this.getParentModel().body.x / 16.0F, this.getParentModel().body.y / 16.0F, this.getParentModel().body.z / 16.0F);
            if (bl2) {
                float h = 0.75F;
                //poseStack.scale(0.5F, 0.5F, 0.5F);
            }

            poseStack.mulPose(Axis.YP.rotationDegrees(this.getParentModel().body.yRot));
            poseStack.mulPose(Axis.XP.rotationDegrees(this.getParentModel().body.xRot));
            if (entityRenderState.isBaby) {
                if (bl) {
                    poseStack.translate(0.4F, 0.26F, 0.15F);
                } else {
                    poseStack.translate(0.02F, 2.7F, -0.9F);
                }
            } else if (bl) {
                poseStack.translate(0.46F, -0.2F, 0.22F);
            } else {
                poseStack.translate(0.02F, 2.7F, -0.9F);
            }

            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            if (bl) {
                poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
            }

            itemStackRenderState.submit(poseStack, submitNodeCollector, i, OverlayTexture.NO_OVERLAY, entityRenderState.outlineColor);
            poseStack.popPose();
        }
    }
}
