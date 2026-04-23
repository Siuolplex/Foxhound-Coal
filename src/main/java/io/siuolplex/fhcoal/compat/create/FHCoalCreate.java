package io.siuolplex.fhcoal.compat.create;

import com.simibubi.create.api.registry.CreateRegistries;
import io.siuolplex.fhcoal.compat.create.registration.FHCoalCIATs;
import net.neoforged.neoforge.registries.RegisterEvent;

public class FHCoalCreate {
    public static void createRegistryHandler(RegisterEvent event) {
        if (event.getRegistryKey().equals(CreateRegistries.ITEM_ATTRIBUTE_TYPE)) {
            FHCoalCIATs.init();
        }
    }
}
