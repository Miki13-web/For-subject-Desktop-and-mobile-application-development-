package source.gui;

import source.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

public class MainWindow extends JFrame {
    private StableManager manager;
    private boolean isAdmin;

    //GUI elements
    private JTable stableTable;
    private JTable horseTable;
    private HorseTableModel horseModel;
    private StableTableModel stableModel;

    // Filter fields
    private JTextField filterField;
    private JComboBox<HorseCondition> statusCombo;

    //currently chosen stable
    private Stable currentStable = null;

    public MainWindow(StableManager manager, boolean isAdmin) {
        this.manager = manager;
        this.isAdmin = isAdmin;

        setTitle("Stable Manager - " + (isAdmin ? "ADMIN" : "USER"));
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //preparing panels
        // top panel filters
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.add(new JLabel("Logged as: " + (isAdmin ? "Administrator" : "User")));

        // logout
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(255, 100, 100));
        logoutBtn.addActionListener(e -> handleLogout());
        topPanel.add(logoutBtn);

        //filter Textbox (search by name)
        topPanel.add(new JLabel("Search Name:"));
        filterField = new JTextField(15);
        filterField.setToolTipText("Type name and press Enter");
        filterField.addActionListener(e -> handleNameSearch()); // Action on Enter key
        topPanel.add(filterField);

        // state Combobox (Filter by status)
        topPanel.add(new JLabel("Status:"));
        statusCombo = new JComboBox<>(HorseCondition.values());
        statusCombo.addActionListener(e -> handleStatusFilter()); // Action on selection change
        topPanel.add(statusCombo);

        // reset button clears filters
        JButton resetBtn = new JButton("Reset Filters");
        resetBtn.addActionListener(e -> resetFilters());
        topPanel.add(resetBtn);

        add(topPanel, BorderLayout.NORTH);

        // center/middle panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        //lef side Stables
        List<Stable> stables = manager.getAll();
        stableModel = new StableTableModel(stables);
        stableTable = new JTable(stableModel);

        //events
        stableTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && stableTable.getSelectedRow() != -1) {
                //take chosen stable
                currentStable = stableModel.getObjectAt(stableTable.getSelectedRow());
                //resetFilters(); // Show all horses when switching stables
                //refresh right side
                horseModel.setData(currentStable.getHorseList());
            }
        });
        splitPane.setLeftComponent(new JScrollPane(stableTable));

        // Right side: Horses
        horseModel = new HorseTableModel(new ArrayList<>());
        horseTable = new JTable(horseModel);
        splitPane.setRightComponent(new JScrollPane(horseTable));

        splitPane.setDividerLocation(400); //split window
        add(splitPane, BorderLayout.CENTER);

        //buttons panel
        JPanel buttonPanel = new JPanel();

        if(isAdmin){
            JButton addStableBtn = new JButton("Add Stable");
            JButton addHorseBtn = new JButton("Add Horse");
            JButton removeHorseBtn = new JButton("Remove Horse");
            JButton removeStableBtn = new JButton("Remove Stable");

            //buttons logic
            addStableBtn.addActionListener(e -> handleAddStable());
            addHorseBtn.addActionListener(event -> handleAddHorse());
            removeHorseBtn.addActionListener(event -> {
                try {
                    handleRemoveHorse();
                } catch (StableException e) {
                    throw new RuntimeException(e);
                }
            });
            removeStableBtn.addActionListener(e -> handleRemoveStable());

            buttonPanel.add(addStableBtn);
            buttonPanel.add(removeStableBtn);
            buttonPanel.add(new JSeparator(SwingConstants.VERTICAL)); // Ozdobnik
            buttonPanel.add(addHorseBtn);
            buttonPanel.add(removeHorseBtn);
        } else{
            //user buttons
            JButton sortHorseBtn = new JButton("Sort Horse by price");
            sortHorseBtn.addActionListener(e -> {
                if(currentStable != null) {
                    horseModel.setData(currentStable.sortByPrice());
                }
            });
            buttonPanel.add(sortHorseBtn);
        }

        add(buttonPanel, BorderLayout.SOUTH);
    }

    //methods logic
    private void handleAddStable() {
        JTextField nameField = new JTextField();
        JTextField capacityField = new JTextField();

        Object[] message = {
                "Stable Name:", nameField,
                "Max Capacity:", capacityField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Stable", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                int capacity = Integer.parseInt(capacityField.getText());

                manager.addStable(name, capacity);

                // refresh stables
                stableModel.setData(manager.getAll());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Capacity must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRemoveStable() {
        int selectedRow = stableTable.getSelectedRow();
        if (selectedRow == -1) return;

        Stable stableToRemove = stableModel.getObjectAt(selectedRow);
        manager.removeStable(stableToRemove.getStableName());

        // refresh view
        stableModel.setData(manager.getAll());
        horseModel.setData(new ArrayList<>()); // clear horse table
        currentStable = null;
    }

    private void handleRemoveHorse() throws StableException {
        if (currentStable == null) return;
        int selectedRow = horseTable.getSelectedRow();
        if (selectedRow == -1) return;

        Horse h = horseModel.getObjectAt(selectedRow);
        currentStable.removeHorse(h);

        // refresh views, occupancy changed
        horseModel.setData(currentStable.getHorseList());
        stableModel.fireTableDataChanged();
    }

    private void handleAddHorse() {
        if (currentStable == null) {
            JOptionPane.showMessageDialog(this, "Select a stable first!");
            return;
        }

        //form for adding horse
        JTextField nameField = new JTextField();
        JTextField breedField = new JTextField();
        JTextField ageField = new JTextField();
        JComboBox<Gender> genderCombo = new JComboBox<>(Gender.values());
        JTextField colorField = new JTextField();
        JComboBox<HorseType> typeCombo = new JComboBox<>(HorseType.values());
        JTextField priceField = new JTextField();
        JTextField weightField = new JTextField();
        JComboBox<HorseCondition> statusCombo = new JComboBox<>(HorseCondition.values());

        Object[] message = {
                "Name:", nameField, "Breed:", breedField, "Age:", ageField,
                "Gender:", genderCombo, "Color:", colorField, "Type:", typeCombo,
                "Price:", priceField, "Weight:", weightField, "Status:", statusCombo
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Horse", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                Horse h = new Horse(
                        nameField.getText(),
                        breedField.getText(),
                        (HorseType) typeCombo.getSelectedItem(),
                        Integer.parseInt(ageField.getText()),
                        (Gender) genderCombo.getSelectedItem(),
                        colorField.getText(),
                        Double.parseDouble(weightField.getText()),
                        Double.parseDouble(priceField.getText()),
                        (HorseCondition) statusCombo.getSelectedItem()
                );

                currentStable.addHorse(h);

                // refresh views
                horseModel.setData(currentStable.getHorseList());
                stableModel.fireTableDataChanged(); //update occupancy

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (StableException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Logic Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleNameSearch() {
        if (currentStable == null) return;
        String text = filterField.getText();
        if (text.isEmpty()) {
            horseModel.setData(currentStable.getHorseList());
        } else {
            List<Horse> results = currentStable.searchPartial(text);
            horseModel.setData(results);
        }
    }

    // Filter horses by status
    private void handleStatusFilter() {
        if (currentStable == null) return;
        HorseCondition selectedStatus = (HorseCondition) statusCombo.getSelectedItem();

        List<Horse> filteredList = currentStable.getHorseList().stream()
                .filter(h -> h.getStatus() == selectedStatus)
                .collect(Collectors.toList());

        horseModel.setData(filteredList);
    }

    private void resetFilters() {
        if (currentStable == null) return;
        filterField.setText("");
        horseModel.setData(currentStable.getHorseList());
    }

    // Sort stables by current load
    private void handleSortStables() {
        List<Stable> allStables = new ArrayList<>(manager.getAll());
        // Sorting logic: (current horses / max capacity)
        allStables.sort((s1, s2) -> {
            double load1 = (double) s1.getHorseList().size() / s1.getMaxCapacity();
            double load2 = (double) s2.getHorseList().size() / s2.getMaxCapacity();
            return Double.compare(load2, load1); // Descending order
        });
        stableModel.setData(allStables);
    }

    private void handleLogout() {
        this.dispose();
        new LoginWindow(manager).setVisible(true);
    }
}
