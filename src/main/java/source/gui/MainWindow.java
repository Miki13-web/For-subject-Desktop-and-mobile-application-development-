package source.gui;

import source.*;
import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        setSize(1000, 750); // Trochę większe okno
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- TOP PANEL ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.add(new JLabel("User: " + (isAdmin ? "Admin" : "User")));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(255, 100, 100));
        logoutBtn.addActionListener(e -> handleLogout());
        topPanel.add(logoutBtn);

        topPanel.add(new JLabel("Search:"));
        filterField = new JTextField(10);
        filterField.addActionListener(e -> handleNameSearch());
        topPanel.add(filterField);

        JButton searchBtn = new JButton("Search DB");
        searchBtn.addActionListener(e -> handleNameSearch());
        topPanel.add(searchBtn);

        topPanel.add(new JLabel("Status:"));
        statusCombo = new JComboBox<>(HorseCondition.values());
        statusCombo.addActionListener(e -> handleStatusFilter());
        topPanel.add(statusCombo);

        JButton resetBtn = new JButton("Reset");
        resetBtn.addActionListener(e -> refreshCurrentStableData());
        topPanel.add(resetBtn);

        JButton statsBtn = new JButton("Show Breed Stats");
        statsBtn.addActionListener(e -> showStats());
        topPanel.add(statsBtn);

        add(topPanel, BorderLayout.NORTH);

        // --- CENTER ---
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
        splitPane.setDividerLocation(300);
        add(splitPane, BorderLayout.CENTER);

        // --- BOTTOM PANEL ---
        JPanel buttonPanel = new JPanel();

        JButton rateBtn = new JButton("Rate Horse");
        rateBtn.addActionListener(e -> handleRateHorse());
        buttonPanel.add(rateBtn);

        JButton exportBtn = new JButton("Export CSV");
        exportBtn.addActionListener(e -> handleExportCSV());
        buttonPanel.add(exportBtn);

        // NOWE PRZYCISKI BINARNE
        JButton binSaveBtn = new JButton("Save Binary");
        binSaveBtn.addActionListener(e -> handleBinarySave());
        buttonPanel.add(binSaveBtn);

        if (isAdmin) {
            JButton importBtn = new JButton("Import CSV");
            importBtn.addActionListener(e -> handleImportCSV());
            buttonPanel.add(importBtn);

            JButton binLoadBtn = new JButton("Load Binary");
            binLoadBtn.addActionListener(e -> handleBinaryLoad());
            buttonPanel.add(binLoadBtn);

            JButton addStableBtn = new JButton("Add Stable");
            JButton removeStableBtn = new JButton("Remove Stable");
            JButton addHorseBtn = new JButton("Add Horse");
            JButton removeHorseBtn = new JButton("Remove Horse");

            addStableBtn.addActionListener(e -> handleAddStable());
            removeStableBtn.addActionListener(e -> handleRemoveStable());
            addHorseBtn.addActionListener(e -> handleAddHorse());
            removeHorseBtn.addActionListener(e -> handleRemoveHorse());

            buttonPanel.add(new JSeparator(SwingConstants.VERTICAL));
            buttonPanel.add(addStableBtn);
            buttonPanel.add(removeStableBtn);
            buttonPanel.add(addHorseBtn);
            buttonPanel.add(removeHorseBtn);
        }

        JButton sortStablesBtn = new JButton("Sort Stables");
        sortStablesBtn.addActionListener(e -> {
            stableModel.setData(manager.getStablesSortedByLoad());
        });
        buttonPanel.add(sortStablesBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    // --- LOGIC ---

    private void refreshCurrentStableData() {
        if (currentStable != null) {
            List<Horse> horses = manager.getHorsesFromStable(currentStable.getId());
            horseModel.setData(horses);
            currentStable.setHorseList(horses);
            stableTable.repaint();
        }
    }

    private void showStats() {
        Map<String, Long> stats = manager.getHorseCountByBreed();
        StringBuilder sb = new StringBuilder("Horses per Breed (Criteria API):\n");
        stats.forEach((breed, count) -> sb.append(breed).append(": ").append(count).append("\n"));
        JOptionPane.showMessageDialog(this, sb.toString(), "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleRateHorse() {
        if (horseTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Select a horse to rate first.");
            return;
        }

        String scoreStr = JOptionPane.showInputDialog(this, "Enter Rating (0-5):");
        if (scoreStr != null && !scoreStr.trim().isEmpty()) {
            try {
                int score = Integer.parseInt(scoreStr);
                String desc = JOptionPane.showInputDialog(this, "Optional Description:");

                Horse h = horseModel.getObjectAt(horseTable.getSelectedRow());
                manager.addRatingToHorse(h.getId(), score, desc == null ? "" : desc);

                refreshCurrentStableData();
                JOptionPane.showMessageDialog(this, "Rating added!");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Rating must be a number!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void handleExportCSV() {
        if (currentStable == null) {
            JOptionPane.showMessageDialog(this, "Select a stable first.");
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String path = file.getAbsolutePath();
            if (!path.toLowerCase().endsWith(".csv")) path += ".csv";
            try {
                manager.exportStableToCSV(currentStable.getId(), path);
                JOptionPane.showMessageDialog(this, "Export successful to: " + path);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error exporting: " + e.getMessage());
            }
        }
    }

    private void handleImportCSV() {
        if (currentStable == null) {
            JOptionPane.showMessageDialog(this, "Select a stable to import into.");
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                manager.importStableFromCSV(currentStable.getStableName(), file.getAbsolutePath());
                refreshCurrentStableData();
                JOptionPane.showMessageDialog(this, "Import successful!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error importing: " + e.getMessage());
            }
        }
    }

    // --- OBSŁUGA BINARNA ---
    private void handleBinarySave() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String path = file.getAbsolutePath();
            if (!path.toLowerCase().endsWith(".bin")) path += ".bin";
            try {
                manager.saveToBinary(path);
                JOptionPane.showMessageDialog(this, "Binary backup saved!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void handleBinaryLoad() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                manager.loadFromBinary(fileChooser.getSelectedFile().getAbsolutePath());
                stableModel.setData(manager.getAll());
                JOptionPane.showMessageDialog(this, "Binary backup restored to DB!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void handleAddStable() {
        String name = JOptionPane.showInputDialog(this, "Stable Name:");
        if (name != null && !name.trim().isEmpty()) {
            String capStr = JOptionPane.showInputDialog(this, "Capacity:");
            try {
                manager.addStable(name, Integer.parseInt(capStr));
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
        if (currentStable == null) return;
        try {
            Horse h = new Horse("NewHorse", "Mix", HorseType.HotBlood, 5, Gender.Mare, "Brown", 500, 1000, HorseCondition.healthy);
            manager.addHorseToStable(currentStable.getStableName(), h);
            refreshCurrentStableData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
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
            horseModel.setData(manager.searchHorsesInStable(currentStable.getId(), filterField.getText()));
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