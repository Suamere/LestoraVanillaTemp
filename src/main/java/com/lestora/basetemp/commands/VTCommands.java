package com.lestora.basetemp.commands;

import com.lestora.basetemp.BaseTemp;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VTCommands {

    @SubscribeEvent
    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        var root = Commands.literal("lestora");

        registerGetCurrentTemp(root);

        event.getDispatcher().register(root);
    }

    private static void registerGetCurrentTemp(LiteralArgumentBuilder<CommandSourceStack> root) {
        root.then(Commands.literal("baseTemp")
            .then(Commands.literal("current")
                    .executes(ctx -> {
                        float currentTemp = BaseTemp.calculate(Minecraft.getInstance().player);
                        Minecraft.getInstance().gui.getChat().addMessage(Component.literal("Current body temp: " + currentTemp));
                        return 1;
                    })
        ));
    }
}