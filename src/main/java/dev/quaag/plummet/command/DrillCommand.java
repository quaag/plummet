package dev.quaag.plummet.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.quaag.plummet.gamemode.ComboDrill;
import dev.quaag.plummet.gamemode.HeightDrill;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public final class DrillCommand {
    private DrillCommand() {}

    public static LiteralArgumentBuilder<ServerCommandSource> build() {
        return literal("drill")
            .then(literal("height")
                .executes(ctx -> startHeight(ctx.getSource())))
            .then(literal("combo")
                .executes(ctx -> startCombo(ctx.getSource())))
            .then(literal("stop")
                .executes(ctx -> stopDrill(ctx.getSource())));
    }

    private static int startHeight(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        ComboDrill.stop(player);
        HeightDrill.start(player);
        source.sendFeedback(
            () -> Text.literal("[Plummet] Height drill started. Use a wind charge and land to see your gain."),
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
