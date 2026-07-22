package dev.quaag.plummet.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.quaag.plummet.bot.BotMode;
import dev.quaag.plummet.bot.PracticeBot;
import dev.quaag.plummet.scenario.Arena;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.server.command.CommandManager.argument;
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
                        .executes(ctx -> spawnBot(ctx.getSource(), PracticeBot.DEFAULT_HEALTH))
                        .then(argument("health", IntegerArgumentType.integer(
                                PracticeBot.MIN_HEALTH, PracticeBot.MAX_HEALTH))
                            .executes(ctx -> spawnBot(
                                ctx.getSource(),
                                IntegerArgumentType.getInteger(ctx, "health")))))
                    .then(literal("remove")
                        .executes(ctx -> removeBot(ctx.getSource())))
                    .then(literal("mode")
                        .then(literal("static")
                            .executes(ctx -> setMode(ctx.getSource(), BotMode.STATIC)))
                        .then(literal("strafe")
                            .executes(ctx -> setMode(ctx.getSource(), BotMode.STRAFE)))))
                .then(literal("arena")
                    .executes(ctx -> buildArena(ctx.getSource())))
                .then(DrillCommand.build())
                .then(StatsCommand.build())
        );
    }

    private static int buildArena(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        int placed = Arena.build(source.getWorld(), player);
        source.sendFeedback(
            () -> Text.literal("[Plummet] Built a " + Arena.SIZE + " by " + Arena.SIZE
                + " arena, placed " + placed + " blocks."),
            false);
        return 1;
    }

    private static int spawnBot(ServerCommandSource source, int health) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        float yaw = player.getYaw();
        double radians = Math.toRadians(yaw);
        Vec3d pos = player.getEntityPos().add(
            -Math.sin(radians) * SPAWN_DISTANCE,
            0.0,
            Math.cos(radians) * SPAWN_DISTANCE);

        ZombieEntity bot = PracticeBot.spawn(source.getWorld(), pos, yaw + 180.0f, health);
        if (bot == null) {
            source.sendError(Text.literal("[Plummet] Could not spawn a practice dummy."));
            return 0;
        }

        float spawned = bot.getMaxHealth();
        source.sendFeedback(
            () -> Text.literal("[Plummet] Spawned a practice dummy with " + (int) spawned + " health."),
            false);
        return 1;
    }

    private static int setMode(ServerCommandSource source, BotMode mode) {
        if (PracticeBot.get() == null) {
            source.sendError(Text.literal("[Plummet] No practice dummy to set a mode on."));
            return 0;
        }

        PracticeBot.setMode(mode);
        source.sendFeedback(
            () -> Text.literal("[Plummet] Dummy mode set to " + mode.id() + "."),
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
