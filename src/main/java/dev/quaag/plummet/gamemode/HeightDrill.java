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
    private static final Map<UUID, State> ACTIVE = new HashMap<>();

    private HeightDrill() {}

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

        ServerTickEvents.END_SERVER_TICK.register(HeightDrill::tick);
    }

    public static void start(ServerPlayerEntity player, DrillDifficulty difficulty) {
        ACTIVE.put(player.getUuid(), new State(difficulty));
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
        private final DrillDifficulty difficulty;
        private boolean launched;
        private double baseY;
        private double peakY;
        private double best;
        private int attempts;
        private int passes;

        private State(DrillDifficulty difficulty) {
            this.difficulty = difficulty;
        }

        private void launch(double y) {
            launched = true;
            baseY = y;
            peakY = y;
        }

        private void tick(ServerPlayerEntity player) {
            if (!launched) {
                return;
            }

            double y = player.getY();
            if (y > peakY) {
                peakY = y;
            }

            if (!player.isOnGround()) {
                return;
            }

            launched = false;
            double gained = peakY - baseY;
            if (gained > best) {
                best = gained;
            }

            attempts++;
            boolean passed = gained >= difficulty.target();
            if (passed) {
                passes++;
            }
            player.sendMessage(format(gained, passed), true);
        }

        private Text format(double gained, boolean passed) {
            return Text.literal(String.format(
                Locale.ROOT,
                "%s %.1f of %.1f blocks, best %.1f, %d of %d passed",
                passed ? "PASS" : "FAIL",
                gained,
                difficulty.target(),
                best,
                passes,
                attempts));
        }
    }
}
