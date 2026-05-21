package io.siuolplex.fhcoal.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class FHCoalItemTags {
    public static TagKey<Item> STORAGE_BLOCKS_RAW_ARKENIUM = cTag("storage_blocks/raw_arkenium");
    public static TagKey<Item> STORAGE_BLOCKS_RAW_SHADOLINE = cTag("storage_blocks/raw_shadoline");
    public static TagKey<Item> STORAGE_BLOCKS_RAW_PALLADIUM = cTag("storage_blocks/raw_palladium");


    public static TagKey<Item> RAW_ARKENIUM = cTag("raw_materials/raw_arkenium");
    public static TagKey<Item> RAW_SHADOLINE = cTag("raw_materials/raw_shadoline");
    public static TagKey<Item> RAW_PALLADIUM = cTag("raw_materials/raw_palladium");

    private static TagKey<Item> cTag(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", name));
    }

    private static TagKey<Item> fhcTag(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("fhcoal", name));
    }
}
