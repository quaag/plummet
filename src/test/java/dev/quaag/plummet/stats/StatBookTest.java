package dev.quaag.plummet.stats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class StatBookTest {
    private static final UUID ID = new UUID(1L, 2L);

    @Test
    void accumulatesHitsAndBests() {
        StatBook book = new StatBook();
        book.record(ID, 4.0, 6.0F, true);
        book.record(ID, 9.0, 12.0F, true);
        book.record(ID, 2.0, 3.0F, false);

        StatEntry entry = book.get(ID);
        assertEquals(3, entry.hits());
        assertEquals(2, entry.maceHits());
        assertEquals(9.0, entry.bestFallDistance());
        assertEquals(12.0F, entry.bestMaceDamage());
        assertEquals(3.0F, entry.bestOtherDamage());
    }

    @Test
    void keepsBestWhenLaterHitIsSmaller() {
        StatBook book = new StatBook();
        book.record(ID, 10.0, 8.0F, true);
        book.record(ID, 1.0, 2.0F, true);

        StatEntry entry = book.get(ID);
        assertEquals(10.0, entry.bestFallDistance());
        assertEquals(8.0F, entry.bestMaceDamage());
    }

    @Test
    void resetRemovesOnlyOnce() {
        StatBook book = new StatBook();
        book.record(ID, 1.0, 1.0F, false);

        assertEquals(1, book.size());
        assertTrue(book.reset(ID));
        assertFalse(book.reset(ID));
        assertEquals(0, book.size());
        assertNull(book.get(ID));
    }
}
