package com.lestora.basetemp.dependencies;

import com.lestora.basetemp.dependencies.Real.RealDebugHandler;

public class DebugHandler {
    static boolean initialized = false;

    public static void init() {
        if (net.minecraftforge.fml.ModList.get().isLoaded("lestora_debug")) {
            RealDebugHandler.registerDebug();
        }
        initialized = true;
    }
}