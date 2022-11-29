package net.local.color.entity.custom;

// imported packages

import net.local.color.item.ModItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;

//Greenfly class w/ animation call
public class GreenflyEntity extends AbstractColorflyEntity implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    // Initialize Greenfly
    public GreenflyEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.ignoreCameraFrustum = true;
        this.moveControl = new FlightMoveControl(this, 10, false);
        this.lookControl = new GreenflyLookControl(this);
    }
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.setSilent(true);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    // Greenfly Navigation
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world) {
            public boolean isValidPosition(BlockPos pos) {
                return !this.world.getBlockState(pos).isAir();
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

    // Tamable Child Override
    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) { return null; }

    // Greenfly Custom Goals
    public static class GreenflyLookControl extends LookControl {
        GreenflyLookControl(MobEntity entity) { super(entity); }
        public void tick() {{ super.tick();}}
        protected boolean shouldStayHorizontal() {
            return true;
        }
    }

    // Greenfly Morse Code
    public boolean canBlink () {
        long time = world.getTimeOfDay() % 24000;
        if (!isScared()) {
            return (time <= 1000 || time >= 13000) || this.world.isRaining() || this.world.isThundering();
        } else {
            return false;
        }
    }
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

    // Greenfly Custom Gravity
    public void setGravity () {
        if (this.getVelocity().getY() < -0.15) {
            this.setVelocity(this.getVelocity().multiply(1, 0.6, 1));
        }
    }

    // Fly + Walk State Controller
    public void stateControl () {
        this.setGround();
        if (!this.navigation.isIdle()) {
            if (this.onGround && !this.world.getBlockState(this.getBlockPos().down()).isAir())
                if (this.isWant()) {
                    this.setVelocity(this.getVelocity().getX(), -0.15, this.getVelocity().getZ());
                    if ((this.random.nextInt(100) <= 5)) {
                        this.setWant(false);
                    }
                } else {
                    if ((this.random.nextInt(100) <= 1)) {
                        this.setWant(true);
                    }
                }
        } else {
            if ((this.random.nextInt(100) <= 1) && this.world.getBlockState(this.getBlockPos().down()).isAir()) {
                this.setWant(true);
            }
        }
    }

    // Greenfly Tick
    public void mobTick() {
        this.setScared(false);
        this.stateControl();

    }
    public void tick() {
        super.tick();

        this.setGravity();
    }
    public void tickMovement() {
        super.tickMovement();
    }

    // Greenfly Animation code
    private <E extends IAnimatable> PlayState blinkPredicate(AnimationEvent<E> event) {
        if (event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            if (this.random.nextInt(4) == 0) {
                if (this.canBlink()) {
                    switch (this.setMorse()) {
                        case "dark" -> {
                            event.getController().markNeedsReload();
                            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.colorfly.dark", ILoopType.EDefaultLoopTypes.PLAY_ONCE));                        }
                        case "wet" -> {
                            event.getController().markNeedsReload();
                            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.colorfly.wet", ILoopType.EDefaultLoopTypes.PLAY_ONCE));                        }
                        case "storm" -> {
                            event.getController().markNeedsReload();
                            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.colorfly.storm", ILoopType.EDefaultLoopTypes.PLAY_ONCE));                        }
                        default -> {
                            event.getController().markNeedsReload();
                            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.colorfly.idle", ILoopType.EDefaultLoopTypes.PLAY_ONCE));                        }
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "blinkController", 5, this::blinkPredicate));
    }

    public AnimationFactory getFactory() {
        return this.factory;
    }

    // Interaction Code
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!this.world.isClient && CAPTURE.contains(itemStack.getItem())) {
            itemStack.decrement(1);
            this.remove(RemovalReason.DISCARDED);
            player.giveItemStack(new ItemStack(ModItems.GREENFLY_BOTTLE,1));

            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }


    // Greenfly Spawn Condition
    public static boolean canCustomSpawn(EntityType<GreenflyEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        int l = world.getLightLevel(pos);

        return l <= 10 && canMobSpawn(type, world, spawnReason, pos, random);
    }
}