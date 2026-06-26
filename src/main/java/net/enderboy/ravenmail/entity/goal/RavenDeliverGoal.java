package net.enderboy.ravenmail.entity.goal;

import net.enderboy.ravenmail.entity.Raven;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class RavenDeliverGoal extends Goal {
    Raven raven;

    public RavenDeliverGoal(Raven raven) {
        this.raven = raven;
    }

    @Override
    public boolean canUse() {
        ItemStack stack = raven.getMainHandItem();
        if (raven.level().getServer() != null && !stack.isEmpty() && stack.is(ItemTags.BUNDLES) && stack.getCustomName() != null) {
            Player player = raven.level().getServer().getPlayerList().getPlayer(stack.getCustomName().getString());
            if (player != null && raven.level().dimension() == player.level().dimension()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        ItemStack stack = raven.getMainHandItem();
        if (raven.level().getServer() != null && !stack.isEmpty() && stack.getCustomName() != null) {
            Player player = raven.level().getServer().getPlayerList().getPlayer(stack.getCustomName().getString());
            if (player != null && raven.level().dimension() == player.level().dimension()) {
                if (raven.distanceTo(player) <= 50) {
                    raven.getNavigation().moveTo(player, 1);
                } else {
                    teleport(player.getX(), player.getY(), player.getZ(), player);
                }
            }
        }
    }


    boolean teleportTowards(Entity entity) {
        Vec3 vec3 = new Vec3(raven.getX() - entity.getX(), raven.getY((double)0.5F) - entity.getEyeY(), raven.getZ() - entity.getZ());
        vec3 = vec3.normalize();
        double d = (double)16.0F;
        double e = raven.getX() + (raven.getRandom().nextDouble() - (double)0.5F) * (double)8.0F - vec3.x * (double)16.0F;
        double f = raven.getY() + (double)(raven.getRandom().nextInt(16) - 8) - vec3.y * (double)16.0F;
        double g = raven.getZ() + (raven.getRandom().nextDouble() - (double)0.5F) * (double)8.0F - vec3.z * (double)16.0F;
        return this.teleport(e, f, g, entity);
    }

    private boolean teleport(double d, double e, double f, Entity entity) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(d, e, f);
        Vec3 vec3 = new Vec3(raven.getX() - entity.getX(), raven.getY((double)0.5F) - entity.getEyeY(), raven.getZ() - entity.getZ());
        vec3 = vec3.normalize();
        double a = d + (raven.getRandom().nextDouble() - (double)0.5F) * (double)8.0F - vec3.x * (double)16.0F;
        double b = e + (double)(raven.getRandom().nextInt(16) - 8) - vec3.y * (double)16.0F;
        double c = f + (raven.getRandom().nextDouble() - (double)0.5F) * (double)8.0F - vec3.z * (double)16.0F;
        while(mutableBlockPos.getY() > raven.level().getMinY() && !raven.level().getBlockState(mutableBlockPos).blocksMotion()) {
            mutableBlockPos.move(Direction.DOWN);
        }

        BlockState blockState = raven.level().getBlockState(mutableBlockPos);
        boolean bl = blockState.blocksMotion();
        boolean bl2 = blockState.getFluidState().is(FluidTags.WATER);
        if (bl && !bl2) {
            boolean bl3 = raven.randomTeleport(a, b, c, true);
            return bl3;
        } else {
            return false;
        }
    }
}
