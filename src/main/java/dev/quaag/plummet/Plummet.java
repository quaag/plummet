package dev.quaag.plummet;

import dev.quaag.plummet.behavior.AttackTracker;
import dev.quaag.plummet.behavior.HitFeedback;
import dev.quaag.plummet.bot.PracticeBot;
import dev.quaag.plummet.command.BotCommand;
import dev.quaag.plummet.gamemode.ComboDrill;
import dev.quaag.plummet.gamemode.HeightDrill;
import dev.quaag.plummet.stats.StatsStorage;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Plummet implements ModInitializer {
    public static final String MOD_ID = "plummet";
    public static final Logger LOGGER = LoggerFactory.getLogger("Plummet");

    @Override
    public void onInitialize() {
        LOGGER.info("Plummet initializing");

        PracticeBot.register();
        AttackTracker.register();
        HitFeedback.register();
        HeightDrill.register();
        ComboDrill.register();
        StatsStorage.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                BotCommand.register(dispatcher));
    }
}
