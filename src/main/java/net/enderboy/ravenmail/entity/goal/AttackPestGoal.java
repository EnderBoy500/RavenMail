package net.enderboy.ravenmail.entity.goal;

import net.enderboy.ravenmail.entity.Raven;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;

public class AttackPestGoal extends Goal {
    Raven raven;
    Class<? extends Entity> entityClass;
    Entity targetedEntity;

    public AttackPestGoal(Raven raven, Class<? extends Entity> entityClass) {
        this.raven = raven;
        this.entityClass = entityClass;
    }

    @Override
    public boolean canUse() {
        List<? extends Entity> itemEntities = raven.level().getEntitiesOfClass(entityClass, raven.getBoundingBox().inflate(10));
        if (itemEntities.isEmpty()) return false;
        targetedEntity = itemEntities.get(0);
        return true;
    }

    @Override
    public void tick() {
        if (targetedEntity != null && targetedEntity.isAlive()) {
            raven.getNavigation().createPath(targetedEntity, 1);
            if (raven.distanceTo(targetedEntity) < 1) {
                raven.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100 ,0));
                targetedEntity.discard();
            }
        }
        super.tick();
    }
}
