package io.siuolplex.fhcoal.mixin;

import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BedItem.class)
public class BedItemMixin {
    @Inject(method = "<init>", at = @At(value = "HEAD"))
    private static void setBedStackSize(Block block, Item.Properties prop, CallbackInfo ci) {
        prop.stacksTo(64);
    }
}
