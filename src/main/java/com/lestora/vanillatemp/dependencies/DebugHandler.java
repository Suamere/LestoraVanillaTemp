package com.lestora.vanillatemp.dependencies;

import com.lestora.vanillatemp.dependencies.Alt.AlternateWetnessHandler;
import com.lestora.vanillatemp.dependencies.Real.RealDebugHandler;
import com.lestora.vanillatemp.dependencies.Real.RealWetnessHandler;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

public class DebugHandler {
    static boolean initialized = false;

    public static void init() {
        if (net.minecraftforge.fml.ModList.get().isLoaded("lestora_debug")) {
            RealDebugHandler.registerDebug();
        }
        initialized = true;
    }
}