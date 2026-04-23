package io.siuolplex.fhcoal.compat.create.registration;

import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import com.simibubi.create.content.logistics.item.filter.attribute.SingletonItemAttribute;
import io.siuolplex.fhcoal.FHCoalTweaks;
import io.siuolplex.fhcoal.compat.create.registration.pastel.PastelCreateIATs;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class FHCoalCIATs {
    static {
        if (ModList.get().isLoaded("pastel")) {
            PastelCreateIATs.init();
        }
    }

    // These are essentially the same as the methods in AllItemAttributeTypes, but they have to be public so that way I can use them for the mod compat stuff.
    public static <T extends Recipe<SingleRecipeInput>> boolean testRecipe(ItemStack s, Level w, RecipeType<T> type) {
        return w.getRecipeManager()
                .getRecipeFor(type, new SingleRecipeInput(s.copy()), w)
                .isPresent();
    }

    // Made specifically for the dipping recipes. It should work if the recipe is essentially , but I wouldnt recommend using it unless you have to.
    public static <T extends Recipe<RecipeInput>> boolean testRecipeUnstable(ItemStack s, Level w, RecipeType<T> type) {
        return w.getRecipeManager()
                .getRecipeFor(type, new SingleRecipeInput(s.copy()), w) // Hopefully this works man...
                .isPresent();
    }

    public static ItemAttributeType singleton(String id, Predicate<ItemStack> predicate) {
        return register(id, new SingletonItemAttribute.Type(type -> new SingletonItemAttribute(type, (stack, level) -> predicate.test(stack), "fhcoal." + id)));
    }

    public static ItemAttributeType singleton(String id, BiPredicate<ItemStack, Level> predicate) {
        return register(id, new SingletonItemAttribute.Type(type -> new SingletonItemAttribute(type, predicate, "fhcoal." + id)));
    }

    public static ItemAttributeType register(String id, ItemAttributeType type) {
        return Registry.register(CreateBuiltInRegistries.ITEM_ATTRIBUTE_TYPE, FHCoalTweaks.getResourceLocation(id), type);
    }

    public static void init() {}
}
