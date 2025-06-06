package com.lestora.basetemp.dependencies;

import com.lestora.basetemp.dependencies.Alt.AlternateWetnessHandler;
import com.lestora.basetemp.dependencies.Real.RealWetnessHandler;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

public class WetnessHandler {
    static boolean initialized = false;
    static Function<Player, Integer> getWetness;

    public static void init() {
        if (net.minecraftforge.fml.ModList.get().isLoaded("lestora_wetness")) {
            getWetness = RealWetnessHandler.getWetnessOrdinal();
        }
        else {
            getWetness = AlternateWetnessHandler.getWetnessOrdinal();
        }
        initialized = true;
    }

    public static int getWetnessOrdinal(Player player) {
        return getWetness.apply(player);
    }
}