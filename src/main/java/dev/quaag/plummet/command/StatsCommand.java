package dev.quaag.plummet.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.quaag.plummet.stats.SessionStats;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Locale;

import static net.minecraft.server.command.CommandManager.literal;

public final class StatsCommand {
    private StatsCommand() {}

    public static LiteralArgumentBuilder<ServerCommandSource> build() {
        return literal("stats")
            .executes(ctx -> show(ctx.getSource()))
            .then(literal("reset")
                .executes(ctx -> reset(ctx.getSource())));
    }

    private static int show(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        int hits = SessionStats.hits(player);
        double best = SessionStats.bestFallDistance(player);
        source.sendFeedback(
            () -> Text.literal(String.format(
                Locale.ROOT,
                "[Plummet] %d dummy hits, best fall %.1f blocks.",
                hits,
                best)),
            false);
        return 1;
    }

    private static int reset(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        if (!SessionStats.reset(player)) {
            source.sendError(Text.literal("[Plummet] No stats to reset."));
            return 0;
        }

        source.sendFeedback(
            () -> Text.literal("[Plummet] Session stats reset."),
            false);
        return 1;
    }
}
