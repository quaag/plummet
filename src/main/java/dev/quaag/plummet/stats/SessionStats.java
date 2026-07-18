package dev.quaag.plummet.stats;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SessionStats {
    private static final Map<UUID, Entry> ENTRIES = new HashMap<>();

    private SessionStats() {}

    public static void recordHit(ServerPlayerEntity player, double fallDistance) {
        Entry entry = ENTRIES.computeIfAbsent(player.getUuid(), key -> new Entry());
        entry.hits++;
        if (fallDistance > entry.bestFallDistance) {
            entry.bestFallDistance = fallDistance;
        }
    }

    public static int hits(ServerPlayerEntity player) {
        Entry entry = ENTRIES.get(player.getUuid());
        return entry == null ? 0 : entry.hits;
    }

    public static double bestFallDistance(ServerPlayerEntity player) {
        Entry entry = ENTRIES.get(player.getUuid());
        return entry == null ? 0.0 : entry.bestFallDistance;
    }

    public static boolean reset(ServerPlayerEntity player) {
        return ENTRIES.remove(player.getUuid()) != null;
    }

    private static final class Entry {
        private int hits;
        private double bestFallDistance;
    }
}
