package dev.quaag.plummet.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.quaag.plummet.gamemode.ComboDrill;
import dev.quaag.plummet.gamemode.DrillDifficulty;
import dev.quaag.plummet.gamemode.HeightDrill;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Locale;

import static net.minecraft.server.command.CommandManager.literal;

public final class DrillCommand {
    private DrillCommand() {}

    public static LiteralArgumentBuilder<ServerCommandSource> build() {
        return literal("drill")
            .then(literal("height")
                .executes(ctx -> startHeight(ctx.getSource(), DrillDifficulty.NORMAL))
                .then(literal("easy")
                    .executes(ctx -> startHeight(ctx.getSource(), DrillDifficulty.EASY)))
                .then(literal("normal")
                    .executes(ctx -> startHeight(ctx.getSource(), DrillDifficulty.NORMAL)))
                .then(literal("hard")
                    .executes(ctx -> startHeight(ctx.getSource(), DrillDifficulty.HARD))))
            .then(literal("combo")
                .executes(ctx -> startCombo(ctx.getSource())))
            .then(literal("stop")
                .executes(ctx -> stopDrill(ctx.getSource())));
    }

    private static int startHeight(ServerCommandSource source, DrillDifficulty difficulty)
            throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        ComboDrill.stop(player);
        HeightDrill.start(player, difficulty);
        source.sendFeedback(
            () -> Text.literal(String.format(
                Locale.ROOT,
                "[Plummet] Height drill started on %s, target %.1f blocks.",
                difficulty.id(),
                difficulty.target())),
            false);
        return 1;
    }

    private static int startCombo(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        HeightDrill.stop(player);
        ComboDrill.start(player);
        source.sendFeedback(
            () -> Text.literal("[Plummet] Combo drill started. Wind charge up, then mace the dummy on the way down."),
            false);
        return 1;
    }

    private static int stopDrill(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        boolean stopped = HeightDrill.stop(player) | ComboDrill.stop(player);
        if (!stopped) {
            source.sendError(Text.literal("[Plummet] No drill is running."));
            return 0;
        }

        source.sendFeedback(
            () -> Text.literal("[Plummet] Drill stopped."),
            false);
        return 1;
    }
}
