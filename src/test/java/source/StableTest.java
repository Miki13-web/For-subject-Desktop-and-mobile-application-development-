package source;

import org.junit.jupiter.api.Test;
import source.Stable;

import static org.junit.jupiter.api.Assertions.*;

class StableTest {

    @Test
    void testStableEntity() {
        Stable stable = new Stable("Test Stable", 10);

        assertEquals("Test Stable", stable.getStableName());
        assertEquals(10, stable.getMaxCapacity());

        stable.setStableName("New Name");
        assertEquals("New Name", stable.getStableName());

        assertTrue(stable.isEmpty());
    }
}