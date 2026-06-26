package net.enderboy.ravenmail.item;

import net.enderboy.ravenmail.RavenMailContent;
import net.enderboy.ravenmail.entity.Raven;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public class RavenMasterOcarina extends Item {
    public RavenMasterOcarina(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        BlockPos pos = useOnContext.getClickedPos();
        Player player = useOnContext.getPlayer();
        ItemStack stack = useOnContext.getItemInHand();
        if (player.isShiftKeyDown() && pos != null) {
            stack.set(RavenMailContent.BLOCK_POS, pos);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(useOnContext);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand interactionHand) {
        if (itemStack.has(RavenMailContent.BLOCK_POS) && livingEntity instanceof Raven raven) {
            BlockPos pos = itemStack.get(RavenMailContent.BLOCK_POS);
            BlockPos goToPos = pos.above();
            raven.getNavigation().moveTo(goToPos.getX(), goToPos.getY(), goToPos.getZ(),1);
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(itemStack, player, livingEntity, interactionHand);
    }
}
