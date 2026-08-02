package net.enderboy.ravenmail.entity;

import net.enderboy.ravenmail.RavenMailContent;
import net.enderboy.ravenmail.entity.goal.*;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.entity.variant.VariantUtils;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class Raven extends TamableAnimal implements InventoryCarrier, FlyingAnimal {
    private static final EntityDimensions BABY_DIMENSIONS = RavenMailContent.RAVEN.getDimensions().scale(0.7F).withEyeHeight(0.24F);
    private final SimpleContainer inventory = new SimpleContainer(1);
    private static final ResourceKey<RavenVariant> DEFAULT_VARIANT;
    private static final EntityDataAccessor<Holder<RavenVariant>> DATA_VARIANT_ID;
    public static final EntityDataAccessor<ItemStack> HELD_ITEM = SynchedEntityData.defineId(Raven.class, EntityDataSerializers.ITEM_STACK);

    public Raven(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    public Holder<RavenVariant> getVariant() {
        return this.entityData.get(DATA_VARIANT_ID);
    }

    private void setVariant(Holder<RavenVariant> variantHolder) {
        this.entityData.set(DATA_VARIANT_ID, variantHolder);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TamableAnimal.TamableAnimalPanicGoal(1.25));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new RandomSwimmingGoal(this,1,1));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 2, true));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1, (itemStack) -> itemStack.is(RavenMailContent.RAVEN_ATTRACTED_TO), false));
        this.goalSelector.addGoal(2, new RavenDeliverGoal(this));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1, 5, 1));
        this.goalSelector.addGoal(2, new RavenWanderGoal(this, 1));
        this.goalSelector.addGoal(0, new BreedGoal(this, 1));
        this.goalSelector.addGoal(1, new SearchAndCollectItemGoal(this));
        this.goalSelector.addGoal(2, new AttackPestGoal(this, Silverfish.class));
        this.goalSelector.addGoal(3, new FollowMobGoal(this, 1, 3, 7));
    }

    @Override
    public boolean shouldTryTeleportToOwner() {
        return !inventory.getItem(0).is(ItemTags.BUNDLES) && distanceTo(Objects.requireNonNull(getOwner())) > 15;
    }

    @Override
    public void tick() {
        super.tick();
        AABB callToAttackSearchBox = new AABB(blockPosition()).inflate(8);
        if (level().getDifficulty().getId() >= 2) {
            if (getLastHurtByMob() != null && getOwner() == null && !isBaby()) {
                setTarget(getLastHurtByMob());
                for (Raven raven : level().getEntitiesOfClass(Raven.class, callToAttackSearchBox)) {
                    if (!raven.isBaby() && raven.getOwner() == null) raven.setTarget(this.getTarget());
                }
            }
        }
        if (entityData.get(HELD_ITEM).isEmpty() && !inventory.isEmpty()) entityData.set(HELD_ITEM, inventory.getItem(0).copy());
    }

    @Override
    protected int calculateFallDamage(double d, float f) {
        return 0;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HELD_ITEM, ItemStack.EMPTY);
        builder.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(this.registryAccess(), DEFAULT_VARIANT));
    }

    @Override
    public <T> @Nullable T get(DataComponentType<? extends T> dataComponentType) {
        return castComponentValue(dataComponentType, this.getVariant());
    }

    protected void applyImplicitComponents(DataComponentGetter dataComponentGetter) {
        this.applyImplicitComponentIfPresent(dataComponentGetter, RavenMailContent.RAVEN_VARIANT_COMPONENT);
        super.applyImplicitComponents(dataComponentGetter);
    }

    protected <T> boolean applyImplicitComponent(DataComponentType<T> dataComponentType, T object) {
        if (dataComponentType == RavenMailContent.RAVEN_VARIANT_COMPONENT) {
            this.setVariant(castComponentValue(RavenMailContent.RAVEN_VARIANT_COMPONENT, object));
            return true;
        } else {
            return super.applyImplicitComponent(dataComponentType, object);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.FLYING_SPEED, 0.4F)
                .add(Attributes.MOVEMENT_SPEED, 0.2F)
                .add(Attributes.ATTACK_DAMAGE, 1.0);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, level);
        flyingPathNavigation.setCanOpenDoors(false);
        flyingPathNavigation.setCanFloat(true);
        return flyingPathNavigation;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        this.writeInventoryToTag(valueOutput);
        VariantUtils.writeVariant(valueOutput, this.getVariant());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        VariantUtils.readVariant(valueInput, RavenMailContent.RAVEN_VARIANT).ifPresent(this::setVariant);
        this.readInventoryFromTag(valueInput);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        Vec3 deltaMovement = this.getDeltaMovement();
        if (!this.onGround() && deltaMovement.y < (double)0.0F) {
            this.setDeltaMovement(deltaMovement.multiply((double)1.0F, 0.6, (double)1.0F));
        }
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected void doPush(Entity entity) {
        if (!(entity instanceof Player)) {
            super.doPush(entity);
        }
    }

    @Override
    public @NonNull ItemStack getMainHandItem() {
        return entityData.get(HELD_ITEM).copy();
    }

    public void giveOrTake(Player player, ItemStack stack) {
        if (!stack.isEmpty() && !stack.is(RavenMailContent.CAN_BE_GIVEN_TO_RAVEN)) return;
        if (getInventory().isEmpty() && !stack.isEmpty()) {
            inventory.setItem(0, stack.copyWithCount(1));
            entityData.set(HELD_ITEM, inventory.getItem(0).copy());
            stack.shrink(1);
        } else if (!inventory.isEmpty() && stack.isEmpty()) {
            player.setItemInHand(InteractionHand.MAIN_HAND, inventory.getItem(0).copyWithCount(1));
            inventory.getItem(0).shrink(1);
            entityData.set(HELD_ITEM, ItemStack.EMPTY);
        }
    }

    private void tryToTame(Player player) {
        if (this.random.nextInt(3) == 0) {
            this.tame(player);
            this.navigation.stop();
            this.level().broadcastEntityEvent(this, (byte)7);
        } else {
            this.level().broadcastEntityEvent(this, (byte)6);
        }

        this.playEatingSound();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (this.isTame() && this.getOwner() != null && player == this.getOwner() && player.isShiftKeyDown()) {
            this.getNavigation().stop();
            this.setOrderedToSit(!this.isInSittingPose());
            return InteractionResult.SUCCESS;
        }

        if (this.isInSittingPose()) {
            this.clearMovementThisTick();
        }

        if (this.isBaby()) {
            return super.mobInteract(player, interactionHand);
        } else {
            if (!itemStack.isEmpty()) {
                if (!this.level().isClientSide() && !this.isTame() && this.isFood(itemStack)) {
                    this.usePlayerItem(player, interactionHand, itemStack);
                    this.tryToTame(player);
                    return InteractionResult.SUCCESS_SERVER;
                }

                if (this.isFood(itemStack) && this.getHealth() < this.getMaxHealth()) {
                    FoodProperties foodProperties = itemStack.get(DataComponents.FOOD);
                    this.heal(foodProperties != null ? 2 * foodProperties.nutrition() : 1.0F);
                    this.usePlayerItem(player, interactionHand, itemStack);
                    this.playEatingSound();
                    return InteractionResult.SUCCESS;
                }

                InteractionResult interactionResult = itemStack.interactLivingEntity(player, this, interactionHand);
                if (interactionResult.consumesAction()) {
                    return interactionResult;
                }
            }

            if (this.isTame() && !player.isSecondaryUseActive() && !itemStack.is(RavenMailContent.RAVEN_FOOD)) {
                this.giveOrTake(player, itemStack);
                return InteractionResult.SUCCESS;
            } else {
                return super.mobInteract(player, interactionHand);
            }
        }
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return RavenMailContent.RAVEN_AMBIENCE;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return RavenMailContent.RAVEN_HURT;
    }

    @Override
    protected void dropAllDeathLoot(ServerLevel serverLevel, DamageSource damageSource) {
        drop(inventory.getItem(0).copy(), false, false);
        super.dropAllDeathLoot(serverLevel, damageSource);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float f) {
        if (this.isInvulnerableTo(serverLevel, damageSource)) {
            return false;
        } else {
            this.setOrderedToSit(false);
            return super.hurtServer(serverLevel, damageSource, f);
        }
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(RavenMailContent.RAVEN_FOOD);
    }

    @Nullable
    public Raven getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        Raven raven = RavenMailContent.RAVEN.create(serverLevel, EntitySpawnReason.BREEDING);
        if (raven != null && ageableMob instanceof Raven raven2) {
            int mutationChance = random.nextInt(0,9) + 1;
            if (mutationChance == 4) {
                VariantUtils.selectVariantToSpawn(SpawnContext.create(serverLevel, this.blockPosition()), RavenMailContent.RAVEN_VARIANT).ifPresent(raven::setVariant);
            } else {
                if (this.random.nextBoolean()) {
                    raven.setVariant(this.getVariant());
                } else {
                    raven.setVariant(raven2.getVariant());
                }
            }
        }
        if (raven != null && this.isTame()) {
                raven.setOwnerReference(this.getOwnerReference());
                raven.setTame(true, true);
        }

        return raven;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, EntitySpawnReason entitySpawnReason, @Nullable SpawnGroupData spawnGroupData) {
        spawnGroupData = super.finalizeSpawn(serverLevelAccessor, difficultyInstance, entitySpawnReason, spawnGroupData);
        VariantUtils.selectVariantToSpawn(SpawnContext.create(serverLevelAccessor, this.blockPosition()), RavenMailContent.RAVEN_VARIANT).ifPresent(this::setVariant);
        return spawnGroupData;
    }

    @Override
    public SimpleContainer getInventory() {
        return inventory;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    static {
        DATA_VARIANT_ID = SynchedEntityData.defineId(Raven.class, RavenMailContent.RAVEN_VARIANT_DATA);
        DEFAULT_VARIANT = RavenVariants.BLACK;
    }
}
