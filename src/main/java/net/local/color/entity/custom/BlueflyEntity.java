package net.local.color.entity.custom;

// imported packages

import net.local.color.item.ModItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
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

//Bluefly class w/ animation call
public class BlueflyEntity extends AbstractColorflyEntity implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    // Initialize Bluefly
    public BlueflyEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.ignoreCameraFrustum = true;
        this.moveControl = new MoveControl(this);
        this.lookControl = new BlueflyLookControl(this);
    }
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.setSilent(true);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    // Tamable Child Override
    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) { return null; }

    // Bluefly Custom Goals
    public static class BlueflyLookControl extends LookControl {
        BlueflyLookControl(MobEntity entity) { super(entity); }
        public void tick() {{ super.tick();}}
        protected boolean shouldStayHorizontal() {
            return true;
        }
    }

    // Bluefly Morse Code
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

    // Bluefly Tick
    public void mobTick() {
    }
    public void tick() {
        super.tick();
    }
    public void tickMovement() {
        super.tickMovement();
    }

    // Bluefly Animation code
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
            player.giveItemStack(new ItemStack(ModItems.BLUEFLY_BOTTLE,1));

            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }


    // Bluefly Spawn Condition
    public static boolean canCustomSpawn(EntityType<BlueflyEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        int l = world.getLightLevel(pos);

        return l <= 10 && canMobSpawn(type, world, spawnReason, pos, random);
    }
}