package com.lumengrid.oritechthings.block.custom;

import com.lumengrid.oritechthings.block.entity.TierAddonBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rearth.oritech.block.blocks.addons.MachineAddonBlock;

public class TierAddonBlock extends MachineAddonBlock {

    public TierAddonBlock(AddonSettings addonSettings) {
        super(BlockBehaviour.Properties.of(), addonSettings);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        try {
            return new TierAddonBlockEntity(pos, state);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public Class<? extends BlockEntity> getBlockEntityType() {
        return TierAddonBlockEntity.class;
    }
}
