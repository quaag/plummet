package dev.quaag.plummet.behavior;

import dev.quaag.plummet.bot.PracticeBot;
import dev.quaag.plummet.stats.SessionStats;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Locale;

public final class HitFeedback {
    private HitFeedback() {}

    public static void register() {
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamageTaken, damageTaken, blocked) -> {
            if (!PracticeBot.isBot(entity)) {
                return;
            }

            Entity attacker = source.getAttacker();
            if (!(attacker instanceof ServerPlayerEntity player)) {
                return;
            }

            SessionStats.recordHit(player, player.fallDistance);
            player.sendMessage(format(damageTaken, player.fallDistance), true);
        });
    }

    private static Text format(float damage, double fallDistance) {
        return Text.literal(String.format(
            Locale.ROOT,
            "%.1f damage from %.1f blocks",
            damage,
            fallDistance));
    }
}
