package source;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StableTest {

    private Stable stable;
    private Horse h1;
    private Horse h2;

    @BeforeEach
    void setUp() {
        // stable with low capacity to test overload
        stable = new Stable("Test Stable", 2);

        //test for constructor
        // Name, Breed, Type, Age, Gender, Color, Weight, Price, Status
        h1 = new Horse("Roach", "Arab", HorseType.HotBlood, 5, Gender.Gelding, "Brown", 500.0, 1000.0, HorseCondition.healthy);
        h2 = new Horse("Kelpie", "Thoroughbred", HorseType.HotBlood, 7, Gender.Mare, "Black", 550.0, 2000.0, HorseCondition.healthy);
    }

    @Test
    void testAddHorseSuccess() throws StableException {
        stable.addHorse(h1);
        assertEquals(1, stable.getHorseList().size(), "Stable should contain 1 horse after adding");
        assertTrue(stable.getHorseList().contains(h1), "Stable should contain the specific horse added");
    }

    @Test
    void testAddHorseDuplicate() throws StableException {
        stable.addHorse(h1);
        // duplicate horse test
        Exception exception = assertThrows(StableException.class, () -> stable.addHorse(h1));
        assertTrue(exception.getMessage().contains("already") || exception.getMessage().contains("juz jest"));
    }

    @Test
    void testAddHorseCapacityExceeded() throws StableException {
        stable.addHorse(h1);
        stable.addHorse(h2); // full stable

        Horse h3 = new Horse("Extra", "Pony", HorseType.ColdBlood, 2, Gender.Stallion, "White", 200.0, 500.0, HorseCondition.healthy);

        // adding to full
        Exception exception = assertThrows(StableException.class, () -> stable.addHorse(h3));
        assertTrue(exception.getMessage().contains("full") || exception.getMessage().contains("pelna"));
    }

    @Test
    void testRemoveHorseSuccess() throws StableException {
        stable.addHorse(h1);
        stable.removeHorse(h1);
        assertTrue(stable.isEmpty(), "Stable should be empty after removing the only horse");
    }

    @Test
    void testRemoveHorseNotFound() {
        // removing not existing horse
        assertThrows(StableException.class, () -> stable.removeHorse(h1));
    }

    @Test
    void testSortByPrice() throws StableException {
        stable.addHorse(h1); // price 1000.0
        stable.addHorse(h2); // price 2000.0

        stable.removeHorse(h2);
        Horse cheapHorse = new Horse("Cheap", "Pony", HorseType.ColdBlood, 10, Gender.Gelding, "Gray", 300.0, 100.0, HorseCondition.sick);
        stable.addHorse(cheapHorse);

        List<Horse> sorted = stable.sortByPrice();

        assertEquals(cheapHorse, sorted.get(0), "Cheapest horse should be first");
        assertEquals(h1, sorted.get(1), "More expensive horse should be second");
    }

    @Test
    void testSearchPartial() throws StableException {
        stable.addHorse(h1); //name: "Roach"

        List<Horse> results = stable.searchPartial("oa"); // Fragment "oa" pasuje do "Roach"
        assertEquals(1, results.size());
        assertEquals("Roach", results.get(0).getName());
    }

    @Test
    void testCountByStatus() throws StableException {
        h1.setStatus(HorseCondition.sick);
        stable.addHorse(h1);
        stable.addHorse(h2); // h2  healthy

        assertEquals(1, stable.countByStatus(HorseCondition.sick));
        assertEquals(1, stable.countByStatus(HorseCondition.healthy));
    }

    @Test
    void testChangeStatus() throws StableException {
        stable.addHorse(h1);
        stable.changeStatus(h1, HorseCondition.sold);
        assertEquals(HorseCondition.sold, h1.getStatus());
    }

    @Test
    void testStableGettersAndSetters() {
        stable.setStableName("New Name");
        assertEquals("New Name", stable.getStableName());

        stable.setMaxCapacity(100);
        assertEquals(100, stable.getMaxCapacity());
    }

    @Test
    void testSummaryMethods() throws StableException {
        // Add horses to make summary print something interesting
        stable.addHorse(h1);
        stable.addHorse(h2);

        // Call summary() just to execute lines (increase coverage)
        // We don't assert console output usually, but we need to run the code
        stable.summary();

        // Test empty stable summary
        Stable emptyStable = new Stable("Empty", 10);
        emptyStable.summary();
    }

    @Test
    void testSickHorseMethod() throws StableException {
        // This tests specifically the sickHorse logic in Stable
        stable.addHorse(h1);
        stable.sickHorse(h1);

        assertEquals(HorseCondition.sick, h1.getStatus());
        assertFalse(stable.getHorseList().contains(h1), "Sick horse should be removed");
    }
}