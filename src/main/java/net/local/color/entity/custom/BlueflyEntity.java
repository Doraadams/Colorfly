package net.local.color.entity.custom;

import net.local.color.item.ModItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;

public class BlueflyEntity extends AbstractColorflyEntity {
    private static final Ingredient TEMPT;

    // Initialize
    public BlueflyEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.ignoreCameraFrustum = true;
        BOTTLE = ModItems.BLUEFLY_BOTTLE;
        this.moveControl = new FlightMoveControl(this, 10, false);
        this.lookControl = new ColorflyLookControl(this);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
    }

    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt) {
        this.setSilent(true);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    // InitGoals
    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new TemptGoal(this, 1.2, TEMPT, false));
        this.goalSelector.add(3, new FleeEntityGoal<>(this, FrogEntity.class ,2.0F, 1, 1));
        this.goalSelector.add(3, new FleeEntityGoal<>(this, PlayerEntity.class, 1.0F, 1, 1));
        this.goalSelector.add(3, new AbstractColorflyEntity.AvoidDaylightGoal(1.0));
        this.goalSelector.add(4, new AbstractColorflyEntity.ColorFlyOntoOrganicGoal(this, 1.0));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(5, new LookAroundGoal(this));
    }

    // Spawn Condition
    public static boolean canCustomSpawn(EntityType<BlueflyEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        int l = world.getLightLevel(pos);
        return l <= 10 && canMobSpawn(type, world, spawnReason, pos, random);
    }

    // Static
    static {
        TEMPT = Ingredient.ofItems(ModItems.BLUEFLY_BOTTLE);
    }
}