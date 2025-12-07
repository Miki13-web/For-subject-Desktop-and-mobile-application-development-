package test.source;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.hibernate.Session;
import org.hibernate.Transaction;
import source.DataGenerator;
import source.StableManager;
import source.HibernateUtil;

import static org.junit.jupiter.api.Assertions.*;

class DataGeneratorTest {

    @BeforeEach
    void cleanDB() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("DELETE FROM Rating").executeUpdate();
        session.createQuery("DELETE FROM Horse").executeUpdate();
        session.createQuery("DELETE FROM Stable").executeUpdate();
        tx.commit();
        session.close();
    }

    @Test
    void testDataGenerator() {
        DataGenerator gen = DataGenerator.getInstance();
        assertNotNull(gen);

        StableManager manager = new StableManager();
        manager.addStable("TestGenStable", 5);

        assertFalse(manager.getAll().isEmpty());
    }
}