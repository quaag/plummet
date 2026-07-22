package dev.quaag.plummet.gamemode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DrillDifficultyTest {
    @Test
    void targetsIncreaseWithDifficulty() {
        assertEquals(3.0, DrillDifficulty.EASY.target());
        assertEquals(5.0, DrillDifficulty.NORMAL.target());
        assertEquals(7.0, DrillDifficulty.HARD.target());
    }

    @Test
    void idsAreLowercase() {
        assertEquals("easy", DrillDifficulty.EASY.id());
        assertEquals("normal", DrillDifficulty.NORMAL.id());
        assertEquals("hard", DrillDifficulty.HARD.id());
    }
}
