package com.lestora.basetemp.dependencies.Real;

import com.lestora.debug.DebugOverlay;
import com.lestora.debug.models.DebugObject;
import com.lestora.debug.models.DebugSupplier;
import com.lestora.basetemp.BaseTemp;
import net.minecraft.client.Minecraft;

public class RealDebugHandler {
    public static void registerDebug() {
        var bodyTempSupplier = new DebugSupplier("Lestora_BodyTemp", 9, () -> {
            var bodyTemp = BaseTemp.getLastTemp(Minecraft.getInstance().player.getUUID());
            var temp = String.valueOf(bodyTemp.playerTemp());
            int color = bodyTemp.playerTemp() > 120 ? 16711680 : (bodyTemp.playerTemp() < 60 ? 255 : 16776960);
            return new DebugObject("Temp", color, false, temp, color, false,
                    "Body Temp", color, true, temp, color, true);
        });
        DebugOverlay.registerDebugLine(bodyTempSupplier.getKey(), bodyTempSupplier);
    }
}