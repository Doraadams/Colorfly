package net.local.color.entity.custom;

// imported packages
import com.google.common.collect.Sets;
import net.local.color.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

//Bluefly class w/ animation call
public class BlueflyEntity extends TameableEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final Set<Item> TAMING_INGREDIENTS;
    private static final TargetPredicate CLOSE_PLAYER_PREDICATE;
    private static final TrackedData<Byte> CHILL;
    private static final TrackedData<Byte> GENDER;
    private static boolean WAIT = false;

    // Initialize Bluefly
    public BlueflyEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 10, false);
    }
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(CHILL, (byte)0);
        this.dataTracker.startTracking(GENDER, (byte)0);
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(CHILL, nbt.getByte("Chill"));
        this.dataTracker.set(GENDER, nbt.getByte("Gender"));
    }
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putByte("Chill", this.dataTracker.get(CHILL));
        nbt.putByte("Gender", this.dataTracker.get(GENDER));
    }
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty,
                                 SpawnReason spawnReason, @Nullable EntityData entityData,
                                 @Nullable NbtCompound entityNbt) {
        int nxt = Random.create().nextInt(9);
        this.setSilent(true);
        if ( nxt > 0) {
            if (this.random.nextInt(9) >= 5) {
                this.setNob(true);
            } else {
                this.setNob(false);
            }
        }

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }
    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    // Bluefly Attributes
    public static DefaultAttributeContainer.Builder setAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 1.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15);
    }

    // Bluefly Goals
    @SuppressWarnings("rawtypes")
    protected void initGoals() {
        this.goalSelector.add(0, new EscapeDangerGoal(this, 1));
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new BlueflyEntity.LookAtTargetGoal(this));
        this.goalSelector.add(2, new FleeEntityGoal(this, FrogEntity.class, 3.0F, 1, 1));
        this.goalSelector.add(2, new FleeEntityGoal(this, PlayerEntity.class, 3.0F, 1, 1));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.5));
        this.goalSelector.add(3, new FlyGoal(this, 1));
        this.goalSelector.add(5, new BlueflyEntity.SafeGoal(this, 1));
    }

    // Bluefly Custom Goals
    static class LookAtTargetGoal extends Goal {
        private final BlueflyEntity bluefly;

        public LookAtTargetGoal(BlueflyEntity bluefly) {
            this.bluefly = bluefly;
            this.setControls(EnumSet.of(Control.LOOK));
        }

        @Override
        public boolean canStart() {
            return true;
        }

        public boolean shouldRunEveryTick() {
            return false;
        }

        public void tick() {
            if (this.bluefly.getTarget() == null) {
                Vec3d vec3d = this.bluefly.getVelocity();
                this.bluefly.setYaw(-((float)MathHelper.atan2(vec3d.y, vec3d.z)) * 57.295776F);
                this.bluefly.bodyYaw = this.bluefly.getYaw();
            } else {
                LivingEntity livingEntity = this.bluefly.getTarget();
                if (livingEntity.squaredDistanceTo(this.bluefly) < 4096.0) {
                    double e = livingEntity.getX() - this.bluefly.getX();
                    double f = livingEntity.getZ() - this.bluefly.getZ();
                    this.bluefly.setYaw(-((float)MathHelper.atan2(e, f)) * 57.295776F);
                    this.bluefly.bodyYaw = this.bluefly.getYaw();
                }
            }

        }
    }
    public static class SafeGoal extends FlyGoal {
        public SafeGoal (PathAwareEntity pathAwareEntity, double d) {
            super(pathAwareEntity, d);
        }
        @Nullable
        protected Vec3d getWanderTarget() {
            Vec3d vec3d = null;
            if (this.mob.isTouchingWater()) {
                vec3d = FuzzyTargeting.find(this.mob, 15, 15);
                return vec3d == null ? super.getWanderTarget() : vec3d;
            }
            if (Random.create().nextInt(9) == 0) {
                vec3d = this.locateSafeSpace();
            }
            return vec3d == null ? super.getWanderTarget() : vec3d;
        }

        @Nullable
        private Vec3d locateSafeSpace() {
            BlockPos blockPos = this.mob.getBlockPos();
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            BlockPos.Mutable mutable2 = new BlockPos.Mutable();
            Iterable<BlockPos> iterable = BlockPos.iterate(MathHelper.floor(this.mob.getX() - 3.0), MathHelper.floor(this.mob.getY() - 6.0), MathHelper.floor(this.mob.getZ() - 3.0), MathHelper.floor(this.mob.getX() + 3.0), MathHelper.floor(this.mob.getY() + 6.0), MathHelper.floor(this.mob.getZ() + 3.0));
            Iterator var5 = iterable.iterator();
            BlockPos blockPos2;
            boolean bl;
            do {
                do {
                    if (!var5.hasNext()) {
                        return null;
                    }
                    blockPos2 = (BlockPos)var5.next();
                } while(blockPos.equals(blockPos2));
                BlockState blockState = this.mob.world.getBlockState(mutable2.set(blockPos2, Direction.DOWN));
                bl = blockState.getBlock() instanceof FernBlock || blockState.getBlock() instanceof FlowerBlock || blockState.getBlock() instanceof TallPlantBlock || blockState.getBlock() instanceof TallFlowerBlock;
            } while(!bl || !this.mob.world.isAir(blockPos2) || !this.mob.world.isAir(mutable.set(blockPos2, Direction.UP)));
            return Vec3d.ofCenter(blockPos2);
        }
    }

    // Set & Check DataTracker
    public boolean isChill () { return (this.dataTracker.get(CHILL) & 1) != 0; }
    public void setChill (boolean safe) {
        byte b = this.dataTracker.get(CHILL);
        if (safe) {
            this.dataTracker.set(CHILL, (byte)(b | 1));
        } else {
            this.dataTracker.set(CHILL, (byte)(b & -2));
        }
    }
    public boolean isNob () {return (this.dataTracker.get(GENDER) & 1) !=0; }
    public void setNob (boolean nob) {
        byte b = this.dataTracker.get(GENDER);
        if (nob) {
            this.dataTracker.set(GENDER, (byte)(b | 1));
        } else {
            this.dataTracker.set(GENDER, (byte)(b & -2));
        }
    }

    // Tick Code (Custom Gravity + Other)
    public void mobTick() {
        super.mobTick();
        BlockPos blockPos = this.getBlockPos();
        BlockPos blockPos2 = blockPos.up();
        BlockPos blockPos3 = blockPos.down();
        if (this.world.isAir(blockPos) || this.world.isAir(blockPos2) || this.world.isAir(blockPos3)) {
            this.setVelocity(this.getVelocity().multiply(1.0, 0.6, 1.0));
        }

        if (this.isChill()) {
            boolean bl1 = this.isSilent();
            if (this.world.getClosestPlayer(CLOSE_PLAYER_PREDICATE, this) != null) {
                this.setChill(false);
                if (!bl1) {
                    this.world.syncWorldEvent(null, 1025, blockPos, 0);
                }
            }
        } else {
            if (this.random.nextInt(10) >= 5) {
                this.setChill(true);
            }
        }
    }

    // Animation code
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.colorfly.idle", true));
        return PlayState.CONTINUE;
    }
    private PlayState blinkPredicate(@SuppressWarnings("rawtypes") AnimationEvent event) {
        if (!this.isNob() && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            if (this.world.getClosestPlayer(CLOSE_PLAYER_PREDICATE, this) != null) { return PlayState.CONTINUE; }
            else {
                if (this.random.nextInt(9) >= 5 && !WAIT) {
                    event.getController().markNeedsReload();
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.colorfly.dot", false));
                    WAIT = true;

                } else {
                    WAIT = false;
                }
            }
        } else if (this.isNob() && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            if (this.world.getClosestPlayer(CLOSE_PLAYER_PREDICATE, this) != null) { return PlayState.CONTINUE; }
            else {
                if (this.random.nextInt(9) >= 5) {
                    event.getController().markNeedsReload();
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.colorfly.dash", false));
                    WAIT = true;
                } else {
                    WAIT = false;
                }
            }
        }
        return PlayState.CONTINUE;
    }
    @SuppressWarnings("rawtypes")
    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this,"controller",5,this::predicate));
        animationData.addAnimationController(new AnimationController(this,"blinkController",5,this::blinkPredicate));
    }
    public AnimationFactory getFactory() {
        return factory;
    }

    // Interaction Code
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!this.world.isClient && TAMING_INGREDIENTS.contains(itemStack.getItem())) {
            itemStack.decrement(1);
            this.remove(Entity.RemovalReason.DISCARDED);
            player.giveItemStack(new ItemStack(ModItems.BLUEFLY_BOTTLE,1));

            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    // Extra Options or Code
    public boolean canBeLeashedBy(PlayerEntity player) { return false; }
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {return false;}
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {}
    public boolean isPushable() {return false;}
    protected void tickCramming() {}
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {return dimensions.height * 0.1F;}

    public static boolean canSpawn(EntityType<BlueflyEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        int i = world.getLightLevel(pos);

        return i > 10 ? false : canMobSpawn(type, world, spawnReason, pos, random);
    }

    //Static Variables
    static {
        TAMING_INGREDIENTS = Sets.newHashSet(Items.GLASS_BOTTLE);
        GENDER = DataTracker.registerData(BlueflyEntity.class, TrackedDataHandlerRegistry.BYTE);
        CHILL = DataTracker.registerData(BlueflyEntity.class, TrackedDataHandlerRegistry.BYTE);
        CLOSE_PLAYER_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(4);
    }
}
