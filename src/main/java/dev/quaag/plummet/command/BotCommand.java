package dev.quaag.plummet.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public final class BotCommand {

    private BotCommand() {}

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            literal("plummet")
                .executes(ctx -> {
                    ctx.getSource().sendFeedback(
                        () -> Text.literal("Plummet. Try /plummet bot spawn"),
                        false);
                    return 1;
                })
                .then(literal("bot")
                    .then(literal("spawn")
                        .executes(ctx -> spawnBot(ctx.getSource()))))
        );
    }

    private static int spawnBot(ServerCommandSource source) {
        source.getServer().getCommandManager()
            .parseAndExecute(source, "summon minecraft:zombie ~ ~ ~2");

        source.sendFeedback(
            () -> Text.literal("[Plummet] Spawned a practice dummy."),
            false);
        return 1;
    }
}
