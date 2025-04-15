package com.lestora.vanillatemp.dependencies.Real;

import com.lestora.wetness.models.Wetness;
import com.lestora.wetness.models.WetnessUtil;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

public class RealWetnessHandler {
    public static Function<Player, Integer> getWetnessOrdinal() {
        return (player) -> {
            var playerWetness = WetnessUtil.getWetness(player);
            var thisWetness = playerWetness == Wetness.FULLY_SUBMERGED ? Wetness.NEARLY_SUBMERGED : playerWetness;
            return thisWetness.ordinal();
        };
    }
}