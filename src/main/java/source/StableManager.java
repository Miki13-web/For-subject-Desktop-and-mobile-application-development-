package source;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StableManager {

    public StableManager() {}

    // --- STABLES (CRUD) ---

    public void addStable(String name, int capacity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if (session.createQuery("FROM Stable WHERE stableName = :name", Stable.class)
                    .setParameter("name", name).uniqueResult() == null) {
                session.persist(new Stable(name, capacity));
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void removeStable(String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.createQuery("DELETE FROM Stable WHERE stableName = :name")
                    .setParameter("name", name).executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public List<Stable> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM Stable", Stable.class).list();
        } finally {
            session.close();
        }
    }

    public Stable getStable(String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM Stable s LEFT JOIN FETCH s.horseList WHERE s.stableName = :name", Stable.class)
                    .setParameter("name", name)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

    public List<Stable> getStablesSortedByLoad() {
        List<Stable> list = getAll();
        list.sort((s1, s2) -> Double.compare(s2.getLoad(), s1.getLoad()));
        return list;
    }

    public List<Stable> findEmpty() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("SELECT s FROM Stable s WHERE size(s.horseList) = 0", Stable.class).list();
        } finally {
            session.close();
        }
    }

    public void summaryManager() {
        List<Stable> stables = getAll();
        System.out.println("=======Summary Stable Manager=======");
        for(Stable s : stables) {
            System.out.println("Stable: " + s.getStableName() + " | Occupancy: " + (s.getLoad() * 100) + "%");
        }
    }

    // --- HORSES ---

    public void addHorseToStable(String stableName, Horse horse) throws StableException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Stable stable = session.createQuery("FROM Stable WHERE stableName = :name", Stable.class)
                    .setParameter("name", stableName).uniqueResult();

            if (stable == null) throw new StableException("Stable not found!");

            Long count = session.createQuery("SELECT count(h) FROM Horse h WHERE h.stable = :s", Long.class)
                    .setParameter("s", stable).uniqueResult();

            if (count >= stable.getMaxCapacity()) throw new StableException("Stable is full!");

            horse.setStable(stable);
            session.persist(horse);
            tx.commit();
        } catch (StableException se) {
            if (tx != null) tx.rollback();
            throw se;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new StableException("DB Error: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    public void removeHorse(Long horseId) throws StableException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Horse horse = session.get(Horse.class, horseId);
            if (horse != null) session.remove(horse);
            else throw new StableException("Horse not found.");
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new StableException("Error removing horse: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    public List<Horse> getHorsesFromStable(Long stableId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM Horse h LEFT JOIN FETCH h.ratings WHERE h.stable.id = :sid", Horse.class)
                    .setParameter("sid", stableId)
                    .list();
        } finally {
            session.close();
        }
    }

    public List<Horse> searchHorsesInStable(Long stableId, String fragment) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM Horse h WHERE h.stable.id = :sid AND (lower(h.name) LIKE :frag OR lower(h.breed) LIKE :frag)", Horse.class)
                    .setParameter("sid", stableId)
                    .setParameter("frag", "%" + fragment.toLowerCase() + "%")
                    .list();
        } finally {
            session.close();
        }
    }

    // --- RATINGS ---

    public void addRatingToHorse(Long horseId, int score, String description) throws StableException {
        if (score < 0 || score > 5) throw new StableException("Rating must be between 0 and 5");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Horse horse = session.get(Horse.class, horseId);
            if (horse == null) throw new StableException("Horse not found");
            Rating rating = new Rating(score, horse, description);
            session.persist(rating);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw new StableException("Error adding rating: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    // --- CSV IMPORT / EXPORT ---

    public void exportStableToCSV(Long stableId, String filePath) throws IOException {
        List<Horse> horses = getHorsesFromStable(stableId);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Name,Breed,Type,Age,Gender,Color,Weight,Price,Status");
            writer.newLine();
            for (Horse h : horses) {
                String line = String.format("%s,%s,%s,%d,%s,%s,%.1f,%.1f,%s",
                        h.getName(), h.getBreed(), h.getHorseType(), h.getAge(),
                        h.getGender(), h.getColor(), h.getWeight(), h.getPrice(), h.getStatus());
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public void importStableFromCSV(String stableName, String filePath) throws IOException, StableException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 9) {
                    Horse h = new Horse(
                            parts[0].trim(), parts[1].trim(),
                            HorseType.valueOf(parts[2].trim()),
                            Integer.parseInt(parts[3].trim()),
                            Gender.valueOf(parts[4].trim()),
                            parts[5].trim(),
                            Double.parseDouble(parts[6].trim()),
                            Double.parseDouble(parts[7].trim()),
                            HorseCondition.valueOf(parts[8].trim())
                    );
                    addHorseToStable(stableName, h);
                }
            }
        }
    }

    // --- SERIALIZATION (BINARY) ---

    public void saveToBinary(String filePath) throws IOException {
        List<Stable> allData = getAll();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            allData = session.createQuery("FROM Stable s LEFT JOIN FETCH s.horseList", Stable.class).list();

            for(Stable s : allData) {
                for(Horse h : s.getHorseList()) {
                    h.getRatings().size();
                }
            }
        } finally {
            session.close();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(allData);
        }
    }

    public void loadFromBinary(String filePath) throws IOException, ClassNotFoundException {
        List<Stable> loadedStables;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            loadedStables = (List<Stable>) ois.readObject();
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (Stable s : loadedStables) {

                if (session.createQuery("FROM Stable WHERE stableName = :name", Stable.class)
                        .setParameter("name", s.getStableName()).uniqueResult() == null) {

                    s.setId(null);
                    for(Horse h : s.getHorseList()) {
                        h.setId(null);
                        for(Rating r : h.getRatings()) {
                            r.setId(null);
                        }
                    }
                    session.persist(s);
                }
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new IOException("DB Import error: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    // --- CRITERIA API ---

    public Map<String, Long> getHorseCountByBreed() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Map<String, Long> stats = new HashMap<>();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
            Root<Horse> root = query.from(Horse.class);
            query.multiselect(root.get("breed"), builder.count(root));
            query.groupBy(root.get("breed"));
            List<Object[]> results = session.createQuery(query).getResultList();
            for (Object[] row : results) {
                stats.put((String) row[0], (Long) row[1]);
            }
        } finally {
            session.close();
        }
        return stats;
    }
}