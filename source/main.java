import source.DataGenerator;
import source.StableManager;
import source.gui.LoginWindow;

// W Main.java
public static void main(String[] args) {
    //generating data
    StableManager manager = DataGenerator.getInstance().getSampleData();

    //starting GUI
    javax.swing.SwingUtilities.invokeLater(() -> {
        new LoginWindow(manager).setVisible(true);
    });
}