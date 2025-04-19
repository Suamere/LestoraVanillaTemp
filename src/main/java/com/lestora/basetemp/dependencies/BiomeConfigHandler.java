package com.lestora.basetemp.dependencies;

import com.lestora.basetemp.dependencies.Alt.AlternateBiomeConfigHandler;
import com.lestora.basetemp.dependencies.Real.RealBiomeConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class BiomeConfigHandler {
    static boolean initialized = false;
    static boolean configLoaded = false;
    static Function<ResourceLocation, Float> getBiomeTemp;
    static final Map<Biome, ResourceLocation> biomeRLCache = new HashMap<>();

    public static void init() {
        if (net.minecraftforge.fml.ModList.get().isLoaded("lestora_config")) {
            getBiomeTemp = RealBiomeConfigHandler.getBiomeTemp();
            configLoaded = true;
            //BiomeConfig.subscribe(......);
        }
        else {
            getBiomeTemp = AlternateBiomeConfigHandler.getBiomeTemp();
        }
        initialized = true;
    }

    public static float getBiomeTemp(Biome biome) {
        if (!initialized) init();

        ResourceLocation rl = biomeRLCache.get(biome);
        if (rl == null) {
            // Attempt to lookup via the dynamic registry from the current client level.
            var mc = Minecraft.getInstance();
            if (mc.level != null) {
                Optional<Registry<Biome>> maybeBiomeRegistry = mc.level.registryAccess().registries()
                        .filter(entry -> entry.key().equals(Registries.BIOME))
                        .map(entry -> (Registry<Biome>) entry.value())
                        .findFirst();
                if (maybeBiomeRegistry.isPresent()) {
                    rl = maybeBiomeRegistry.get().getKey(biome);
                    if (rl != null) {
                        biomeRLCache.put(biome, rl);
                    }
                }
            }
        }
        // Look up the temperature from our config map, falling back to the biome's base temperature.
        Float temp = (rl != null) ? getBiomeTemp.apply(rl) : null;
        if (temp == null) {
            temp = 1f; // biome.getBaseTemperature() is so bad, just default to neutral val between -1f and 2f
        }
        return temp;
    }
}