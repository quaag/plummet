package dev.quaag.plummet.stats;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SessionStats {
    private static final Map<UUID, Entry> ENTRIES = new HashMap<>();

    private SessionStats() {}

    public static void recordHit(ServerPlayerEntity player, double fallDistance, float damage, boolean mace) {
        Entry entry = ENTRIES.computeIfAbsent(player.getUuid(), key -> new Entry());
        entry.hits++;
        if (fallDistance > entry.bestFallDistance) {
            entry.bestFallDistance = fallDistance;
        }

        if (mace) {
            entry.maceHits++;
            if (damage > entry.bestMaceDamage) {
                entry.bestMaceDamage = damage;
            }
        } else if (damage > entry.bestOtherDamage) {
            entry.bestOtherDamage = damage;
        }
    }

    public static int hits(ServerPlayerEntity player) {
        Entry entry = ENTRIES.get(player.getUuid());
        return entry == null ? 0 : entry.hits;
    }

    public static int maceHits(ServerPlayerEntity player) {
        Entry entry = ENTRIES.get(player.getUuid());
        return entry == null ? 0 : entry.maceHits;
    }

    public static double bestFallDistance(ServerPlayerEntity player) {
        Entry entry = ENTRIES.get(player.getUuid());
        return entry == null ? 0.0 : entry.bestFallDistance;
    }

    public static float bestMaceDamage(ServerPlayerEntity player) {
        Entry entry = ENTRIES.get(player.getUuid());
        return entry == null ? 0.0F : entry.bestMaceDamage;
    }

    public static float bestOtherDamage(ServerPlayerEntity player) {
        Entry entry = ENTRIES.get(player.getUuid());
        return entry == null ? 0.0F : entry.bestOtherDamage;
    }

    public static boolean reset(ServerPlayerEntity player) {
        return ENTRIES.remove(player.getUuid()) != null;
    }

    private static final class Entry {
        private int hits;
        private int maceHits;
        private double bestFallDistance;
        private float bestMaceDamage;
        private float bestOtherDamage;
    }
}
