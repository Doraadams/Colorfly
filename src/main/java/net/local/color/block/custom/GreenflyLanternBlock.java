package net.local.color.block.custom;

import net.minecraft.block.*;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;

public class GreenflyLanternBlock extends AbstractColorflyLanternBlock {
    public static final IntProperty LIT = IntProperty.of("lit",10,15);

    public GreenflyLanternBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(LIT, 15)
                .with(HANGING, false)
                .with(WATERLOGGED, false));
    }

    //Append/Extra Code
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HANGING, WATERLOGGED, LIT);
    }
}