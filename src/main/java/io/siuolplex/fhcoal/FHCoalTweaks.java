package io.siuolplex.fhcoal;

import com.mojang.logging.LogUtils;
import io.siuolplex.fhcoal.compat.create.FHCoalCreate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;

import java.util.List;

@Mod(FHCoalTweaks.MODID)
public class FHCoalTweaks {
    public static final String MODID = "fhcoal";
    private static final Logger LOGGER = LogUtils.getLogger();

    public FHCoalTweaks(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registryHandler);
        NeoForge.EVENT_BUS.addListener(this::onPortal );
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

    public void onPortal(BlockEvent.PortalSpawnEvent portalEvent) {
        if (portalEvent.getState().fhcoal$getPlacedByPlayer() == null || !portalEvent.getState().fhcoal$getPlacedByPlayer().hasPermissions(2)) {
            portalEvent.setCanceled(true);
        }


        /*for (Player player : players) {
            if (player.isHolding(Items.FLINT_AND_STEEL)) {
                Vec3 vec3 = portalEvent.getPos().getCenter();
                Vec3 vec31 = new Vec3(player.getX(), player.getEyeY(), player.getZ());
                Vec3 vec32 = Player.
                boolean works = portalEvent.getLevel().clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty())).getType() == HitResult.Type.BLOCK;
                if (works) {
                    return;
                }
            }
        }

        portalEvent.setCanceled(true);*/
    }
}
