package com.lestora.basetemp.commands;

import com.lestora.basetemp.BaseTemp;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BTServerCommands {
    @SubscribeEvent
    static void onRegisterCommands(RegisterCommandsEvent event) {
        // build the shared "baseTemp" node
        LiteralArgumentBuilder<CommandSourceStack> baseTemp = Commands.literal("baseTemp");

        // /lestora baseTemp current
        baseTemp.then(Commands.literal("current")
                .requires(src -> src.getEntity() instanceof ServerPlayer)
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    var snap = BaseTemp.getLastTemp(player.getUUID()); // your snapshot
                    ctx.getSource().sendSuccess(
                            () -> Component.literal("Current body temp: " + snap.playerTemp()),
                            false
                    );
                    return 1;
                })
        );

        // /lestora baseTemp disable <temp>
        baseTemp.then(Commands.literal("disable")
                .requires(src -> src.getEntity() instanceof ServerPlayer)
                // no-arg version: use the defaultPlayerTemp
                .executes(ctx -> {
                    BaseTemp.tempTickInterval = 0;
                    ctx.getSource().sendSuccess(() -> Component.literal("baseTemp disabled, set players to " + BaseTemp.defaultPlayerTemp), true);
                    return 1;
                })
                // arg version: override the default
                .then(Commands.argument("temp", IntegerArgumentType.integer())
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            BaseTemp.defaultPlayerTemp = IntegerArgumentType.getInteger(ctx, "temp");
                            BaseTemp.tempTickInterval = 0;
                            ctx.getSource().sendSuccess(() -> Component.literal("baseTemp disabled, set players to " + BaseTemp.defaultPlayerTemp), true);
                            return 1;
                        })
                )
        );

        // /lestora baseTemp tempInterval <ticks>
        baseTemp.then(Commands.literal("tempInterval")
                .requires(src -> src.hasPermission(2))  // ops only
                .then(Commands.argument("ticks", IntegerArgumentType.integer(1))
                        .executes(ctx -> {
                            int ticks = IntegerArgumentType.getInteger(ctx, "ticks");
                            BaseTemp.tempTickInterval = ticks;
                            ctx.getSource().sendSuccess(
                                    () -> Component.literal("Temperature update interval set to " + ticks + " ticks"),
                                    true
                            );
                            return 1;
                        })
                )
        );

        // register the root command: /lestora baseTemp ...
        event.getDispatcher().register(
                Commands.literal("lestora")
                        .then(baseTemp)
        );
    }
}
