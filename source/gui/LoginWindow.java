package source.gui;

import javax.swing.*;
import java.awt.*;
import source.StableManager;

public class LoginWindow extends JFrame {
    public LoginWindow(StableManager manager) {
        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10));

        JLabel label = new JLabel("Select login mode:", SwingConstants.CENTER);
        JButton adminBtn = new JButton("Log in as ADMIN");
        JButton userBtn = new JButton("Log in as USER");

        //Admin
        adminBtn.addActionListener(e -> {
            new MainWindow(manager, true).setVisible(true);
            this.dispose(); // close login window
        });

        //User
        userBtn.addActionListener(e -> {
            new MainWindow(manager, false).setVisible(true);
            this.dispose();
        });

        add(label);
        add(adminBtn);
        add(userBtn);
    }
}
