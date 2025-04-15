package com.lestora.vanillatemp;

import com.lestora.vanillatemp.dependencies.BiomeConfigHandler;
import com.lestora.vanillatemp.dependencies.DebugHandler;
import com.lestora.vanillatemp.dependencies.WetnessHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("lestora_vanilla_temperature")
public class LestoraVTMod {
    public LestoraVTMod(FMLJavaModLoadingContext constructContext) {
        WetnessHandler.init();
        BiomeConfigHandler.init();
        VanillaTemp.init();
        DebugHandler.init();
    }
}