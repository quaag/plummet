package dev.quaag.plummet.stats;

import dev.quaag.plummet.Plummet;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class StatsStorage {
    private static final String FILE_NAME = "plummet_stats.dat";

    private StatsStorage() {}

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTED.register(StatsStorage::load);
        ServerLifecycleEvents.SERVER_STOPPING.register(StatsStorage::save);
    }

    private static void load(MinecraftServer server) {
        Path path = path(server);
        if (!Files.isRegularFile(path)) {
            return;
        }

        try {
            NbtCompound root = NbtIo.read(path);
            if (root == null) {
                return;
            }

            int loaded = SessionStats.readNbt(root);
            Plummet.LOGGER.info("Loaded Plummet stats for {} players", loaded);
        } catch (IOException failure) {
            Plummet.LOGGER.warn("Could not read {}", path, failure);
        }
    }

    private static void save(MinecraftServer server) {
        Path path = path(server);
        try {
            NbtIo.write(SessionStats.writeNbt(), path);
        } catch (IOException failure) {
            Plummet.LOGGER.warn("Could not write {}", path, failure);
        }
    }

    private static Path path(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT).resolve(FILE_NAME);
    }
}
