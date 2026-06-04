package net.enderboy.ravenmail.entity.goal;

import net.enderboy.ravenmail.RavenMail;
import net.enderboy.ravenmail.RavenMailContent;
import net.enderboy.ravenmail.entity.Raven;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.List;

public class SearchAndCollectItemGoal extends Goal {
    Raven raven;
    ItemEntity item;

    public SearchAndCollectItemGoal(Raven raven) {
        this.raven = raven;
    }

    @Override
    public boolean canUse() {
        List<ItemEntity> itemEntities = raven.level().getEntitiesOfClass(ItemEntity.class, raven.getBoundingBox().inflate(10), itemEntity -> itemEntity.getItem().is(RavenMailContent.RAVEN_ATTRACTED_TO));
        if (itemEntities.isEmpty()) return false;
        this.item = itemEntities.get(0);
        return raven.getInventory().isEmpty() && !raven.isInLove();
    }

    @Override
    public void tick() {
        if (raven.getInventory().isEmpty() && item != null && item.isAlive()) {
            raven.getNavigation().createPath(item, 1);
            if (raven.distanceTo(item) < 1.5) {
                raven.getInventory().setItem(0, item.getItem().copyWithCount(1));
                item.getItem().shrink(1);
            }
        }
    }
}
