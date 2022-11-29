package net.local.color.entity.custom;

// imported packages

import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

//Colorfly Abstract
public abstract class AbstractColorflyEntity extends TameableEntity {
    private static final TargetPredicate CLOSE_PLAYER_PREDICATE;
    private static final TrackedData<Byte> SCARED;
    private static final TrackedData<Byte> STATE;
    private static final TrackedData<Byte> WANT;
    protected static final Set<Item> CAPTURE;

    public AbstractColorflyEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    // Colorfly DataTracker & NBT
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(STATE, (byte)0);
        this.dataTracker.startTracking(SCARED, (byte)0);
        this.dataTracker.startTracking(WANT, (byte)0);
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

    // Colorfly Attributes
    public static DefaultAttributeContainer.Builder setAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1);
    }

    // Colorfly Default Goals
    protected void initGoals() {
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new FleeEntityGoal<>(this, FrogEntity.class ,2.0F, 1, 1));
        this.goalSelector.add(2, new FleeEntityGoal<>(this, PlayerEntity.class, 1.0F, 1, 1));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(3, new AbstractColorflyEntity.AvoidDaylightGoal(1.0));
        this.goalSelector.add(6, new LookAroundGoal(this));
    }

    // Colorfly Custom Goals
    class AvoidDaylightGoal extends EscapeSunlightGoal {
        private int timer = toGoalTicks(100);

        public AvoidDaylightGoal(double speed) {
            super(AbstractColorflyEntity.this, speed);
        }

        public boolean canStart() {
            if (!AbstractColorflyEntity.this.navigation.isIdle()) {
                if (AbstractColorflyEntity.this.world.isThundering() && AbstractColorflyEntity.this.world.isSkyVisible(this.mob.getBlockPos())) {
                    return this.targetShadedPos();
                } else if (this.timer > 0) {
                    --this.timer;
                    return false;
                } else {
                    this.timer = 100;
                    BlockPos blockPos = this.mob.getBlockPos();
                    return AbstractColorflyEntity.this.world.isDay() && AbstractColorflyEntity.this.world.isSkyVisible(blockPos) && !((ServerWorld) AbstractColorflyEntity.this.world).isNearOccupiedPointOfInterest(blockPos) && this.targetShadedPos();
                }
            } else {
                return false;
            }
        }
        public void start() {
            super.start();
        }
    }

    // Colorfly Set & Check
    public boolean isScared () {
        return (this.dataTracker.get(SCARED) & 1) != 0;
    }
    public void setScared (boolean safe) {
        byte b = this.dataTracker.get(SCARED);
        if (safe || this.world.getClosestPlayer(CLOSE_PLAYER_PREDICATE, this) != null) {
            if (this.random.nextInt(20) == 0) {
                this.dataTracker.set(SCARED, (byte)(b | 1));
            }
        } else {
            this.dataTracker.set(SCARED, (byte)(b & -2));
        }
    }
    //public boolean isGround () { return (this.dataTracker.get(STATE) & 1) != 0; }
    public void setGround () {
        byte b = this.dataTracker.get(STATE);
        if (!this.world.getBlockState(this.getBlockPos().down().add(0.0, 0.0, 0.0)).isAir()) {
            if (this.getVelocity().getY() == 0) {
                this.dataTracker.set(STATE, (byte) (b | 1));
            }
        } else {
            this.dataTracker.set(STATE, (byte) (b | -2));
        }
    }
    public boolean isWant () {
        return (this.dataTracker.get(WANT) & 1) != 0;
    }
    public void setWant (boolean want) {
        byte b = this.dataTracker.get(WANT);
        if (want) {
            this.dataTracker.set(WANT, (byte)(b | 1));
        } else {
            this.dataTracker.set(WANT, (byte)(b & -2));
        }
    }

    // Tick
    public void mobTick() {super.mobTick();}
    public void tick() {super.tick();}
    public void tickMovement() {super.tickMovement();}

    // Damage
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {return false;}
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {}

    // Entity Settings
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {return dimensions.height * 0.1F;}
    public boolean hasWings() { return !this.onGround; }
    protected void tickCramming() {}
    public boolean isPushable() {return false;}
    public boolean canBeLeashedBy(PlayerEntity player) { return false; }

    // Sound
    protected float getSoundVolume() { return 0.0F; }
    protected SoundEvent getAmbientSound() { return null; }

    //Static Variables
    static {
        CAPTURE = Sets.newHashSet(Items.GLASS_BOTTLE);
        SCARED = DataTracker.registerData(AbstractColorflyEntity.class, TrackedDataHandlerRegistry.BYTE);
        STATE = DataTracker.registerData(AbstractColorflyEntity.class, TrackedDataHandlerRegistry.BYTE);
        WANT = DataTracker.registerData(AbstractColorflyEntity.class, TrackedDataHandlerRegistry.BYTE);
        CLOSE_PLAYER_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(1);
    }
}