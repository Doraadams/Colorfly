package net.local.color.entity.custom;

// imported packages

import com.google.common.collect.Sets;
import net.local.color.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;

//Bluefly class w/ animation call
public class BlueflyEntity extends TameableEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final Set<Item> CAPTURE;
    private static final TargetPredicate CLOSE_PLAYER_PREDICATE;
    private static final TrackedData<Byte> SCARED;
    private static boolean WAIT = false;
    public static final int field_28638 = MathHelper.ceil(1.4959966F);
    int I = this.random.nextInt(9);

    // Initialize Bluefly
    public BlueflyEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 10, false);
        this.lookControl = new BlueflyLookControl(this);
    }
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SCARED, (byte)0);
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(SCARED, nbt.getByte("Scared"));
    }
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putByte("Scared", this.dataTracker.get(SCARED));
    }
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.setSilent(true);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world) {
            public boolean isValidPosition(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }
            public void tick() {
                super.tick();
            }
        };
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(true);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }
    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) { return null; }

    // Bluefly Attributes
    public static DefaultAttributeContainer.Builder setAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.5)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15);
    }

    // Bluefly Goals
    @SuppressWarnings("rawtypes")
    protected void initGoals() {
        this.goalSelector.add(0, new EscapeDangerGoal(this, 1));
        this.goalSelector.add(1, new FleeEntityGoal(this, FrogEntity.class ,2.0F, 1, 1));
        this.goalSelector.add(1, new FleeEntityGoal(this, PlayerEntity.class, 1.0F, 1, 1));
        this.goalSelector.add(1, new EscapeSunlightGoal(this, 1));
        this.goalSelector.add(2, new SwimGoal(this));
        this.goalSelector.add(2, new AvoidSunlightGoal(this));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1));
    }

    // Bluefly Custom Goals
    public static class BlueflyLookControl extends LookControl {
        BlueflyLookControl(MobEntity entity) { super(entity); }
        public void tick() {{ super.tick();}}
        protected boolean shouldStayHorizontal() {
            return true;
        }
    }

    /*
    public class BlueflyFlyAroundGoal extends Goal {
        BlueflyFlyAroundGoal() {
            this.setControls(EnumSet.of(Control.MOVE));
        }
        public boolean canStart() {
            return BlueflyEntity.this.navigation.isIdle() && BlueflyEntity.this.random.nextInt(100) == 0;
        }
        public boolean shouldContinue() {
            return BlueflyEntity.this.navigation.isFollowingPath();
        }
        public void start() {
            Vec3d vec3d = this.getRandomLocation();
            if (vec3d != null) {
                BlueflyEntity.this.navigation.startMovingAlong(BlueflyEntity.this.navigation.findPathTo(new BlockPos(vec3d), 2), 0.5);
            }
        }
        @Nullable
        private Vec3d getRandomLocation() {
            Vec3d vec3d2;
            vec3d2 = BlueflyEntity.this.getRotationVec(0.0F);
            Vec3d vec3d3 = AboveGroundTargeting.find(BlueflyEntity.this, 8, 7, vec3d2.x, vec3d2.z, 1.5707964F, 3, 1);
            return vec3d3 != null ? vec3d3 : NoPenaltySolidTargeting.find(BlueflyEntity.this, 8, 4, -2, vec3d2.x, vec3d2.z, 1.5707963705062866);
        }
    }

    private abstract class NotScaredGoal extends Goal {
        NotScaredGoal() {
        }

        public abstract boolean canBlueflyStart();

        public abstract boolean canBlueflyContinue();

        public boolean canStart() {
            return this.canBlueflyStart() && !BlueflyEntity.this.isScared();
        }

        public boolean shouldContinue() {
            return this.canBlueflyContinue() && !BlueflyEntity.this.isScared();
        }
    }
    */

    // Set & Check
    public boolean isScared () { return (this.dataTracker.get(SCARED) & 1) != 0; }
    public void setScared (boolean safe) {
        byte b = this.dataTracker.get(SCARED);
        if (safe) {
            this.dataTracker.set(SCARED, (byte)(b | 1));
        } else {
            this.dataTracker.set(SCARED, (byte)(b & -2));
        }
    }
    public void setPlayerScared () {
        BlockPos blockPos = this.getBlockPos();

        if (!this.isScared()) {
            boolean bl1 = this.isSilent();
            if (this.world.getClosestPlayer(CLOSE_PLAYER_PREDICATE, this) != null) {
                this.setScared(true);
                if (!bl1) {
                    this.world.syncWorldEvent(null, 1025, blockPos, 0);
                }
            }
        } else {
            this.setScared(false);
        }
    }
    public void setMovementState() {
        if (this.isOnGround()) {
            if (I < 100) {
                if (I > 10) {
                    this.setVelocity(this.getVelocity().multiply(1.0, 0.0, 1.0));
                }
                I++;
            } else {
                I = 0;
            }
        } else {
            this.setVelocity(this.getVelocity().multiply(1.0, 0.8, 1.0));
            I++;
        }
    }

    // Morse Code
    public boolean isBlink () {
        long time = world.getTimeOfDay() % 24000;
        if (!isScared()) {
            if ((time <= 1000 || time >= 13000)) {
                return true;
            } else if (this.world.isRaining() || this.world.isThundering()) {
                return true;
            }
        }
        return false;
    }
    public String setMorse() {
        long time = world.getTimeOfDay() % 24000;
        String morse = " ";
        if ((time <= 1000 || time >= 13000))
            morse = "dark";
        if (this.world.isRaining()) {
            morse = "wet";
        }
        if (this.world.isThundering()) {
            morse = "storm";
        }
        return morse;
    }

    // Tick and Movement Code
    public void mobTick() {
        if (!this.isScared() && this.random.nextInt(19) == 0){
            this.setPlayerScared();
        }
        this.setMovementState();
        super.mobTick();
    }
    public void tick() {
        super.tick();
    }
    public void tickMovement() {
        super.tickMovement();
    }

    // Animation code
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.colorfly.idle", true));
        return PlayState.CONTINUE;
    }
    private PlayState blinkPredicate(@SuppressWarnings("rawtypes") AnimationEvent event) {
        if (event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            int rand = this.random.nextInt(3);
            if (rand == 0 && !WAIT) {
                if (this.isBlink()) {
                    String morse = this.setMorse();
                    System.out.println(morse);
                    if (Objects.equals(morse, "dark")) {
                        WAIT = true;
                        event.getController().markNeedsReload();
                        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.colorfly.dark", false));
                    } else if (Objects.equals(morse, "wet")) {
                        WAIT = true;
                        event.getController().markNeedsReload();
                        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.colorfly.dark", false));
                    } else if (Objects.equals(morse, "storm")) {
                        WAIT = true;
                        event.getController().markNeedsReload();
                        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.colorfly.dark", false));
                    }
                }
            }
            if (rand==1) {
                WAIT = false;
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
    public AnimationFactory getFactory() { return factory; }

    // Interaction Code
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!this.world.isClient && CAPTURE.contains(itemStack.getItem())) {
            itemStack.decrement(1);
            this.remove(RemovalReason.DISCARDED);
            player.giveItemStack(new ItemStack(ModItems.BLUEFLY_BOTTLE,1));

            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    // Damage
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {return false;}
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {}

    // Entity Settings
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {return dimensions.height * 0.1F;}
    public boolean hasWings() { return this.isInAir() && this.age % field_28638 == 0; }
    public boolean isInAir() { return !this.onGround; }
    protected void tickCramming() {}
    public boolean isPushable() {return false;}
    public boolean canBeLeashedBy(PlayerEntity player) { return false; }

    // Sound
    protected float getSoundVolume() { return 0.0F; }
    protected SoundEvent getAmbientSound() { return null; }
    //protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.ENTITY_BEE_HURT; }
    //protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_BEE_DEATH; }

    // Custom Spawn Condition
    public static boolean canSpawn(EntityType<BlueflyEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        int l = world.getLightLevel(pos);

        return l > 10 ? false : canMobSpawn(type, world, spawnReason, pos, random);
    }

    //Static Variables
    static {
        CAPTURE = Sets.newHashSet(Items.GLASS_BOTTLE);
        SCARED = DataTracker.registerData(BlueflyEntity.class, TrackedDataHandlerRegistry.BYTE);
        CLOSE_PLAYER_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(1);
    }
}
