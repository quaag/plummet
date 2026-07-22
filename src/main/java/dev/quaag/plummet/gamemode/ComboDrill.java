package dev.quaag.plummet.gamemode;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public final class ComboDrill {
    private static final Map<UUID, State> ACTIVE = new HashMap<>();

    private ComboDrill() {}

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (world.isClient() || !(player instanceof ServerPlayerEntity serverPlayer)) {
                return ActionResult.PASS;
            }
            if (!player.getStackInHand(hand).isOf(Items.WIND_CHARGE)) {
                return ActionResult.PASS;
            }

            State state = ACTIVE.get(serverPlayer.getUuid());
            if (state != null) {
                state.launch(serverPlayer.getY());
            }
            return ActionResult.PASS;
        });

        ServerTickEvents.END_SERVER_TICK.register(ComboDrill::tick);
    }

    public static void start(ServerPlayerEntity player) {
        ACTIVE.put(player.getUuid(), new State());
    }

    public static boolean stop(ServerPlayerEntity player) {
        return ACTIVE.remove(player.getUuid()) != null;
    }

    public static boolean isRunning(ServerPlayerEntity player) {
        return ACTIVE.containsKey(player.getUuid());
    }

    public static void recordHit(ServerPlayerEntity player, float damage, boolean mace) {
        State state = ACTIVE.get(player.getUuid());
        if (state != null) {
            state.hit(damage, mace);
        }
    }

    private static void tick(MinecraftServer server) {
        if (ACTIVE.isEmpty()) {
            return;
        }

        Iterator<Map.Entry<UUID, State>> entries = ACTIVE.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<UUID, State> entry = entries.next();
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(entry.getKey());
            if (player == null) {
                entries.remove();
                continue;
            }
            entry.getValue().tick(player);
        }
    }

    private static final class State {
        private boolean launched;
        private double baseY;
        private double peakY;
        private int airtime;
        private float damage;
        private boolean mace;
        private boolean hit;

        private void launch(double y) {
            launched = true;
            baseY = y;
            peakY = y;
            airtime = 0;
            damage = 0.0F;
            mace = false;
            hit = false;
        }

        private void hit(float dealt, boolean withMace) {
            if (!launched) {
                return;
            }

            hit = true;
            damage = dealt;
            mace = withMace;
        }

        private void tick(ServerPlayerEntity player) {
            if (!launched) {
                return;
            }

            airtime++;
            double y = player.getY();
            if (y > peakY) {
                peakY = y;
            }

            if (!player.isOnGround()) {
                return;
            }

            launched = false;
            player.sendMessage(format(peakY - baseY), true);
        }

        private Text format(double gained) {
            if (!hit) {
                return Text.literal(String.format(
                    Locale.ROOT,
                    "%.1f blocks, %d ticks airtime, no hit",
                    gained,
                    airtime));
            }

            return Text.literal(String.format(
                Locale.ROOT,
                "%.1f blocks, %d ticks airtime, %s %.1f damage",
                gained,
                airtime,
                mace ? "MACE" : "hit",
                damage));
        }
    }
}
