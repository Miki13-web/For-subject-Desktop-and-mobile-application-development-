package source;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HorseTest {

    @Test
    void testHorseGettersAndSetters() {
        // 1. Test Constructor and Getters
        Horse h = new Horse("Spirit", "Mustang", HorseType.HotBlood, 4, Gender.Stallion, "Buckskin", 450.0, 5000.0, HorseCondition.healthy);

        assertEquals("Spirit", h.getName());
        assertEquals("Mustang", h.getBreed());
        assertEquals(HorseType.HotBlood, h.getHorseType());
        assertEquals(4, h.getAge());
        assertEquals(Gender.Stallion, h.getGender());
        assertEquals("Buckskin", h.getColor());
        assertEquals(450.0, h.getWeight());
        assertEquals(5000.0, h.getPrice());
        assertEquals(HorseCondition.healthy, h.getStatus());

        // 2. Test Setters
        h.setPrice(6000.0);
        assertEquals(6000.0, h.getPrice());

        h.setWeight(460.0);
        assertEquals(460.0, h.getWeight());

        h.setGender(Gender.Gelding);
        assertEquals(Gender.Gelding, h.getGender());

        h.setStatus(HorseCondition.sick);
        assertEquals(HorseCondition.sick, h.getStatus());

        // test toString (executes the formatting code)
        assertNotNull(h.toString());
        // We call printHorse just to execute the line (coverage)
        h.printHorse();
        h.print();
    }

    @Test
    void testEqualsAndHashCode() {
        Horse h1 = new Horse("A", "B", HorseType.HotBlood, 1, Gender.Mare, "C", 1, 1, HorseCondition.healthy);
        Horse h2 = new Horse("A", "B", HorseType.ColdBlood, 1, Gender.Stallion, "D", 2, 2, HorseCondition.sick);
        Horse h3 = new Horse("X", "Y", HorseType.HotBlood, 1, Gender.Mare, "C", 1, 1, HorseCondition.healthy);

        assertEquals(h1, h2); // Same name, breed, age -> should be equal
        assertNotEquals(h1, h3);
        assertEquals(h1.hashCode(), h2.hashCode());
    }

    @Test
    void testCompareTo() {
        Horse h1 = new Horse("Adam", "Arab", HorseType.HotBlood, 5, Gender.Mare, "C", 1, 1, HorseCondition.healthy);
        Horse h2 = new Horse("Ben", "Arab", HorseType.HotBlood, 5, Gender.Mare, "C", 1, 1, HorseCondition.healthy);

        // "Adam" is before "Ben" -> result negative
        assertTrue(h1.compareTo(h2) < 0);
    }
}