package com.lestora.basetemp.dependencies.Real;

import com.lestora.debug.DebugOverlay;
import com.lestora.debug.models.DebugObject;
import com.lestora.debug.models.DebugSupplier;
import com.lestora.basetemp.BaseTemp;
import net.minecraft.client.Minecraft;

public class RealDebugHandler {
    public static void registerDebug() {
        System.err.println("DO IT");
        var bodyTempSupplier = new DebugSupplier("Lestora_BodyTemp", 9, () -> {
            float bodyTemp = BaseTemp.calculate(Minecraft.getInstance().player);
            var temp = String.valueOf(bodyTemp);
            int color = bodyTemp > 120 ? 16711680 : (bodyTemp < 60 ? 255 : 16776960);
            return new DebugObject("Temp", color, false, temp, color, false,
                    "Body Temp", color, true, temp, color, true);
        });
        DebugOverlay.registerDebugLine(bodyTempSupplier.getKey(), bodyTempSupplier);
    }
}