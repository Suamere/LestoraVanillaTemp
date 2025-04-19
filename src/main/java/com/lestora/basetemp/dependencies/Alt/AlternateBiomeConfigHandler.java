package com.lestora.basetemp.dependencies.Alt;

import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class AlternateBiomeConfigHandler {
    static final Map<ResourceLocation, Float> PRIORITY_MAP;
    static {
        Map<ResourceLocation, Float> defaultBiomeTemps = new HashMap<>();
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "end_barrens"), -1.0f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "end_midlands"), -1.0f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "the_end"), -1.0f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "small_end_islands"), -1.0f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "end_highlands"), 0.0f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "the_void"), -1.0f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "ice_spikes"), -1.0f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "frozen_peaks"), -1.0f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "deep_frozen_ocean"), -1.0f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "frozen_ocean"), -0.8f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "jagged_peaks"), -0.7f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "snowy_taiga"), -0.7f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "frozen_river"), -0.5f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "snowy_slopes"), -0.7f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "snowy_plains"), -0.8f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "deep_cold_ocean"), -0.8f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "snowy_beach"), -0.25f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "grove"), -0.2f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "cold_ocean"), -0.2f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "old_growth_spruce_taiga"), 0.25f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "taiga"), 0.25f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "old_growth_pine_taiga"), 0.3f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "lush_caves"), 0.4f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "dark_forest"), 0.4f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "pale_garden"), 0.4f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "cherry_grove"), 0.5f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "deep_ocean"), 0.3f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "meadow"), 0.5f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "ocean"), 0.5f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "river"), 0.5f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "warm_ocean"), 0.7f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "birch_forest"), 0.6f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "old_growth_birch_forest"), 0.6f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "deep_lukewarm_ocean"), 0.7f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "lukewarm_ocean"), 0.6f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "flower_forest"), 0.7f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "forest"), 0.7f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "beach"), 0.8f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "dripstone_caves"), 0.4f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "mangrove_swamp"), 0.9f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "plains"), 0.8f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "sunflower_plains"), 0.8f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "mushroom_fields"), 0.9f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "stony_peaks"), 1.0f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "deep_dark"), 0.8f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "bamboo_jungle"), 1.1f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "sparse_jungle"), 1.1f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "swamp"), 1.0f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "jungle"), 1.2f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "eroded_badlands"), 1.5f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "savanna"), 1.5f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "savanna_plateau"), 1.5f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "windswept_savanna"), 1.5f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "wooded_badlands"), 1.5f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "badlands"), 1.8f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "desert"), 1.8f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "basalt_deltas"), 2.0f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "crimson_forest"), 1.8f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "nether_wastes"), 2.0f);
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "soul_sand_valley"), -0.5f); // Odd choice, but let's make it cold in a Soul Sand Valley?  Lol
        defaultBiomeTemps.put(ResourceLocation.fromNamespaceAndPath("minecraft", "warped_forest"), 1.8f);
        PRIORITY_MAP = Collections.unmodifiableMap(defaultBiomeTemps);
    }

    // Returns a BiFunction that looks up a ResourceLocation directly in the PRIORITY_MAP.
    public static Function<ResourceLocation, Float> getBiomeTemp() {
        return (rl) -> (rl == null) ? 0 : PRIORITY_MAP.getOrDefault(rl, 0f);
    }
}
