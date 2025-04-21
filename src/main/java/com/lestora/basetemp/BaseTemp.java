package com.lestora.basetemp;

import com.lestora.basetemp.dependencies.BiomeConfigHandler;
import com.lestora.basetemp.dependencies.WetnessHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BaseTemp {
    static final Map<UUID, TempSnapshot> playerTemps = new HashMap<>();
    static float maxDayDiff = 15f;
    public static int tempTickInterval = 5;
    public static float defaultPlayerTemp = 98.7f;
    private static int tickCounter = 0;
    private static Boolean enabled = true;

    public static TempSnapshot getLastTemp(UUID playerUUID) {
        return playerTemps.getOrDefault(playerUUID, new TempSnapshot(98.7f, 0f, 0f));
    }

    static TempSnapshot calculate(ServerPlayer player) {
        var level = player.level();
        var lastTemps = getLastTemp(player.getUUID());

        Holder<Biome> biomeHolder = level.getBiome(player.blockPosition());
        var biome = biomeHolder.value();

        BlockPos playerPos = player.blockPosition();
        float baseTemp = BiomeConfigHandler.getBiomeTemp(biome, player.getServer());
        int daytimeOffset = daytimeTempOffset(level.getDayTime(), baseTemp);

        // If player's altitude is below 60 and the biome is not one of the underground biomes,
        // then override baseTemp to 0.5.

        var isOverworld = level.dimension().equals(Level.OVERWORLD);
        var coolBeanz = 0.5f;
        if (player.getBlockY() < 45) {
            ResourceLocation biomeRL = getBiomeResourceLocation(level, biome);
            if (biomeRL == null ||
                    (!biomeRL.equals(ResourceLocation.parse("minecraft:dripstone_caves"))
                            && !biomeRL.equals(ResourceLocation.parse("minecraft:lush_caves"))
                            && !biomeRL.equals(ResourceLocation.parse("minecraft:deep_dark")))) {
                baseTemp = coolBeanz;
            }
        }

        if (isOverworld && baseTemp > coolBeanz){
            var light = level.getLightEngine().getLayerListener(LightLayer.SKY).getLightValue(playerPos);
            float skyLightTemp = light / 15f;
            baseTemp = ((baseTemp - coolBeanz) * skyLightTemp) + coolBeanz;
        }

        // Immediate check: if the player is directly in lava or fire.
        var heatDist = 5;
        var nearbyBlocks = getNearbyHeat(level, playerPos, heatDist);
        BlockState currentState = nearbyBlocks.get(playerPos);
        if (currentState != null) {
            if (currentState.getBlock() == Blocks.LAVA) {
                var newTemp = RubberBand(lastTemps.playerTemp(), 300);
                return setPlayerTemp(newTemp, daytimeOffset);
            }
            if (currentState.getBlock() == Blocks.FIRE || currentState.getBlock() == Blocks.SOUL_FIRE) {
                var newTemp = RubberBand(lastTemps.playerTemp(), 150);
                return setPlayerTemp(newTemp, daytimeOffset);
            }
        }

        // Check if the player has any Lava Buckets in inventory.
        boolean hasLavaBucket = false;
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty() && stack.getItem() == Items.LAVA_BUCKET) {
                hasLavaBucket = true;
                break;
            }
        }
        boolean hasSnowBucket = false;
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty() && stack.getItem() == Items.POWDER_SNOW_BUCKET) {
                hasSnowBucket = true;
                break;
            }
        }

        float nearHeatOffset = 0f;
        if (!player.isInWater()) {
            var nearHeatMax = 75f;
            if (hasLavaBucket || player.isOnFire()) {
                nearHeatOffset = nearHeatMax;
            } else {
                nearHeatOffset = getFireLavaOffset(playerPos, nearbyBlocks, heatDist, nearHeatMax);
            }
        }

        if (hasSnowBucket || player.isInPowderSnow) {
            nearHeatOffset -= 25f;
        }

        int altitudeOffset = (player.getBlockY() - 60) / 5;
        if (level.dimension().equals(Level.NETHER))
            altitudeOffset = player.getBlockY() / -5;
        else if (level.dimension().equals(Level.END))
            altitudeOffset = 0;

        var timeOffset = 0;
        if (isOverworld) {
            timeOffset = daytimeOffset;
        }

        var wetnessOffset = getWetnessOffset(player, baseTemp);
        var finalResult = Math.min((baseTemp * 25) - wetnessOffset - altitudeOffset + 60 + timeOffset + nearHeatOffset, 300);

        var newTemp = RubberBand(lastTemps.playerTemp(), finalResult);
        return setPlayerTemp(newTemp, daytimeOffset);
    }

    static int daytimeTempOffset(long time, float biomeBaseTemp) {
        float baseValue = getBaseValue(time, maxDayDiff);

        // Apply the environmentOffset multipliers:
        float result = baseValue;
        if (biomeBaseTemp < 0) {
            var absOffset = Math.abs(biomeBaseTemp) + 1;
            if (baseValue < 0) {
                result = baseValue * absOffset;
            } else if (baseValue > 0) {
                result = baseValue * (absOffset / 2.0f);
            }
        } else if (biomeBaseTemp > 0) {
            var absOffset = biomeBaseTemp + 1;
            if (baseValue < 0) {
                result = baseValue * (absOffset / 2.0f);
            } else if (baseValue > 0) {
                result = baseValue * absOffset;
            }
        }

        return (int) result;
    }


    static float getBaseValue(long time, float maxDayDiff) {
        long tickTime = time % 24000;
        float baseValue;

        // Compute the base value:
        // - 0 at sunrise (tick 0) and sunset (tick 12000)
        // - 15 at noon (tick 6000)
        // - -15 at midnight (tick 18000)
        if (tickTime < 6000) {
            // Sunrise (0) to noon (6000): increase from 0 to 15.
            baseValue = (maxDayDiff / 6000) * tickTime;
        } else if (tickTime < 12000) {
            // Noon (6000) to sunset (12000): decrease from 15 to 0.
            baseValue = maxDayDiff - (maxDayDiff / 6000) * (tickTime - 6000);
        } else if (tickTime < 18000) {
            // Sunset (12000) to midnight (18000): decrease from 0 to -15.
            baseValue = 0f - (maxDayDiff / 6000) * (tickTime - 12000);
        } else {
            // Midnight (18000) to next sunrise (24000): increase from -15 to 0.
            baseValue = -maxDayDiff + (maxDayDiff / 6000) * (tickTime - 18000);
        }
        return baseValue;
    }

    @SubscribeEvent
    static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        if (tempTickInterval == 0){
            tickCounter = 0;
            var player1Temp = playerTemps.entrySet().iterator().next().getValue().playerTemp();
            if (enabled || player1Temp != defaultPlayerTemp) {
                enabled = false;
                for (var player : event.getServer().getPlayerList().getPlayers())
                    playerTemps.put(player.getUUID(), new TempSnapshot(defaultPlayerTemp, 0f, 0f));
            }
            return;
        }

        if (++tickCounter < BaseTemp.tempTickInterval) return;
        tickCounter = 0;

        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers())
            playerTemps.put(player.getUUID(), BaseTemp.calculate(player));
    }

    static TempSnapshot setPlayerTemp(float playerTemp, int daytimeOffset) {
        return new TempSnapshot(playerTemp, daytimeOffset < 0 ? -daytimeOffset / maxDayDiff : 0f, daytimeOffset > 0 ?  daytimeOffset / maxDayDiff : 0f);
    }

    static float getWetnessOffset(Player player, float baseTemp) {
        int playerWetness = WetnessHandler.getWetnessOrdinal(player);
        return Math.min((baseTemp - 3) * -4 * playerWetness, 50);
    }

    static float RubberBand(float oldTemp, float newTemp) {
        float diff = newTemp - oldTemp;
        if (diff == 0f) return oldTemp;

        // your original “per‑tick” factor
        float baseFactor = 0.05f + 0.05f * (float)Math.tanh(Math.abs(diff) / 50.0f);

        // since you only call this every tempTickInterval ticks,
        // multiply factor by that interval
        int ticks = Math.max(1, BaseTemp.tempTickInterval);
        float factor = Math.min(1f, baseFactor * ticks);

        float step = diff * factor;
        return Math.abs(step) >= Math.abs(diff)
                ? newTemp
                : oldTemp + step;
    }

    static Map<BlockPos, BlockState> getNearbyHeat(Level level, BlockPos playerPos, int distance) {
        Map<BlockPos, BlockState> cachedBlockStates = new HashMap<>();
        for (int x = -distance; x <= distance; x++) {
            for (int y = -distance; y <= distance; y++) {
                for (int z = -distance; z <= distance; z++) {
                    BlockPos pos = playerPos.offset(x, y, z);
                    BlockState state = level.getBlockState(pos);
                    // Only store if the block is LAVA, FIRE, or SOUL_FIRE.
                    if (state.getBlock() == Blocks.LAVA ||
                            state.getBlock() == Blocks.FIRE ||
                            state.getBlock() == Blocks.SOUL_FIRE) {
                        cachedBlockStates.put(pos, state);
                    }
                }
            }
        }
        return cachedBlockStates;
    }

    static float getFireLavaOffset(BlockPos playerPos, Map<BlockPos, BlockState> cachedBlockStates, int spread, float max) {
        float offset = 0f;
        for (var entry : cachedBlockStates.entrySet()) {
            BlockPos pos = entry.getKey();
            BlockState state = entry.getValue();

            int distance = playerPos.distManhattan(pos);
            if (state.getBlock() == Blocks.LAVA) {
                float candidate = getLavaOffset(distance, spread, max);
                offset = Math.max(offset, candidate);
            } else if (state.getBlock() == Blocks.FIRE || state.getBlock() == Blocks.SOUL_FIRE) {
                float candidate = getLavaOffset(distance, spread, max) / 2;
                offset = Math.max(offset, candidate);
            }
        }
        return offset;
    }

    static float getLavaOffset(int d, int spread, float max) {
        if (d < 1) d = 1;
        if (d > spread) return 0f;

        float u = (float)(spread - d) / (spread - 1);
        float a = 0.1f; // My Curve.  0.0 is closer to a line, and 1.0 is very curvy

        return max * (a * u * u + (1 - a) * u);
    }

    static final Map<Biome, ResourceLocation> biomeResourceCache = new HashMap<>();
    static ResourceLocation getBiomeResourceLocation(Level world, Biome biome) {
        return biomeResourceCache.computeIfAbsent(biome, b -> {
            Optional<Registry<Biome>> maybeBiomeRegistry = world.registryAccess().registries()
                    .filter(entry -> entry.key().equals(Registries.BIOME))
                    .map(entry -> (Registry<Biome>) entry.value())
                    .findFirst();
            return maybeBiomeRegistry.map(registry -> registry.getKey(b)).orElse(null);
        });
    }
}