package dev.quaag.plummet.stats;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SessionStats {
    private static final String ENTRIES_KEY = "entries";
    private static final String UUID_KEY = "uuid";
    private static final String HITS_KEY = "hits";
    private static final String MACE_HITS_KEY = "maceHits";
    private static final String BEST_FALL_KEY = "bestFallDistance";
    private static final String BEST_MACE_KEY = "bestMaceDamage";
    private static final String BEST_OTHER_KEY = "bestOtherDamage";

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

    public static NbtCompound writeNbt() {
        NbtList list = new NbtList();
        for (Map.Entry<UUID, Entry> stored : ENTRIES.entrySet()) {
            Entry entry = stored.getValue();
            NbtCompound nbt = new NbtCompound();
            nbt.putString(UUID_KEY, stored.getKey().toString());
            nbt.putInt(HITS_KEY, entry.hits);
            nbt.putInt(MACE_HITS_KEY, entry.maceHits);
            nbt.putDouble(BEST_FALL_KEY, entry.bestFallDistance);
            nbt.putFloat(BEST_MACE_KEY, entry.bestMaceDamage);
            nbt.putFloat(BEST_OTHER_KEY, entry.bestOtherDamage);
            list.add(nbt);
        }

        NbtCompound root = new NbtCompound();
        root.put(ENTRIES_KEY, list);
        return root;
    }

    public static int readNbt(NbtCompound root) {
        ENTRIES.clear();

        NbtList list = root.getListOrEmpty(ENTRIES_KEY);
        for (int i = 0; i < list.size(); i++) {
            NbtCompound nbt = list.getCompoundOrEmpty(i);
            UUID uuid = parseUuid(nbt.getString(UUID_KEY, ""));
            if (uuid == null) {
                continue;
            }

            Entry entry = new Entry();
            entry.hits = nbt.getInt(HITS_KEY, 0);
            entry.maceHits = nbt.getInt(MACE_HITS_KEY, 0);
            entry.bestFallDistance = nbt.getDouble(BEST_FALL_KEY, 0.0);
            entry.bestMaceDamage = nbt.getFloat(BEST_MACE_KEY, 0.0F);
            entry.bestOtherDamage = nbt.getFloat(BEST_OTHER_KEY, 0.0F);
            ENTRIES.put(uuid, entry);
        }

        return ENTRIES.size();
    }

    private static UUID parseUuid(String value) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException invalid) {
            return null;
        }
    }

    private static final class Entry {
        private int hits;
        private int maceHits;
        private double bestFallDistance;
        private float bestMaceDamage;
        private float bestOtherDamage;
    }
}
