package test.source;

import org.junit.jupiter.api.Test;
import source.*;

import static org.junit.jupiter.api.Assertions.*;

class HorseTest {

    @Test
    void testHorseGettersAndSetters() {
        Horse h = new Horse("Spirit", "Mustang", HorseType.HotBlood, 4, Gender.Stallion, "Buckskin", 450.0, 5000.0, HorseCondition.healthy);

        assertEquals("Spirit", h.getName());
        assertEquals("Mustang", h.getBreed());
        assertEquals(HorseType.HotBlood, h.getHorseType());

        h.setPrice(6000.0);
        assertEquals(6000.0, h.getPrice());
    }

    @Test
    void testToString() {
        Horse h = new Horse("Spirit", "Mustang", HorseType.HotBlood, 4, Gender.Stallion, "Buckskin", 450.0, 5000.0, HorseCondition.healthy);
        assertNotNull(h.toString());
        assertTrue(h.toString().contains("Spirit"));
    }
}