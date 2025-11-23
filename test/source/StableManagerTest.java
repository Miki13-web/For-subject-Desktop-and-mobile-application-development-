package source;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StableManagerTest {

    private StableManager manager;

    @BeforeEach
    void setUp() {
        manager = new StableManager();
    }

    @Test
    void testAddStable() {
        manager.addStable("Hubertus", 10);
        assertNotNull(manager.getStable("Hubertus"), "Stable should be retrievable after adding");
        assertEquals(10, manager.getStable("Hubertus").getMaxCapacity());
    }

    @Test
    void testRemoveStable() {
        manager.addStable("Hubertus", 10);
        manager.removeStable("Hubertus");
        assertNull(manager.getStable("Hubertus"), "Stable should be null after removal");
    }

    @Test
    void testFindEmpty() throws StableException {
        manager.addStable("EmptyOne", 10);
        manager.addStable("FullOne", 1);

        // Add a horse to "FullOne" so it is not empty
        Stable fullStable = manager.getStable("FullOne");
        fullStable.addHorse(new Horse("Horse", "Breed", HorseType.HotBlood, 1, Gender.Mare, "Color", 100.0, 100.0, HorseCondition.healthy));

        List<Stable> emptyStables = manager.findEmpty();

        assertEquals(1, emptyStables.size(), "Should find exactly one empty stable");
        assertEquals("EmptyOne", emptyStables.get(0).getStableName());
    }

    @Test
    void testGetAll() {
        manager.addStable("A", 1);
        manager.addStable("B", 2);

        List<Stable> all = manager.getAll();
        assertEquals(2, all.size(), "Should return all added stables");
    }

    @Test
    void testSummaryManager() {
        manager.addStable("A", 10);
        manager.addStable("B", 20);

        // Execute the print loop to get line coverage
        manager.summaryManager();

        // Test empty manager summary
        StableManager emptyManager = new StableManager();
        emptyManager.summaryManager();
    }

    @Test
    void testAddStableDuplicate() {
        manager.addStable("A", 10);
        // Try adding duplicate (should trigger the System.err.println line)
        manager.addStable("A", 20);

        assertEquals(10, manager.getStable("A").getMaxCapacity(), "Capacity should not change on duplicate add");
    }

    @Test
    void testRemoveStableNotFound() {
        // Try removing non-existing stable (triggers the else branch)
        manager.removeStable("Ghost");
    }
}