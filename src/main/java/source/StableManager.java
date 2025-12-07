package source;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.ArrayList;
import java.util.List;

public class StableManager {

    public StableManager() {}

    public void addStable(String name, int capacity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            // check for duplicate
            Query<Stable> q = session.createQuery("FROM Stable WHERE stableName = :name", Stable.class);
            q.setParameter("name", name);

            if (q.uniqueResult() != null) {
                System.err.println("Stable already exists!");
            } else {
                Stable newStable = new Stable(name, capacity);
                session.persist(newStable);
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
            Query q = session.createQuery("DELETE FROM Stable WHERE stableName = :name");
            q.setParameter("name", name);
            q.executeUpdate();
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
            Query<Stable> q = session.createQuery("FROM Stable s LEFT JOIN FETCH s.horseList WHERE s.stableName = :name", Stable.class);
            q.setParameter("name", name);
            return q.uniqueResult();
        } finally {
            session.close();
        }
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
        if(stables.isEmpty()){
            System.out.println("No Stables in system!");
        } else {
            for(Stable s : stables) {
                System.out.println("Stable: " + s.getStableName() + " | Occupancy: " + (s.getLoad() * 100) + "%");
            }
        }
    }

    public List<Stable> getStablesSortedByLoad() {
        List<Stable> list = getAll();
        list.sort((s1, s2) -> Double.compare(s2.getLoad(), s1.getLoad()));
        return list;
    }

    public void addHorseToStable(String stableName, Horse horse) throws StableException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            // get stable from database
            Query<Stable> q = session.createQuery("FROM Stable WHERE stableName = :name", Stable.class);
            q.setParameter("name", stableName);
            Stable stable = q.uniqueResult();

            if (stable == null) throw new StableException("Stable not found!");

            stable.addHorse(horse);

            session.merge(stable); // merge aktualizuje stan stadniny i dodaje nowego konia (cascade)add new horse and adjust current state

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
            if (horse != null) {
                session.remove(horse);
            } else {
                throw new StableException("Horse not found.");
            }
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
            return session.createQuery("FROM Horse h WHERE h.stable.id = :sid", Horse.class)
                    .setParameter("sid", stableId)
                    .list();
        } finally {
            session.close();
        }
    }

    public List<Horse> searchHorsesInStable(Long stableId, String fragment) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "FROM Horse h WHERE h.stable.id = :sid AND (lower(h.name) LIKE :frag OR lower(h.breed) LIKE :frag)";
            return session.createQuery(hql, Horse.class)
                    .setParameter("sid", stableId)
                    .setParameter("frag", "%" + fragment.toLowerCase() + "%")
                    .list();
        } finally {
            session.close();
        }
    }
}