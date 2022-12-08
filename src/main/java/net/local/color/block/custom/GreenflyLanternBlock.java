package net.local.color.block.custom;

import net.minecraft.block.*;
import net.minecraft.state.StateManager;

// Greenfly Lantern Block
public class GreenflyLanternBlock extends AbstractColorflyLanternBlock {
    public GreenflyLanternBlock(Settings settings) {
        super(settings);
        // Initialize Greenfly Lantern Block Default States
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(HANGING, false)
                .with(WATERLOGGED, false));
    }

    //Append Properties
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HANGING, WATERLOGGED);
    }
}