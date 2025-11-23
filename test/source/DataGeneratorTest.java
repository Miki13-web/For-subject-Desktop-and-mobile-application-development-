package source;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DataGeneratorTest {

    @Test
    void testDataGenerator() {
        // test if Singleton is working
        DataGenerator gen1 = DataGenerator.getInstance();
        DataGenerator gen2 = DataGenerator.getInstance();

        assertNotNull(gen1);
        assertSame(gen1, gen2, "Obie referencje powinny wskazywać na ten sam obiekt (Singleton)");

        // test data generating
        StableManager manager = gen1.getSampleData();
        assertNotNull(manager);

        //tetst adding stables
        assertFalse(manager.getAll().isEmpty());
        assertTrue(manager.getAll().size() >= 3);
    }
}