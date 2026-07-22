package dev.quaag.plummet.gamemode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class HeightDrillStateTest {
    @Test
    void ignoresTicksBeforeLaunch() {
        HeightDrillState state = new HeightDrillState(5.0);
        assertFalse(state.isLaunched());
        assertNull(state.tick(70.0, true));
    }

    @Test
    void reportsPassOnLandingAboveTarget() {
        HeightDrillState state = new HeightDrillState(5.0);
        state.launch(64.0);
        assertTrue(state.isLaunched());
        assertNull(state.tick(68.0, false));
        assertNull(state.tick(70.0, false));

        HeightDrillState.Landing landing = state.tick(64.0, true);
        assertEquals(6.0, landing.gained);
        assertEquals(6.0, landing.best);
        assertTrue(landing.passed);
        assertEquals(1, landing.passes);
        assertEquals(1, landing.attempts);
        assertFalse(state.isLaunched());
    }

    @Test
    void reportsFailAndKeepsBestAcrossAttempts() {
        HeightDrillState state = new HeightDrillState(5.0);

        state.launch(64.0);
        state.tick(70.0, false);
        HeightDrillState.Landing first = state.tick(64.0, true);
        assertTrue(first.passed);
        assertEquals(6.0, first.best);

        state.launch(64.0);
        state.tick(66.0, false);
        HeightDrillState.Landing second = state.tick(64.0, true);
        assertFalse(second.passed);
        assertEquals(2.0, second.gained);
        assertEquals(6.0, second.best);
        assertEquals(1, second.passes);
        assertEquals(2, second.attempts);
    }
}
