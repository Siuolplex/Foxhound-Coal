package io.siuolplex.fhcoal.compat;

import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public class ModDependentItem extends Item implements ModDependent {
    List<String> mods = new ArrayList<>();

    public ModDependentItem(Properties properties, String mod) {
        super(properties);
        mods.add(mod);
    }

    public ModDependentItem(Properties properties, String... mods) {
        super(properties);
        this.mods.addAll(List.of(mods));
    }

    @Override
    public boolean parentModsAvailable() {
        for (String mod : mods) {
            if (!ModList.get().isLoaded(mod)) {
                return false;
            }
        }
        return true;
    }
}
