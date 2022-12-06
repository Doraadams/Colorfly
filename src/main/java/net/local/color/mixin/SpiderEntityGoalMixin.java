package net.local.color.mixin;

import net.local.color.entity.custom.BlueflyEntity;
import net.local.color.entity.custom.GreenflyEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Spider Entity Mixin
@Mixin(SpiderEntity.class)
public abstract class SpiderEntityGoalMixin extends HostileEntity {

    protected SpiderEntityGoalMixin(EntityType<? extends HostileEntity> entityType, World world) { super(entityType, world); }

    // Mixin Active Target Goal for Colorfly Entities
    @Inject(method = "initGoals", at = @At("HEAD"))
    protected void onInitGoals(CallbackInfo ci) {
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, GreenflyEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, BlueflyEntity.class, true));
    }
}
