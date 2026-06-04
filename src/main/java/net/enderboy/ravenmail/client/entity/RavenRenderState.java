package net.enderboy.ravenmail.client.entity;

import net.enderboy.ravenmail.entity.Raven;
import net.enderboy.ravenmail.entity.RavenVariants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;


@Environment(EnvType.CLIENT)
public class RavenRenderState extends LivingEntityRenderState {
    private static final Identifier DEFAULT_TEXTURE = RavenVariants.BLACK.identifier();
    public Identifier texture;
    public boolean isSitting;
    public boolean flying;
    public final ItemStackRenderState heldItem = new ItemStackRenderState();

    public static void extractHoldingEntityRenderState(
            LivingEntity livingEntity, RavenRenderState holdingEntityRenderState, ItemModelResolver itemModelResolver
    ) {
        itemModelResolver.updateForLiving(holdingEntityRenderState.heldItem, livingEntity.getEntityData().get(Raven.HELD_ITEM), ItemDisplayContext.GROUND, livingEntity);
    }
}
