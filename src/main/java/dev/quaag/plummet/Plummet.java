package dev.quaag.plummet;

import dev.quaag.plummet.command.BotCommand;
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

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                BotCommand.register(dispatcher));
    }
}
