package dev.quaag.plummet.bot;

import java.util.Locale;

public enum BotMode {
    STATIC,
    STRAFE;

    public String id() {
        return name().toLowerCase(Locale.ROOT);
    }
}
