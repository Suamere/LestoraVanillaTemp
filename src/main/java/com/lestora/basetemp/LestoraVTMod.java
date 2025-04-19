package com.lestora.basetemp;

import com.lestora.basetemp.dependencies.BiomeConfigHandler;
import com.lestora.basetemp.dependencies.DebugHandler;
import com.lestora.basetemp.dependencies.WetnessHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("lestora_base_temperature")
public class LestoraVTMod {
    public LestoraVTMod(FMLJavaModLoadingContext constructContext) {
        WetnessHandler.init();
        BiomeConfigHandler.init();
        BaseTemp.init();
        DebugHandler.init();
    }
}