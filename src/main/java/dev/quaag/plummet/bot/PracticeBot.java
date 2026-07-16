package dev.quaag.plummet.bot;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Unit;
import net.minecraft.util.math.Vec3d;

public final class PracticeBot {
    public static final String BOT_NAME = "Plummet Dummy";

    private static ZombieEntity current;

    private PracticeBot() {}

    public static void register() {
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, amount) -> {
            if (!isBot(entity)) {
                return true;
            }
            entity.discard();
            current = null;
            return false;
        });
    }

    public static ZombieEntity spawn(ServerWorld world, Vec3d pos, float yaw) {
        remove();

        ZombieEntity bot = EntityType.ZOMBIE.create(world, SpawnReason.COMMAND);
        if (bot == null) {
            return null;
        }

        bot.refreshPositionAndAngles(pos.x, pos.y, pos.z, yaw, 0.0f);
        bot.setCustomName(Text.literal(BOT_NAME));
        bot.setCustomNameVisible(true);
        bot.setPersistent();
        bot.disableExperienceDropping();

        ItemStack helmet = new ItemStack(Items.LEATHER_HELMET);
        helmet.set(DataComponentTypes.UNBREAKABLE, Unit.INSTANCE);
        bot.equipStack(EquipmentSlot.HEAD, helmet);
        bot.setEquipmentDropChance(EquipmentSlot.HEAD, 0.0f);

        if (!world.spawnEntity(bot)) {
            return null;
        }

        current = bot;
        return bot;
    }

    public static ZombieEntity get() {
        if (current != null && current.isRemoved()) {
            current = null;
        }
        return current;
    }

    public static boolean remove() {
        ZombieEntity bot = get();
        if (bot == null) {
            return false;
        }
        bot.discard();
        current = null;
        return true;
    }

    public static boolean isBot(LivingEntity entity) {
        return entity != null && entity == current;
    }
}
