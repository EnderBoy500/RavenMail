package net.enderboy.ravenmail.item;

import net.enderboy.ravenmail.RavenMailContent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class RavenEggItem extends BlockItem {
    public RavenEggItem(Properties properties) {
        super(RavenMailContent.RAVEN_EGG, properties.component(DataComponents.DYED_COLOR, new DyedItemColor(0x454551)));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
    }
}
