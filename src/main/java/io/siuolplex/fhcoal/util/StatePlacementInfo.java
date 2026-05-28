package io.siuolplex.fhcoal.util;

import net.minecraft.world.entity.player.Player;

public interface StatePlacementInfo {
    default Player fhcoal$getPlacedByPlayer() {
        return null;
    };
    default void fhcoal$setPlacedByPlayer(Player placedByPlayer) {};
}
