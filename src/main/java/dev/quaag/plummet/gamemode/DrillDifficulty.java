package dev.quaag.plummet.gamemode;

import java.util.Locale;

public enum DrillDifficulty {
    EASY(3.0),
    NORMAL(5.0),
    HARD(7.0);

    private final double target;

    DrillDifficulty(double target) {
        this.target = target;
    }

    public double target() {
        return target;
    }

    public String id() {
        return name().toLowerCase(Locale.ROOT);
    }
}
