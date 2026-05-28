package io.siuolplex.fhcoal.mixin;

import io.siuolplex.fhcoal.util.StatePlacementInfo;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockState.class)
public class BlockStateMixin implements StatePlacementInfo {
    @Unique
    Player fhcoal$placedByPlayer = null;

    @Override
    public Player fhcoal$getPlacedByPlayer() {
        return fhcoal$placedByPlayer;
    }

    @Override
    public void fhcoal$setPlacedByPlayer(Player placedByPlayer) {
        this.fhcoal$placedByPlayer = placedByPlayer;
    }
}
