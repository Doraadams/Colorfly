package net.local.color.entity.custom;

import net.local.color.item.ModItems;
import net.minecraft.block.CobwebBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;

public class GreenflyEntity extends AbstractColorflyEntity {
    private static final Ingredient TEMPT;
    private int ticksSinceStateChange;

    // Initialize
    public GreenflyEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.ignoreCameraFrustum = true;
        BOTTLE = ModItems.GREENFLY_BOTTLE;
        this.moveControl = new FlightMoveControl(this, 0, true);
        this.lookControl = new ColorflyLookControl(this);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
    }
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt) {
        this.setSilent(true);
        this.ticksSinceStateChange = 0;
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    // InitGoals
    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new TemptGoal(this, 0.75, TEMPT, true));
        this.goalSelector.add(3, new FleeEntityGoal<>(this, FrogEntity.class ,2.0F, 1, 1));
        this.goalSelector.add(3, new FleeEntityGoal<>(this, PlayerEntity.class, 1.0F, 1, 1));
        this.goalSelector.add(3, new AbstractColorflyEntity.AvoidDaylightGoal(1.0));
        this.goalSelector.add(4, new AbstractColorflyEntity.ColorFlyOntoOrganicGoal(this, 1.0));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(5, new LookAroundGoal(this));
    }

    // Navigation && State Controller
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world)
        {
            public boolean isValidPosition(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }
            public void tick() { super.tick(); }
        };
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(true);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }
    public void stateControl () {
        if (!this.navigation.isIdle()) {
            if (this.onGround && !this.world.getBlockState(this.getBlockPos().down()).isAir())
                if (this.ticksSinceStateChange > (this.random.nextInt((100 - 5)) + 5)) {
                    this.ticksSinceStateChange = 0;
                    this.setVelocity(this.getVelocity().getX(), -0.15, this.getVelocity().getZ());
                }
        }
    }

    // Tick
    @Override
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
        this.stateControl();
        ++this.ticksSinceStateChange;
    }
    @Override
    public void tickMovement() {
        super.tickMovement();
        Vec3d vec3d = this.getVelocity();
        if (this.world.getBlockState(this.getBlockPos()).getBlock() instanceof CobwebBlock) {
            this.setVelocity(this.getVelocity().multiply(0,0,0));
        } else {
            if (!this.onGround && vec3d.y < -0.10) {
                this.setVelocity(vec3d.multiply(1.0, 0.6, 1.0));
            }
        }
    }

    // Spawn Condition
    public static boolean canCustomSpawn(EntityType<GreenflyEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        int l = world.getLightLevel(pos);
        return l <= 10 && canMobSpawn(type, world, spawnReason, pos, random);
    }

    // Static
    static {
        TEMPT = Ingredient.ofItems(ModItems.GREENFLY_BOTTLE);
    }
}