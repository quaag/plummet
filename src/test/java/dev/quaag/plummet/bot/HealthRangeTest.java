package dev.quaag.plummet.bot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HealthRangeTest {
    @Test
    void clampsBelowMinimum() {
        assertEquals(HealthRange.MIN, HealthRange.clamp(0));
        assertEquals(HealthRange.MIN, HealthRange.clamp(-50));
    }

    @Test
    void clampsAboveMaximum() {
        assertEquals(HealthRange.MAX, HealthRange.clamp(101));
        assertEquals(HealthRange.MAX, HealthRange.clamp(9999));
    }

    @Test
    void keepsValuesInRange() {
        assertEquals(HealthRange.MIN, HealthRange.clamp(HealthRange.MIN));
        assertEquals(HealthRange.MAX, HealthRange.clamp(HealthRange.MAX));
        assertEquals(20, HealthRange.clamp(20));
    }
}
