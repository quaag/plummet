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

public final class HeightDrill {
    private static final Map<UUID, HeightDrillState> ACTIVE = new HashMap<>();

    private HeightDrill() {}

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (world.isClient() || !(player instanceof ServerPlayerEntity serverPlayer)) {
                return ActionResult.PASS;
            }
            if (!player.getStackInHand(hand).isOf(Items.WIND_CHARGE)) {
                return ActionResult.PASS;
            }

            HeightDrillState state = ACTIVE.get(serverPlayer.getUuid());
            if (state != null) {
                state.launch(serverPlayer.getY());
            }
            return ActionResult.PASS;
        });

        ServerTickEvents.END_SERVER_TICK.register(HeightDrill::tick);
    }

    public static void start(ServerPlayerEntity player, DrillDifficulty difficulty) {
        ACTIVE.put(player.getUuid(), new HeightDrillState(difficulty.target()));
    }

    public static boolean stop(ServerPlayerEntity player) {
        return ACTIVE.remove(player.getUuid()) != null;
    }

    public static boolean isRunning(ServerPlayerEntity player) {
        return ACTIVE.containsKey(player.getUuid());
    }

    private static void tick(MinecraftServer server) {
        if (ACTIVE.isEmpty()) {
            return;
        }

        Iterator<Map.Entry<UUID, HeightDrillState>> entries = ACTIVE.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<UUID, HeightDrillState> entry = entries.next();
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(entry.getKey());
            if (player == null) {
                entries.remove();
                continue;
            }

            HeightDrillState state = entry.getValue();
            HeightDrillState.Landing landing = state.tick(player.getY(), player.isOnGround());
            if (landing != null) {
                player.sendMessage(format(state.target(), landing), true);
            }
        }
    }

    private static Text format(double target, HeightDrillState.Landing landing) {
        return Text.literal(String.format(
            Locale.ROOT,
            "%s %.1f of %.1f blocks, best %.1f, %d of %d passed",
            landing.passed ? "PASS" : "FAIL",
            landing.gained,
            target,
            landing.best,
            landing.passes,
            landing.attempts));
    }
}
