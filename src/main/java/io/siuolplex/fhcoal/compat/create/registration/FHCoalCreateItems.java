package io.siuolplex.fhcoal.compat.create.registration;

import io.siuolplex.fhcoal.FHCoalItems;
import net.neoforged.fml.ModList;

public class FHCoalCreateItems {


    public static void init() {
        if (ModList.get().isLoaded("galosphere")) {
            FHCoalItems.CRUSHED_RAW_PALLADIUM.setValid();
        }
    }

}
