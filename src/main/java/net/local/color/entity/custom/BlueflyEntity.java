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
    private static final TrackedData<Byte> MOVESTATE;
    private static boolean WAIT = false;
    public static final int field_28638 = MathHelper.ceil(1.4959966F);
    int moveDelay = 0;
    int moveCondDelay = 0;

    // Initialize Bluefly
    public BlueflyEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 10, false);
        this.lookControl = new BlueflyLookControl(this);
    }
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(MOVESTATE, (byte)0);
        this.dataTracker.startTracking(SCARED, (byte)0);
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(SCARED, nbt.getByte("Scared"));
        this.dataTracker.set(MOVESTATE, nbt.getByte("Movestate"));
    }
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putByte("Scared", this.dataTracker.get(SCARED));
        nbt.putByte("Movestate", this.dataTracker.get(MOVESTATE));
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
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new FleeEntityGoal(this, FrogEntity.class ,2.0F, 1, 1));
        this.goalSelector.add(2, new FleeEntityGoal(this, PlayerEntity.class, 1.0F, 1, 1));
        this.goalSelector.add(3, new BlueflyEntity.AvoidDaylightGoal(1.0));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1));
        this.goalSelector.add(6, new LookAroundGoal(this));
    }

    // Bluefly Custom Goals
    public static class BlueflyLookControl extends LookControl {
        BlueflyLookControl(MobEntity entity) { super(entity); }
        public void tick() {{ super.tick();}}
        protected boolean shouldStayHorizontal() {
            return true;
        }
    }
    class AvoidDaylightGoal extends EscapeSunlightGoal {
        private int timer = toGoalTicks(100);

        public AvoidDaylightGoal(double speed) {
            super(BlueflyEntity.this, speed);
        }

        public boolean canStart() {
            if (!BlueflyEntity.this.navigation.isIdle()) {
                if (BlueflyEntity.this.world.isThundering() && BlueflyEntity.this.world.isSkyVisible(this.mob.getBlockPos())) {
                    return this.targetShadedPos();
                } else if (this.timer > 0) {
                    --this.timer;
                    return false;
                } else {
                    this.timer = 100;
                    BlockPos blockPos = this.mob.getBlockPos();
                    return BlueflyEntity.this.world.isDay() && BlueflyEntity.this.world.isSkyVisible(blockPos) && !((ServerWorld)BlueflyEntity.this.world).isNearOccupiedPointOfInterest(blockPos) && this.targetShadedPos();
                }
            } else {
                return false;
            }
        }
        public void start() {
            super.start();
        }
    }

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
    public void checkPlayerScared () {
        if (!this.isScared()) {
            if (this.world.getClosestPlayer(CLOSE_PLAYER_PREDICATE, this) != null) {
                this.setScared(true);
            }
        } else {
            this.setScared(false);
        }
    }
    public boolean isGround () { return (this.dataTracker.get(MOVESTATE) & 1) != 0; }
    public void setGround () {
        BlockPos pos = this.getBlockPos().add(0.0,-0.51,0.0);
        if (!this.world.getBlockState(pos).isAir()) {
            if (this.getVelocity().getY() <= 0) {
                this.dataTracker.set(MOVESTATE, (byte) 1);
            }
        } else {
            this.dataTracker.set(MOVESTATE, (byte) -2);
        }
    }
    public void setMovementState () {
        if (this.isGround()) {
            if (moveDelay > 20) {
                if (moveCondDelay < (random.nextInt(200-100)+100)) {
                    this.setVelocity(this.getVelocity().multiply(1.0, 0.8,1.0));
                    if (this.getVelocity().getY() <= 0) {
                        this.setVelocity(this.getVelocity().getX(), -0.05, this.getVelocity().getZ());
                        moveCondDelay++;
                    }
                } else {
                    moveDelay = 0;
                    moveCondDelay = 0;
                }
            } else {
                moveDelay++;
            }
        } else {
            moveDelay = 0;
            moveCondDelay = 0;
        }
    }

    // Morse Code
    public boolean isBlink () {
        long time = world.getTimeOfDay() % 24000;
        if (!isScared()) {
            if ((time <= 1000 || time >= 13000)) {
                return true;
            } else return this.world.isRaining() || this.world.isThundering();
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
            this.checkPlayerScared();
        }
        this.setMovementState();
        super.mobTick();
    }
    public void tick() {
        this.setGround();
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
                    if (Objects.equals(morse, "dark")) {
                        WAIT = true;
                        event.getController().markNeedsReload();
                        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.colorfly.dark", false));
                    } else if (Objects.equals(morse, "wet")) {
                        WAIT = true;
                        event.getController().markNeedsReload();
                        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.colorfly.wet", false));
                    } else if (Objects.equals(morse, "storm")) {
                        WAIT = true;
                        event.getController().markNeedsReload();
                        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.colorfly.storm", false));
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

    // Custom Spawn Condition
    public static boolean canCustomSpawn(EntityType<BlueflyEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        int l = world.getLightLevel(pos);

        return l > 10 ? false : canMobSpawn(type, world, spawnReason, pos, random);
    }

    //Static Variables
    static {
        CAPTURE = Sets.newHashSet(Items.GLASS_BOTTLE);
        SCARED = DataTracker.registerData(BlueflyEntity.class, TrackedDataHandlerRegistry.BYTE);
        MOVESTATE = DataTracker.registerData(BlueflyEntity.class, TrackedDataHandlerRegistry.BYTE);
        CLOSE_PLAYER_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(1);
    }
}
