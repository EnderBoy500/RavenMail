package net.enderboy.ravenmail.entity.goal;

import net.enderboy.ravenmail.entity.Raven;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;

public class AttackPestGoal extends Goal {
    Raven raven;
    Class<? extends Entity> pest;
    Entity targetedPest;

    public AttackPestGoal(Raven raven, Class<? extends Entity> pest) {
        this.raven = raven;
        this.pest = pest;
    }

    @Override
    public boolean canUse() {
        List<? extends Entity> itemEntities = raven.level().getEntitiesOfClass(pest, raven.getBoundingBox().inflate(10));
        if (itemEntities.isEmpty()) return false;
        targetedPest = itemEntities.getFirst();
        return true;
    }

    @Override
    public void tick() {
        if (targetedPest != null && targetedPest.isAlive()) {
            raven.getNavigation().createPath(targetedPest, 1);
            if (raven.distanceTo(targetedPest) < 1) {
                raven.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100 ,0));
                targetedPest.discard();
            }
        }
        super.tick();
    }
}
