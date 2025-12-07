package test.source;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.hibernate.Session;
import org.hibernate.Transaction;
import source.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StableManagerTest {

    private StableManager manager;

    @BeforeEach
    void setUp() {
        manager = new StableManager();
        cleanDatabase(); // clear before each test
    }

    private void cleanDatabase() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("DELETE FROM Rating").executeUpdate();
        session.createQuery("DELETE FROM Horse").executeUpdate();
        session.createQuery("DELETE FROM Stable").executeUpdate();
        tx.commit();
        session.close();
    }

    @Test
    void testAddStable() {
        manager.addStable("Hubertus", 10);

        List<Stable> stables = manager.getAll();
        assertEquals(1, stables.size());
        assertEquals("Hubertus", stables.get(0).getStableName());
    }

    @Test
    void testRemoveStable() {
        manager.addStable("Hubertus", 10);
        manager.removeStable("Hubertus");

        List<Stable> stables = manager.getAll();
        assertTrue(stables.isEmpty(), "Stable should be empty after removal");
    }

    @Test
    void testAddHorseToStable() throws StableException {
        manager.addStable("MyStable", 5);

        Horse h = new Horse("Roach", "Mix", HorseType.HotBlood, 5, Gender.Mare, "Br", 500, 1000, HorseCondition.healthy);
        manager.addHorseToStable("MyStable", h);

        Stable s = manager.getAll().get(0);
        List<Horse> horses = manager.getHorsesFromStable(s.getId());

        assertEquals(1, horses.size());
        assertEquals("Roach", horses.get(0).getName());
    }

    @Test
    void testAddHorseCapacityExceeded() throws StableException {
        manager.addStable("TinyStable", 1);

        Horse h1 = new Horse("H1", "X", HorseType.HotBlood, 1, Gender.Mare, "C", 1, 1, HorseCondition.healthy);
        Horse h2 = new Horse("H2", "X", HorseType.HotBlood, 1, Gender.Mare, "C", 1, 1, HorseCondition.healthy);

        manager.addHorseToStable("TinyStable", h1);

        assertThrows(StableException.class, () -> {
            manager.addHorseToStable("TinyStable", h2);
        });
    }

    @Test
    void testGetAll() {
        manager.addStable("A", 1);
        manager.addStable("B", 2);

        List<Stable> all = manager.getAll();
        assertEquals(2, all.size());
    }

    @AfterAll
    static void tearDown() {
        HibernateUtil.shutdown();
    }
}