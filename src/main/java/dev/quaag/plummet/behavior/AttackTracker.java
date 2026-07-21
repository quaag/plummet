package dev.quaag.plummet.behavior;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class AttackTracker {
    private static final int MAX_AGE_TICKS = 2;
    private static final Map<UUID, Capture> CAPTURES = new HashMap<>();

    private AttackTracker() {}

    public static void register() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClient() || !(player instanceof ServerPlayerEntity serverPlayer)) {
                return ActionResult.PASS;
            }

            CAPTURES.put(serverPlayer.getUuid(), new Capture(serverPlayer.fallDistance, serverPlayer.age));
            return ActionResult.PASS;
        });
    }

    public static double fallDistance(ServerPlayerEntity player) {
        Capture capture = CAPTURES.get(player.getUuid());
        if (capture == null || player.age - capture.age > MAX_AGE_TICKS) {
            return player.fallDistance;
        }
        return capture.fallDistance;
    }

    private static final class Capture {
        private final double fallDistance;
        private final int age;

        private Capture(double fallDistance, int age) {
            this.fallDistance = fallDistance;
            this.age = age;
        }
    }
}
