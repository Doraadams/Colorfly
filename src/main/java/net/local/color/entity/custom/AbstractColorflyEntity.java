package net.local.color.entity.custom;

// imported packages

import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
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
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import java.util.Iterator;
import java.util.Set;

//Colorfly Abstract
public abstract class AbstractColorflyEntity extends TameableEntity implements GeoEntity {
    private static final TargetPredicate CLOSE_PLAYER_PREDICATE;
    private static final TrackedData<Boolean> SCARED;
    protected static final Set<Item> CAPTURE;
    static Item BOTTLE = Items.GLASS_BOTTLE;
    int ticksSinceBlink;
    int ticksSinceScared;
    protected AbstractColorflyEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    // Default Attributes
    public static DefaultAttributeContainer.Builder setAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1);
    }

    // NBT Data
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SCARED, false);
    }
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(SCARED, nbt.getBoolean("Scared"));
    }
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Scared", this.dataTracker.get(SCARED));

    }

    // Animation Controller and Render Settings
    public void registerControllers(AnimatableManager<?> manager) {
        manager.addController(new AnimationController<>(this,"idle_controller", event -> PlayState.STOP)
                .triggerableAnim("idle", RawAnimation.begin().then("animation.colorfly.blink", Animation.LoopType.PLAY_ONCE)));
        manager.addController(new AnimationController<>(this,"dark_controller", event -> PlayState.STOP)
                .triggerableAnim("dark", RawAnimation.begin().then("animation.colorfly.blink", Animation.LoopType.PLAY_ONCE)));
        manager.addController(new AnimationController<>(this,"wet_controller", event -> PlayState.STOP)
                .triggerableAnim("wet", RawAnimation.begin().then("animation.colorfly.blink", Animation.LoopType.PLAY_ONCE)));
        manager.addController(new AnimationController<>(this,"storm_controller", event -> PlayState.STOP)
                .triggerableAnim("dark", RawAnimation.begin().then("animation.colorfly.blink", Animation.LoopType.PLAY_ONCE)));

    }
    public void blinkControl() {
        if (!this.isScared() && ticksSinceScared > 40) {
            switch (this.setMorse()) {
                case "dark":
                    triggerAnim("dark_controller", "dark");
                case "wet":
                    triggerAnim("wet_controller", "wet");
                case "storm":
                    triggerAnim("storm_controller", "storm");
                default:
                    triggerAnim("idle_controller", "idle");
            }
        }
    }
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public AnimatableInstanceCache getAnimatableInstanceCache() { return this.cache; }
    public boolean shouldRender(double distance) { return this.world.getClosestPlayer(this, 25) != null; }
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {return dimensions.height * 0.5F;}

    // Interaction Code
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!this.world.isClient && CAPTURE.contains(itemStack.getItem())) {
            itemStack.decrement(1);
            this.remove(RemovalReason.DISCARDED);
            player.giveItemStack(new ItemStack(BOTTLE,1));

            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    // Set & Check
    public String setMorse() {
        long time = world.getTimeOfDay() % 24000;
        String morse = " ";
        if ((time <= 1000 || time >= 13000)) {
            morse = "dark";
        } else if (this.world.isRaining()) {
            morse = "wet";
        } else if (this.world.isThundering()) {
            morse = "storm";
        }
        return morse;
    }
    public boolean canBlink () {
        long time = world.getTimeOfDay() % 24000;
        int l = WorldRenderer.getLightmapCoordinates(this.getWorld(), this.getBlockPos().up());
        if (!this.isScared()) {
            return (time <= 1000 || time >= 13000) || this.world.isRaining() || this.world.isThundering() || l <= 10;
        } else {
            return false;
        }
    }
    public boolean isScared() {
        if ((this.world.getClosestPlayer(CLOSE_PLAYER_PREDICATE, this) != null)) {
            this.dataTracker.set(SCARED, true);
            return true;
        } else {
            this.dataTracker.set(SCARED, false);
            return false;
        }
    }

    // Tick
    public void tick() { super.tick(); }
    public void mobTick() {
        super.mobTick();
        if (this.isScared()) {
            this.ticksSinceScared = 0;
        } else {
            ++this.ticksSinceScared;
        }
        if ((this.canBlink() && (this.random.nextInt(9) == 0)) && (this.ticksSinceBlink > 40)) {
            this.ticksSinceBlink = 0;
            this.blinkControl();
        } else {
            ++this.ticksSinceBlink;
        }
    }
    public void tickMovement() {
        super.tickMovement();
        if (this.world.getBlockState(this.getBlockPos()).getBlock() instanceof CobwebBlock) {
            this.setVelocity(this.getVelocity().multiply(0,0,0));
        }
    }

    // Custom Goals
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
    static class ColorflyLookControl extends LookControl {
        ColorflyLookControl(MobEntity entity) { super(entity); }
        public void tick() {{ super.tick();}}
        protected boolean shouldStayHorizontal() {
            return true;
        }
    }
    static class ColorFlyOntoOrganicGoal extends FlyGoal {
        public ColorFlyOntoOrganicGoal(PathAwareEntity pathAwareEntity, double d) {
            super(pathAwareEntity, d);
        }

        protected Vec3d getWanderTarget() {
            Vec3d vec3d = null;
            if (this.mob.isTouchingWater()) { vec3d = FuzzyTargeting.find(this.mob, 10, 10); }
            if (this.mob.getRandom().nextFloat() >= this.probability) { vec3d = this.locatePlantBlock(); }
            return vec3d == null ? super.getWanderTarget() : vec3d;
        }

        private Vec3d locatePlantBlock() {
            BlockPos blockPos = this.mob.getBlockPos();
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            BlockPos.Mutable mutable2 = new BlockPos.Mutable();
            Iterable<BlockPos> iterable = BlockPos.iterate(
                    MathHelper.floor(this.mob.getX() - 3.0), MathHelper.floor(this.mob.getY() - 3.0), MathHelper.floor(this.mob.getZ() - 3.0),
                    MathHelper.floor(this.mob.getX() + 3.0), MathHelper.floor(this.mob.getY() + 3.0), MathHelper.floor(this.mob.getZ() + 3.0));
            Iterator<BlockPos> var5 = iterable.iterator();
            BlockPos blockPos2;
            boolean bl;
            do {
                do {
                    if (!var5.hasNext()) { return null; }
                    blockPos2 = var5.next();
                } while(blockPos.equals(blockPos2));
                BlockState blockState = this.mob.world.getBlockState(mutable2.set(blockPos2, Direction.DOWN));
                bl = blockState.getBlock() instanceof LeavesBlock || blockState.getBlock() instanceof PlantBlock;
            } while(!bl || !this.mob.world.isAir(blockPos2) || !this.mob.world.isAir(mutable.set(blockPos2, Direction.UP)));

            double dX = blockPos2.getX() - this.mob.getX();
            double dY = blockPos2.getY() - this.mob.getY();
            double dZ = blockPos2.getZ() - this.mob.getZ();
            if (Math.sqrt((dX * dX) + (dY * dY) + (dZ * dZ)) >= 2) {
                return Vec3d.ofBottomCenter(blockPos2);
            } else {
                return null;
            }
        }
    }

    // Damage
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {return false;}
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            return super.damage(source, amount);
        }
    }

    // Entity Settings
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) { return null; }
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {}
    public boolean canBeLeashedBy(PlayerEntity player) { return false; }
    public boolean canAvoidTraps() { return true; }
    public int getXpToDrop() {
        return 0;
    }

    // Entity Collision Settings
    public boolean isPushable() { return true; }
    protected void pushAway(Entity entity) { if (entity instanceof GreenflyEntity || entity instanceof  BlueflyEntity) { super.pushAway(entity); } }
    protected void tickCramming() {}

    // Sound
    protected SoundEvent getAmbientSound() { return null; }
    protected float getSoundVolume() { return 0.0F; }

    //Static Variables
    static {
        CAPTURE = Sets.newHashSet(Items.GLASS_BOTTLE);
        SCARED = DataTracker.registerData(AbstractColorflyEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        CLOSE_PLAYER_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(1);
    }
}