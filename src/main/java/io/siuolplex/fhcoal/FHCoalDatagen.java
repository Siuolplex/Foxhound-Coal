package io.siuolplex.fhcoal;

import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.CrushingRecipeGen;
import io.siuolplex.fhcoal.tags.FHCoalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class FHCoalDatagen {
    @SubscribeEvent // on the mod event bus
    public static void gatherData(GatherDataEvent event) {
        // Data generators may require some of these as constructor parameters.
        // See below for more details on each of these.
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Register the provider.
        generator.addProvider(
                // A boolean that determines whether the data should actually be generated.
                // The event provides methods that determine this:
                // event.includeClient(), event.includeServer(),
                // event.includeDev() and event.includeReports().
                // Since recipes are server data, we only run them in a server datagen.
                event.includeServer(),
                // Our provider.
                new FHCoalRecipeProvider(output, lookupProvider)
        );
        // Other data providers here.
    }

    public static class FHCoalRecipeProvider extends RecipeProvider {
        FHCoalCrushingRecipe crushingRecipe;

        public FHCoalRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
            crushingRecipe = new FHCoalCrushingRecipe(output, lookupProvider);
        }

        @Override
        protected void buildRecipes(@NotNull RecipeOutput output) {
            crushingRecipe.buildRecipes(output);
        }

        public static class FHCoalCrushingRecipe extends CrushingRecipeGen {
            public FHCoalCrushingRecipe(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
                super(output, registries, "fhcoal");
            }

            // I stole code from create sorry chat

            public GeneratedRecipe rawOreCompat(String targetModid, String metalName, Supplier<TagKey<Item>> input, Supplier<ItemLike> result, boolean block, int xpMult) {
                return create("raw_" + metalName + (block ? "_block" : ""), b -> {
                    int amount = block ? 9 : 1;
                    return b.duration(400)
                            .require(input.get())
                            .output(result.get(), amount)
                            .output(.75f, AllItems.EXP_NUGGET.get(), amount * xpMult)
                            .whenModLoaded(targetModid);
                });
            }

            protected GeneratedRecipe oreCompat(String targetModid, ItemLike stoneType, Supplier<ItemLike> ore, Supplier<ItemLike> raw, float expectedAmount, int duration) {
                return create(ore, b -> {
                    b.duration(duration).output(raw.get(), Mth.floor(expectedAmount));
                    float extra = expectedAmount - Mth.floor(expectedAmount);
                    if (extra > 0)
                        b.output(extra, raw.get(), 1);
                    return b.output(.75f, AllItems.EXP_NUGGET.get(), 1).output(.125f, stoneType).whenModLoaded(targetModid);

                });
            }


            GeneratedRecipe CRUSHED_RAW_ARKENIUM = rawOreCompat("aetherdelight", "arkenium", () -> FHCoalItemTags.RAW_ARKENIUM, () -> FHCoalItems.CRUSHED_RAW_ARKENIUM.item, false, 1);
            GeneratedRecipe CRUSHED_RAW_ARKENIUM_BLOCK = rawOreCompat("aetherdelight", "arkenium", () -> FHCoalItemTags.STORAGE_BLOCKS_RAW_ARKENIUM, () -> FHCoalItems.CRUSHED_RAW_ARKENIUM.item, true, 1);

            GeneratedRecipe CRUSHED_RAW_PALLADIUM = rawOreCompat("galosphere","palladium", () -> FHCoalItemTags.RAW_PALLADIUM, () -> FHCoalItems.CRUSHED_RAW_PALLADIUM.item, false, 1);
            GeneratedRecipe CRUSHED_RAW_PALLADIUM_BLOCK = rawOreCompat("galosphere","palladium", () -> FHCoalItemTags.STORAGE_BLOCKS_RAW_PALLADIUM, () -> FHCoalItems.CRUSHED_RAW_PALLADIUM.item, true, 1);

            GeneratedRecipe CRUSHED_RAW_SHADOLINE = rawOreCompat("enderscape", "shadoline", () -> FHCoalItemTags.RAW_SHADOLINE, () -> FHCoalItems.CRUSHED_RAW_SHADOLINE.item, false, 1);
            GeneratedRecipe CRUSHED_RAW_SHADOLINE_BLOCK = rawOreCompat("enderscape","palladium", () -> FHCoalItemTags.STORAGE_BLOCKS_RAW_SHADOLINE, () -> FHCoalItems.CRUSHED_RAW_SHADOLINE.item, true, 1);
        }


    }
}
