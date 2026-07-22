package dev.quaag.plummet.stats;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public final class SessionStats {
    private static final String ENTRIES_KEY = "entries";
    private static final String UUID_KEY = "uuid";
    private static final String HITS_KEY = "hits";
    private static final String MACE_HITS_KEY = "maceHits";
    private static final String BEST_FALL_KEY = "bestFallDistance";
    private static final String BEST_MACE_KEY = "bestMaceDamage";
    private static final String BEST_OTHER_KEY = "bestOtherDamage";

    private static final StatBook BOOK = new StatBook();

    private SessionStats() {}

    public static void recordHit(ServerPlayerEntity player, double fallDistance, float damage, boolean mace) {
        BOOK.record(player.getUuid(), fallDistance, damage, mace);
    }

    public static int hits(ServerPlayerEntity player) {
        StatEntry entry = BOOK.get(player.getUuid());
        return entry == null ? 0 : entry.hits();
    }

    public static int maceHits(ServerPlayerEntity player) {
        StatEntry entry = BOOK.get(player.getUuid());
        return entry == null ? 0 : entry.maceHits();
    }

    public static double bestFallDistance(ServerPlayerEntity player) {
        StatEntry entry = BOOK.get(player.getUuid());
        return entry == null ? 0.0 : entry.bestFallDistance();
    }

    public static float bestMaceDamage(ServerPlayerEntity player) {
        StatEntry entry = BOOK.get(player.getUuid());
        return entry == null ? 0.0F : entry.bestMaceDamage();
    }

    public static float bestOtherDamage(ServerPlayerEntity player) {
        StatEntry entry = BOOK.get(player.getUuid());
        return entry == null ? 0.0F : entry.bestOtherDamage();
    }

    public static boolean reset(ServerPlayerEntity player) {
        return BOOK.reset(player.getUuid());
    }

    public static NbtCompound writeNbt() {
        NbtList list = new NbtList();
        for (UUID id : BOOK.ids()) {
            StatEntry entry = BOOK.get(id);
            NbtCompound nbt = new NbtCompound();
            nbt.putString(UUID_KEY, id.toString());
            nbt.putInt(HITS_KEY, entry.hits());
            nbt.putInt(MACE_HITS_KEY, entry.maceHits());
            nbt.putDouble(BEST_FALL_KEY, entry.bestFallDistance());
            nbt.putFloat(BEST_MACE_KEY, entry.bestMaceDamage());
            nbt.putFloat(BEST_OTHER_KEY, entry.bestOtherDamage());
            list.add(nbt);
        }

        NbtCompound root = new NbtCompound();
        root.put(ENTRIES_KEY, list);
        return root;
    }

    public static int readNbt(NbtCompound root) {
        BOOK.clear();

        NbtList list = root.getListOrEmpty(ENTRIES_KEY);
        for (int i = 0; i < list.size(); i++) {
            NbtCompound nbt = list.getCompoundOrEmpty(i);
            UUID uuid = parseUuid(nbt.getString(UUID_KEY, ""));
            if (uuid == null) {
                continue;
            }

            BOOK.getOrCreate(uuid).set(
                nbt.getInt(HITS_KEY, 0),
                nbt.getInt(MACE_HITS_KEY, 0),
                nbt.getDouble(BEST_FALL_KEY, 0.0),
                nbt.getFloat(BEST_MACE_KEY, 0.0F),
                nbt.getFloat(BEST_OTHER_KEY, 0.0F));
        }

        return BOOK.size();
    }

    private static UUID parseUuid(String value) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException invalid) {
            return null;
        }
    }
}
