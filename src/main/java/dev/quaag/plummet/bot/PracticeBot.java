package dev.quaag.plummet.bot;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Unit;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class PracticeBot {
    public static final String BOT_NAME = "Plummet Dummy";
    public static final int MIN_HEALTH = 1;
    public static final int MAX_HEALTH = 100;
    public static final int DEFAULT_HEALTH = 20;

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
        return spawn(world, pos, yaw, DEFAULT_HEALTH);
    }

    public static ZombieEntity spawn(ServerWorld world, Vec3d pos, float yaw, int health) {
        remove();

        ZombieEntity bot = EntityType.ZOMBIE.create(world, SpawnReason.COMMAND);
        if (bot == null) {
            return null;
        }

        int clamped = MathHelper.clamp(health, MIN_HEALTH, MAX_HEALTH);
        EntityAttributeInstance maxHealth = bot.getAttributeInstance(EntityAttributes.MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.setBaseValue(clamped);
        }
        bot.setHealth(clamped);

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
