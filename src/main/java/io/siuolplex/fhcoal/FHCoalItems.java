package io.siuolplex.fhcoal;

import io.siuolplex.fhcoal.compat.create.registration.FHCoalCreateItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FHCoalItems {
    private static List<RegistrationHolder<Item>> HOLDERS = new ArrayList<>();

    public static RegistrationHolder<Item> CRUSHED_RAW_PALLADIUM = makeHolder("crushed_raw_palladium", () -> new Item(new Item.Properties()));
    public static RegistrationHolder<Item> CRUSHED_RAW_SHADOLINE = makeHolder("crushed_raw_shadoline", () -> new Item(new Item.Properties()));
    public static RegistrationHolder<Item> CRUSHED_RAW_ARKENIUM = makeHolder("crushed_raw_arkenium", () -> new Item(new Item.Properties()));



    public static Item register(String id, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, FHCoalTweaks.getResourceLocation(id), item);
    }

    public static RegistrationHolder<Item> makeHolder(String id, Supplier<Item> supplier) {
        RegistrationHolder<Item> holder = new RegistrationHolder<>(id, supplier);
        HOLDERS.add(holder);
        return holder;
    }

    public static void init(RegisterEvent event) {
        FHCoalCreateItems.init();

        HOLDERS.forEach(itemRegistrationHolder -> itemRegistrationHolder.register((Registry<Item>) event.getRegistry()));
    }

    // Exists to have something I can use until it either gets replaced or something is registered in its stead
    public static class RegistrationHolder<T> {
        String id;
        // Either the supplier or the item will be null depending on where we are in the registry.
        @Nullable
        Supplier<T> supplier;
        @Nullable
        T item = null;
        boolean isDummyOrDisabled = true;

        public RegistrationHolder(String id, @NotNull Supplier<T> supplier) {
            this.id = id;
            this.supplier = supplier;
        }

        public void setValid() {
            isDummyOrDisabled = false;
        }

        public T register(Registry<T> registry) {
            item = Registry.register(registry, FHCoalTweaks.getResourceLocation(id), supplier.get());
            supplier = null;
            return item;
        }
    }
}
