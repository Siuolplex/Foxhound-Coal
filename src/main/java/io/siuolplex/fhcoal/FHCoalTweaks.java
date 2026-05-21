package io.siuolplex.fhcoal;

import com.mojang.logging.LogUtils;
import io.siuolplex.fhcoal.compat.create.FHCoalCreate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;

@Mod(FHCoalTweaks.MODID)
public class FHCoalTweaks {
    public static final String MODID = "fhcoal";
    private static final Logger LOGGER = LogUtils.getLogger();

    public FHCoalTweaks(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registryHandler);
    }

    public void registryHandler(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.ITEM)) {
            FHCoalItems.init(event);
        } if (ModList.get().isLoaded("create")) {
            FHCoalCreate.createRegistryHandler(event);
        }
    }

    public static ResourceLocation getResourceLocation(String id) {
        return ResourceLocation.fromNamespaceAndPath(MODID, id);
    }
}
