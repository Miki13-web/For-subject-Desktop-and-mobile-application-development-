package source;
import source.HibernateUtil;
import source.StableManager;
import source.gui.LoginWindow;
import javax.swing.SwingUtilities;

public class main {
    public static void main(String[] args) {
        // Init Hibernate
        HibernateUtil.getSessionFactory();

        StableManager manager = new StableManager();

        // if database is empty
        if (manager.getAll().isEmpty()) {
            System.out.println("Database is empty. Populating with DataGenerator...");
            DataGenerator.getInstance().generateSampleData(manager);
        } else {
            System.out.println("Database already contains data. Skipping generation.");
        }

        // Start GUI
        SwingUtilities.invokeLater(() -> {
            new LoginWindow(manager).setVisible(true);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(HibernateUtil::shutdown));
    }
}