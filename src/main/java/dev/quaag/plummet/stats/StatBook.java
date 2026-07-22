package dev.quaag.plummet.stats;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class StatBook {
    private final Map<UUID, StatEntry> entries = new HashMap<>();

    public void record(UUID id, double fallDistance, float damage, boolean mace) {
        entries.computeIfAbsent(id, key -> new StatEntry()).record(fallDistance, damage, mace);
    }

    public StatEntry get(UUID id) {
        return entries.get(id);
    }

    public StatEntry getOrCreate(UUID id) {
        return entries.computeIfAbsent(id, key -> new StatEntry());
    }

    public boolean reset(UUID id) {
        return entries.remove(id) != null;
    }

    public void clear() {
        entries.clear();
    }

    public int size() {
        return entries.size();
    }

    public Set<UUID> ids() {
        return entries.keySet();
    }
}
