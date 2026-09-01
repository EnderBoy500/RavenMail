package net.enderboy.ravenmail.block;

import com.mojang.serialization.MapCodec;
import io.github.ciph3rj.cipherlib.util.ItemUtils;
import net.enderboy.ravenmail.RavenMailContent;
import net.enderboy.ravenmail.entity.Raven;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class RavenEggBlock extends BaseEntityBlock {
    public static final MapCodec<RavenEggBlock> CODEC = simpleCodec(RavenEggBlock::new);

    public RavenEggBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity == null) return;
        if (blockEntity instanceof RavenEggBlockEntity ravenEggBlockEntity && itemStack.has(RavenMailContent.RAVEN_VARIANT_COMPONENT)
        ) ravenEggBlockEntity.setRavenVariant(itemStack.get(RavenMailContent.RAVEN_VARIANT_COMPONENT));
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState blockState) {
        return true;
    }

    @Override
    protected void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        if (serverLevel.getBlockEntity(blockPos) != null && serverLevel.getBlockEntity(blockPos) instanceof RavenEggBlockEntity ravenEggBlockEntity &&
                serverLevel.getBlockState(blockPos.below()).is(RavenMailContent.SUITABLE_FOR_RAVEN_EGGS) && randomSource.nextBoolean()) {
            Raven raven = (Raven) RavenMailContent.RAVEN.create(serverLevel, EntitySpawnReason.BREEDING);
            if (raven != null) {
                Vec3 vec3 = blockPos.getCenter();
                if (ravenEggBlockEntity.getRavenVariant() != null) raven.getEntityData().set(Raven.DATA_VARIANT_ID, ravenEggBlockEntity.getRavenVariant());
                raven.setBaby(true);
                raven.snapTo(vec3.x(), vec3.y(), vec3.z(), Mth.wrapDegrees(serverLevel.random.nextFloat() * 360.0F), 0.0F);
                serverLevel.addFreshEntity(raven);
            }
            serverLevel.playSound((Entity) null, blockPos, SoundEvents.SNIFFER_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + randomSource.nextFloat() * 0.2F);
            serverLevel.destroyBlock(blockPos, false);
        }
        super.randomTick(blockState, serverLevel, blockPos, randomSource);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if (!ItemUtils.hasEnchantment(player.getMainHandItem(), "silk_touch")) return super.playerWillDestroy(level, blockPos, blockState, player);
        if (level.getBlockEntity(blockPos) != null &&level.getBlockEntity(blockPos) instanceof RavenEggBlockEntity ravenEggBlockEntity) {
            ItemStack stack = new ItemStack(RavenMailContent.RAVEN_EGG_ITEM);
            ItemEntity itemEntity = new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), stack);
            itemEntity.setDefaultPickUpDelay();
            if (ravenEggBlockEntity.getRavenVariant() != null) {
                stack.set(RavenMailContent.RAVEN_VARIANT_COMPONENT, ravenEggBlockEntity.getRavenVariant());
                stack.set(DataComponents.DYED_COLOR, new DyedItemColor(ravenEggBlockEntity.getRavenVariant().value().color()));
            } else {
                stack.set(RavenMailContent.RAVEN_VARIANT_COMPONENT, ravenEggBlockEntity.getRavenVariant());
                stack.set(DataComponents.DYED_COLOR, new DyedItemColor(0x454551));
            }
            level.addFreshEntity(itemEntity);
        }
        return super.playerWillDestroy(level, blockPos, blockState, player);
    }

    @Override
    public void fallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, double d) {
        super.fallOn(level, blockState, blockPos, entity, d);
        level.destroyBlock(blockPos, false);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new RavenEggBlockEntity(blockPos, blockState);
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return makeShape();
    }

    public VoxelShape makeShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.375, 0, 0.375, 0.625, 0.375, 0.625), BooleanOp.OR);

        return shape;
    }

    protected BlockState updateShape(BlockState blockState, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos blockPos2, BlockState blockState2, RandomSource randomSource) {
        return !blockState.canSurvive(levelReader, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, levelReader, scheduledTickAccess, blockPos, direction, blockPos2, blockState2, randomSource);
    }

    protected boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return !levelReader.isEmptyBlock(blockPos.below());
    }
}
