package source.gui;

import source.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainWindow extends JFrame {
    private StableManager manager;
    private boolean isAdmin;

    private JTable stableTable;
    private JTable horseTable;
    private HorseTableModel horseModel;
    private StableTableModel stableModel;

    private JTextField filterField;
    private JComboBox<HorseCondition> statusCombo;
    private Stable currentStable = null;

    public MainWindow(StableManager manager, boolean isAdmin) {
        this.manager = manager;
        this.isAdmin = isAdmin;

        setTitle("Stable Manager - " + (isAdmin ? "ADMIN" : "USER") + " (DB Connected)");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // TOP PANEL
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.add(new JLabel("Logged as: " + (isAdmin ? "Administrator" : "User")));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(255, 100, 100));
        logoutBtn.addActionListener(e -> handleLogout());
        topPanel.add(logoutBtn);

        topPanel.add(new JLabel("Search Name:"));
        filterField = new JTextField(15);
        filterField.addActionListener(e -> handleNameSearch());
        topPanel.add(filterField);

        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> handleNameSearch());
        topPanel.add(searchBtn);

        topPanel.add(new JLabel("Status:"));
        statusCombo = new JComboBox<>(HorseCondition.values());
        statusCombo.addActionListener(e -> handleStatusFilter());
        topPanel.add(statusCombo);

        JButton resetBtn = new JButton("Reset");
        resetBtn.addActionListener(e -> refreshCurrentStableData());
        topPanel.add(resetBtn);

        add(topPanel, BorderLayout.NORTH);

        // CENTER
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        List<Stable> stables = manager.getAll();
        stableModel = new StableTableModel(stables);
        stableTable = new JTable(stableModel);

        stableTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && stableTable.getSelectedRow() != -1) {
                currentStable = stableModel.getObjectAt(stableTable.getSelectedRow());
                refreshCurrentStableData();
            }
        });
        splitPane.setLeftComponent(new JScrollPane(stableTable));

        horseModel = new HorseTableModel(new ArrayList<>());
        horseTable = new JTable(horseModel);
        splitPane.setRightComponent(new JScrollPane(horseTable));
        splitPane.setDividerLocation(400);
        add(splitPane, BorderLayout.CENTER);

        // BOTTOM
        JPanel buttonPanel = new JPanel();
        if (isAdmin) {
            JButton addStableBtn = new JButton("Add Stable");
            JButton removeStableBtn = new JButton("Remove Stable");
            JButton addHorseBtn = new JButton("Add Horse");
            JButton removeHorseBtn = new JButton("Remove Horse");

            addStableBtn.addActionListener(e -> handleAddStable());
            removeStableBtn.addActionListener(e -> handleRemoveStable());
            addHorseBtn.addActionListener(e -> handleAddHorse());
            removeHorseBtn.addActionListener(e -> handleRemoveHorse());

            buttonPanel.add(addStableBtn);
            buttonPanel.add(removeStableBtn);
            buttonPanel.add(new JSeparator(SwingConstants.VERTICAL));
            buttonPanel.add(addHorseBtn);
            buttonPanel.add(removeHorseBtn);
        }

        JButton sortStablesBtn = new JButton("Sort Stables by Load");
        sortStablesBtn.addActionListener(e -> {
            List<Stable> sorted = manager.getStablesSortedByLoad();
            stableModel.setData(sorted);
        });
        buttonPanel.add(sortStablesBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void refreshCurrentStableData() {
        if (currentStable != null) {
            List<Horse> horses = manager.getHorsesFromStable(currentStable.getId());
            horseModel.setData(horses);
            currentStable.setHorseList(horses);
            stableTable.repaint();
        }
    }

    private void handleAddStable() {
        JTextField nameField = new JTextField();
        JTextField capacityField = new JTextField();
        Object[] message = { "Stable Name:", nameField, "Max Capacity:", capacityField };
        int option = JOptionPane.showConfirmDialog(this, message, "Add New Stable", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                manager.addStable(nameField.getText(), Integer.parseInt(capacityField.getText()));
                stableModel.setData(manager.getAll());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void handleRemoveStable() {
        if (stableTable.getSelectedRow() == -1) return;
        Stable s = stableModel.getObjectAt(stableTable.getSelectedRow());
        manager.removeStable(s.getStableName());
        stableModel.setData(manager.getAll());
        horseModel.setData(new ArrayList<>());
        currentStable = null;
    }

    private void handleAddHorse() {
        if (currentStable == null) {
            JOptionPane.showMessageDialog(this, "Select a stable first!");
            return;
        }

        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JComboBox<Gender> genderCombo = new JComboBox<>(Gender.values());
        JComboBox<HorseType> typeCombo = new JComboBox<>(HorseType.values());
        JComboBox<HorseCondition> statusCombo = new JComboBox<>(HorseCondition.values());

        Object[] message = { "Name:", nameField, "Age:", ageField, "Gender:", genderCombo, "Type:", typeCombo, "Status:", statusCombo };
        int option = JOptionPane.showConfirmDialog(this, message, "Add Horse", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                Horse h = new Horse(nameField.getText(), "Mix",
                        (HorseType) typeCombo.getSelectedItem(),
                        Integer.parseInt(ageField.getText()),
                        (Gender) genderCombo.getSelectedItem(), "Brown", 500.0, 1000.0,
                        (HorseCondition) statusCombo.getSelectedItem());

                manager.addHorseToStable(currentStable.getStableName(), h);
                refreshCurrentStableData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }

    private void handleRemoveHorse() {
        if (horseTable.getSelectedRow() == -1) return;
        try {
            Horse h = horseModel.getObjectAt(horseTable.getSelectedRow());
            manager.removeHorse(h.getId());
            refreshCurrentStableData();
        } catch (StableException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void handleNameSearch() {
        if (currentStable != null) {
            List<Horse> results = manager.searchHorsesInStable(currentStable.getId(), filterField.getText());
            horseModel.setData(results);
        }
    }

    private void handleStatusFilter() {
        if (currentStable != null) {
            refreshCurrentStableData();
            HorseCondition status = (HorseCondition) statusCombo.getSelectedItem();
            List<Horse> filtered = horseModel.getRows().stream()
                    .filter(h -> h.getStatus() == status)
                    .collect(Collectors.toList());
            horseModel.setData(filtered);
        }
    }

    private void handleLogout() {
        this.dispose();
        new LoginWindow(manager).setVisible(true);
    }
}