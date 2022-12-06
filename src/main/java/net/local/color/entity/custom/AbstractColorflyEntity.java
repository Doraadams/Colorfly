package net.local.color.entity.custom;

import net.minecraft.block.*;
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
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.compress.utils.Sets;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

// Colorfly Abstract
@SuppressWarnings("EmptyMethod")
public abstract class AbstractColorflyEntity extends TameableEntity implements GeoEntity {
    private static final TargetPredicate CLOSE_PLAYER_PREDICATE;
    private static final TargetPredicate CLOSE_ENTITY_PREDICATE;
    private static final TrackedData<Boolean> SCARED;
    protected static final Set<Item> CAPTURE;
    protected int ticksAnimDelay;
    protected int ticksSinceScared;
    private String morse;

    protected AbstractColorflyEntity(EntityType<? extends TameableEntity> entityType, World world) { super(entityType, world); }

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

    // Animation Controller & Render Settings
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public AnimatableInstanceCache getAnimatableInstanceCache() { return this.cache; }

    public void registerControllers(AnimatableManager<?> manager) {
        manager.addController(new AnimationController<>(this, event -> {
            event.getController().setAnimation(RawAnimation.begin().then("animation.colorfly.idle", Animation.LoopType.PLAY_ONCE));
            return PlayState.CONTINUE;
        }));
        manager.addController(new AnimationController<>(this, "sos_controller", event -> PlayState.STOP)
                .triggerableAnim("sos", RawAnimation.begin().then("animation.colorfly.sos", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "human_controller", event -> PlayState.STOP)
                .triggerableAnim("human", RawAnimation.begin().then("animation.colorfly.human", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "dark_controller", event -> PlayState.STOP)
                .triggerableAnim("dark", RawAnimation.begin().then("animation.colorfly.dark", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "wet_controller", event -> PlayState.STOP)
                .triggerableAnim("wet", RawAnimation.begin().then("animation.colorfly.wet", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "storm_controller", event -> PlayState.STOP)
                .triggerableAnim("storm", RawAnimation.begin().then("animation.colorfly.storm", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "fairy_controller", event -> PlayState.STOP)
                .triggerableAnim("fairy", RawAnimation.begin().then("animation.colorfly.fairy", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "bat_controller", event -> PlayState.STOP)
                .triggerableAnim("bat", RawAnimation.begin().then("animation.colorfly.bat", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "camel_controller", event -> PlayState.STOP)
                .triggerableAnim("camel", RawAnimation.begin().then("animation.colorfly.camel", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "cat_controller", event -> PlayState.STOP)
                .triggerableAnim("cat", RawAnimation.begin().then("animation.colorfly.cat", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "fowl_controller", event -> PlayState.STOP)
                .triggerableAnim("fowl", RawAnimation.begin().then("animation.colorfly.fowl", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "cow_controller", event -> PlayState.STOP)
                .triggerableAnim("cow", RawAnimation.begin().then("animation.colorfly.cow", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "fox_controller", event -> PlayState.STOP)
                .triggerableAnim("fox", RawAnimation.begin().then("animation.colorfly.fox", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "frog_controller", event -> PlayState.STOP)
                .triggerableAnim("frog", RawAnimation.begin().then("animation.colorfly.frog", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "steed_controller", event -> PlayState.STOP)
                .triggerableAnim("steed", RawAnimation.begin().then("animation.colorfly.steed", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "mule_controller", event -> PlayState.STOP)
                .triggerableAnim("mule", RawAnimation.begin().then("animation.colorfly.mule", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "bird_controller", event -> PlayState.STOP)
                .triggerableAnim("bird", RawAnimation.begin().then("animation.colorfly.bird", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "pig_controller", event -> PlayState.STOP)
                .triggerableAnim("pig", RawAnimation.begin().then("animation.colorfly.pig", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "hare_controller", event -> PlayState.STOP)
                .triggerableAnim("hare", RawAnimation.begin().then("animation.colorfly.hare", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "sheep_controller", event -> PlayState.STOP)
                .triggerableAnim("sheep", RawAnimation.begin().then("animation.colorfly.sheep", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "frosty_controller", event -> PlayState.STOP)
                .triggerableAnim("frosty", RawAnimation.begin().then("animation.colorfly.frosty", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "crush_controller", event -> PlayState.STOP)
                .triggerableAnim("crush", RawAnimation.begin().then("animation.colorfly.crush", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "squid_controller", event -> PlayState.STOP)
                .triggerableAnim("squid", RawAnimation.begin().then("animation.colorfly.squid", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "bee_controller", event -> PlayState.STOP)
                .triggerableAnim("bee", RawAnimation.begin().then("animation.colorfly.bee", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "goat_controller", event -> PlayState.STOP)
                .triggerableAnim("goat", RawAnimation.begin().then("animation.colorfly.goat", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "fe_controller", event -> PlayState.STOP)
                .triggerableAnim("fe", RawAnimation.begin().then("animation.colorfly.fe", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "llama_controller", event -> PlayState.STOP)
                .triggerableAnim("llama", RawAnimation.begin().then("animation.colorfly.llama", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "po_controller", event -> PlayState.STOP)
                .triggerableAnim("po", RawAnimation.begin().then("animation.colorfly.po", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "bear_controller", event -> PlayState.STOP)
                .triggerableAnim("bear", RawAnimation.begin().then("animation.colorfly.bear", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "wolf_controller", event -> PlayState.STOP)
                .triggerableAnim("wolf", RawAnimation.begin().then("animation.colorfly.wolf", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
        manager.addController(new AnimationController<>(this, "glow_controller", event -> PlayState.STOP)
                .triggerableAnim("glow", RawAnimation.begin().then("animation.colorfly.glow", Animation.LoopType.PLAY_ONCE)).setAnimationSpeed(1.25));
    }
    public void blinkController() {
        switch (this.setBlink()) {
            case "SOS" -> {
                triggerAnim("sos_controller", "sos");
                this.ticksAnimDelay = 440;
            }
            case "Human" -> {
                triggerAnim("human_controller", "human");
                this.ticksAnimDelay = 680;
            }
            case "Dark" -> {
                triggerAnim("dark_controller", "dark");
                this.ticksAnimDelay = 580;
            }
            case "Wet" -> {
                triggerAnim("wet_controller", "wet");
                this.ticksAnimDelay = 280;
            }
            case "Storm" -> {
                triggerAnim("storm_controller", "storm");
                this.ticksAnimDelay = 680;
            }
            case "Allay" -> {
                triggerAnim("fairy_controller", "fairy");
                this.ticksAnimDelay = 780;
            }
            case "Bat" -> {
                triggerAnim("bat_controller", "bat");
                this.ticksAnimDelay = 360;
            }
            case "Camel" -> {
                triggerAnim("camel_controller", "camel");
                this.ticksAnimDelay = 700;
            }
            case "Cat", "Ocelot" -> {
                triggerAnim("cat_controller", "cat");
                this.ticksAnimDelay = 340;
            }
            case "Chicken" -> {
                triggerAnim("fowl_controller", "fowl");
                this.ticksAnimDelay = 740;
            }
            case "Cow", "Mooshroom" -> {
                triggerAnim("cow_controller", "cow");
                this.ticksAnimDelay = 560;
            }
            case "Donkey", "Mule" -> {
                triggerAnim("mule_controller", "mule");
                this.ticksAnimDelay = 520;
            }
            case "Fox" -> {
                triggerAnim("fox_controller", "fox");
                this.ticksAnimDelay = 580;
            }
            case "Frog" -> {
                triggerAnim("frog_controller", "frog");
                this.ticksAnimDelay = 700;
            }
            case "Horse" -> {
                triggerAnim("steed_controller", "steed");
                this.ticksAnimDelay = 460;
            }
            case "Parrot" -> {
                triggerAnim("bird_controller", "bird");
                this.ticksAnimDelay = 580;
            }
            case "Pig" -> {
                triggerAnim("pig_controller", "pig");
                this.ticksAnimDelay = 460;
            }
            case "Rabbit" -> {
                triggerAnim("hare_controller", "hare");
                this.ticksAnimDelay = 480;
            }
            case "Sheep" -> {
                triggerAnim("sheep_controller", "sheep");
                this.ticksAnimDelay = 620;
            }
            case "SnowGolem" -> {
                triggerAnim("frosty_controller", "frosty");
                this.ticksAnimDelay = 980;
            }
            case "Turtle" -> {
                triggerAnim("crush_controller", "crush");
                this.ticksAnimDelay = 820;
            }
            case "Villager", "Wandering_Trader" -> {
                triggerAnim("squid_controller", "squid");
                this.ticksAnimDelay = 760;
            }
            case "Bee" -> {
                triggerAnim("bee_controller", "bee");
                this.ticksAnimDelay = 280;
            }
            case "Goat" -> {
                triggerAnim("goat_controller", "goat");
                this.ticksAnimDelay = 540;
            }
            case "IronGolem" -> {
                triggerAnim("fe_controller", "fe");
                this.ticksAnimDelay = 220;
            }
            case "Llama", "Trader_Llama" -> {
                triggerAnim("llama_controller", "llama");
                this.ticksAnimDelay = 740;
            }
            case "Panda" -> {
                triggerAnim("po_controller", "po");
                this.ticksAnimDelay = 380;
            }
            case "PolarBear" -> {
                triggerAnim("bear_controller", "bear");
                this.ticksAnimDelay = 500;
            }
            case "Wolf" -> {
                triggerAnim("wolf_controller", "wolf");
                this.ticksAnimDelay = 740;
            }
            case "Greenfly", "Bluefly" -> {
                triggerAnim("glow_controller", "glow");
                this.ticksAnimDelay = 720;
            }
            default -> this.ticksAnimDelay = 0;
        }
    }
    public boolean shouldRender(double distance) { return true; }
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) { return dimensions.height * 0.5F; }

    // Set, Check, & Morse Code
    public boolean isScared() {
        if ((this.world.getClosestPlayer(CLOSE_PLAYER_PREDICATE, this) != null)) {
            this.dataTracker.set(SCARED, true);
            return true;
        } else {
            this.dataTracker.set(SCARED, false);
            return false;
        }
    }
    public String setBlink() {
        long time = world.getTimeOfDay() % 24000;
        if (this.world.getBlockState(this.getBlockPos()).getBlock() instanceof CobwebBlock) {
            this.morse = "SOS";
            return this.morse;
        }
        if (this.world.getClosestPlayer(CLOSE_PLAYER_PREDICATE, this) != null) {
            this.morse = "Human";
            return this.morse;
        }
        if (world.getClosestEntity(PathAwareEntity.class, CLOSE_ENTITY_PREDICATE, this, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().expand(20)) != null) {
            this.morse = Objects.requireNonNull(world.getClosestEntity(PathAwareEntity.class, CLOSE_ENTITY_PREDICATE, this, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().expand(20))).toString().split("Entity")[0];
            return this.morse;
        }
        if (this.world.isRaining() || this.world.isWater(this.getBlockPos())) {
            if (this.world.isThundering()) {
                this.morse = "Storm";
            } else {
                this.morse = "Wet";
            }
            return this.morse;
        }
        if ((time <= 1000 || time >= 13000)) {
            this.morse = "Dark";
        }
        return this.morse;
    }
    public boolean canBlink() {
        long time = world.getTimeOfDay() % 24000;
        int l = this.world.getLightLevel(this.getBlockPos());
        if (!this.isScared() && this.ticksSinceScared >= 100) {
            return (time <= 1000 || time >= 13000) || this.world.isRaining() || this.world.isThundering() || (l < 10 || (this.world.getBlockState(this.getBlockPos()).getBlock() instanceof CobwebBlock));
        } else {
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
        if (this.ticksAnimDelay <= 0) {
            if (this.canBlink() && (this.random.nextInt(19) == 0)) {
                this.blinkController();
            }
        } else {
            this.ticksAnimDelay--;
        }
    }
    public void tickMovement() {
        super.tickMovement();
        if (this.world.getBlockState(this.getBlockPos()).getBlock() instanceof CobwebBlock) {
            this.setVelocity(this.getVelocity().multiply(0, 0, 0));
        }
    }

    // Custom Goals
    @SuppressWarnings("EmptyMethod")
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
        ColorflyLookControl(MobEntity entity) {
            super(entity);
        }

        public void tick() {
            {
                super.tick();
            }
        }

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
            if (this.mob.isTouchingWater()) {
                vec3d = FuzzyTargeting.find(this.mob, 10, 10);
            }
            if (this.mob.getRandom().nextFloat() >= this.probability) {
                vec3d = this.locatePlantBlock();
            }
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
                    if (!var5.hasNext()) {
                        return null;
                    }
                    blockPos2 = var5.next();
                } while (blockPos.equals(blockPos2));
                BlockState blockState = this.mob.world.getBlockState(mutable2.set(blockPos2, Direction.DOWN));
                bl = blockState.getBlock() instanceof LeavesBlock || blockState.getBlock() instanceof PlantBlock;
            } while (!bl || !this.mob.world.isAir(blockPos2) || !this.mob.world.isAir(mutable.set(blockPos2, Direction.UP)));

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
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) { return false; }

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
    @Override
    public boolean canBeLeashedBy(PlayerEntity player) { return false; }
    public boolean canAvoidTraps() { return true; }
    public int getXpToDrop() { return 0; }
    protected void pushAway(Entity entity) { if (entity instanceof GreenflyEntity || entity instanceof BlueflyEntity) { super.pushAway(entity); }}
    public boolean isPushable() { return false; }
    protected void tickCramming() {}
    @Nullable
    protected SoundEvent getAmbientSound() { return null; }
    protected SoundEvent getHurtSound(DamageSource source) { return null; }
    protected float getSoundVolume() { return 0; }

    //Static Variables
    static {
        CAPTURE = Sets.newHashSet(Items.GLASS_BOTTLE);
        SCARED = DataTracker.registerData(AbstractColorflyEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        CLOSE_PLAYER_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(1);
        CLOSE_ENTITY_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(1);
    }
}