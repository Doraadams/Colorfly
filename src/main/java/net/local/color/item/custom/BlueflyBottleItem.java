package net.local.color.item.custom;

import net.local.color.entity.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import java.util.Objects;


//Bluefly Bottle
public class BlueflyBottleItem extends Item {

    public BlueflyBottleItem(Settings settings) {
        super(settings);
    }

    //Interaction | Release Stored Bluefly
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!(world instanceof ServerWorld)) {
            return ActionResult.SUCCESS;
        } else {
            ItemStack itemStack = context.getStack();
            BlockPos blockPos = context.getBlockPos();
            Direction direction = context.getSide();
            BlockState blockState = world.getBlockState(blockPos);
            BlockPos blockPos2;
            if (blockState.getCollisionShape(world, blockPos).isEmpty()) {
                blockPos2 = blockPos;
            } else {
                blockPos2 = blockPos.offset(direction);
            }
            if (ModEntities.BLUEFLY.spawnFromItemStack((ServerWorld) world, itemStack, context.getPlayer(), blockPos2, SpawnReason.BUCKET, true, !Objects.equals(blockPos, blockPos2) && direction == Direction.UP) != null) {
                itemStack.decrement(1);
                world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
            }
            return ActionResult.CONSUME;
        }
    }
}