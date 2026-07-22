package dev.quaag.plummet.bot;

public final class HealthRange {
    public static final int MIN = 1;
    public static final int MAX = 100;
    public static final int DEFAULT = 20;

    private HealthRange() {}

    public static int clamp(int health) {
        if (health < MIN) {
            return MIN;
        }
        if (health > MAX) {
            return MAX;
        }
        return health;
    }
}
