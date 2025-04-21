package com.lestora.basetemp.dependencies;

import com.lestora.basetemp.dependencies.Alt.AlternateBiomeConfigHandler;
import com.lestora.basetemp.dependencies.Real.RealBiomeConfigHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;

import java.util.HashMap;
import java.util.Map;
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
        }
        else {
            getBiomeTemp = AlternateBiomeConfigHandler.getBiomeTemp();
        }
        initialized = true;
    }

    public static float getBiomeTemp(Biome biome, MinecraftServer server) {
        if (!initialized) init();

        ResourceLocation rl = biomeRLCache.computeIfAbsent(biome, b -> {
            var biomeRegistry = server.registryAccess().lookupOrThrow(Registries.BIOME);
            return biomeRegistry.getResourceKey(biome).get().location();
        });

        if (rl == null) return 1f; // biome.getBaseTemperature() is so bad, just default to neutral val between -1f and 2f

        return getBiomeTemp.apply(rl);
    }
}