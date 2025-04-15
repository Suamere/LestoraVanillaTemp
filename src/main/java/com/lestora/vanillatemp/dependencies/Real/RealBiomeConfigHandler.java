package com.lestora.vanillatemp.dependencies.Real;

import com.lestora.config.BiomeConfig;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class RealBiomeConfigHandler {
    public static Function<ResourceLocation, Float> getBiomeTemp() {
        return BiomeConfig::getBiomeTemp;
    }
}