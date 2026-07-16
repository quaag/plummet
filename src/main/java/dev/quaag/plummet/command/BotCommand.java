package dev.quaag.plummet.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.quaag.plummet.bot.PracticeBot;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.server.command.CommandManager.literal;

public final class BotCommand {
    private static final double SPAWN_DISTANCE = 3.0;

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
                        .executes(ctx -> spawnBot(ctx.getSource())))
                    .then(literal("remove")
                        .executes(ctx -> removeBot(ctx.getSource()))))
        );
    }

    private static int spawnBot(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        float yaw = player.getYaw();
        double radians = Math.toRadians(yaw);
        Vec3d pos = player.getEntityPos().add(
            -Math.sin(radians) * SPAWN_DISTANCE,
            0.0,
            Math.cos(radians) * SPAWN_DISTANCE);

        ZombieEntity bot = PracticeBot.spawn(source.getWorld(), pos, yaw + 180.0f);
        if (bot == null) {
            source.sendError(Text.literal("[Plummet] Could not spawn a practice dummy."));
            return 0;
        }

        source.sendFeedback(
            () -> Text.literal("[Plummet] Spawned a practice dummy."),
            false);
        return 1;
    }

    private static int removeBot(ServerCommandSource source) {
        if (!PracticeBot.remove()) {
            source.sendError(Text.literal("[Plummet] No practice dummy to remove."));
            return 0;
        }

        source.sendFeedback(
            () -> Text.literal("[Plummet] Removed the practice dummy."),
            false);
        return 1;
    }
}
