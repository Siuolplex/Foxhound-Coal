package io.siuolplex.fhcoal.compat.create.registration.pastel;

import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import earth.terrarium.pastel.registries.PastelRecipeTypes;
import io.siuolplex.fhcoal.compat.create.registration.FHCoalCIATs;
import net.minecraft.world.item.BlockItem;

public class PastelCreateIATs {
    public static ItemAttributeType HUMUS_DIPPABLE = FHCoalCIATs.singleton("humus_dippable", (s, w) -> FHCoalCIATs.testRecipeUnstable(s, w, PastelRecipeTypes.HUMUS_CONVERTING));
    public static ItemAttributeType MIDNIGHT_DIPPABLE = FHCoalCIATs.singleton("midnight_dippable", (s, w) -> FHCoalCIATs.testRecipeUnstable(s, w, PastelRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING));
    public static ItemAttributeType CRYSTAL_DIPPABLE = FHCoalCIATs.singleton("crystal_dippable", (s, w) -> FHCoalCIATs.testRecipeUnstable(s, w, PastelRecipeTypes.LIQUID_CRYSTAL_CONVERTING));
    public static ItemAttributeType DRAGONROT_DIPPABLE = FHCoalCIATs.singleton("dragonrot_dippable", (s, w) -> FHCoalCIATs.testRecipeUnstable(s, w, PastelRecipeTypes.DRAGONROT_CONVERTING));
    public static ItemAttributeType ANVIL_CRUSHING = FHCoalCIATs.singleton("anvil_crushable", (s, w) -> FHCoalCIATs.testRecipe(s, w, PastelRecipeTypes.ANVIL_CRUSHING));

    public static void init() {

    }
}
