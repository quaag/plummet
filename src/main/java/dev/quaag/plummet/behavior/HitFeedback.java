package dev.quaag.plummet.behavior;

import dev.quaag.plummet.bot.PracticeBot;
import dev.quaag.plummet.gamemode.ComboDrill;
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

            double fallDistance = AttackTracker.fallDistance(player);
            boolean mace = AttackTracker.usedMace(player);
            SessionStats.recordHit(player, fallDistance, damageTaken, mace);
            ComboDrill.recordHit(player, damageTaken, mace);
            player.sendMessage(format(damageTaken, fallDistance, mace), true);
        });
    }

    private static Text format(float damage, double fallDistance, boolean mace) {
        return Text.literal(String.format(
            Locale.ROOT,
            "%s %.1f damage from %.1f blocks",
            mace ? "MACE" : "hit",
            damage,
            fallDistance));
    }
}
