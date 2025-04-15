package com.lestora.vanillatemp.dependencies.Alt;

import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

public class AlternateWetnessHandler {
    public static Function<Player, Integer> getWetnessOrdinal() {
        return (player) -> {
            if (player.isInWater()) return 2;
            if (player.isInWaterOrRain()) return 1;
            return 0;
        };
    }
}
