package dashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import models.Cryptocurrency;
import services.FileHandler;
import services.APIService;

public class DataInputFrame extends JFrame {
    private JTextField nameField, symbolField, categoryField;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private List<Cryptocurrency> cryptoList;
    private FileHandler fileHandler;
    private CryptoDashboard parent;
    private JButton addBtn, updateBtn, deleteBtn, clearBtn;
    private int editingIndex = -1;
    private APIService apiService;
    private JLabel statusLabel;

    public DataInputFrame(CryptoDashboard parent) {
        this.parent = parent;
        this.fileHandler = new FileHandler();
        this.apiService = new APIService();

        System.out.println("\n=== DATA INPUT FRAME INITIALIZED ===");

        // Load data dari file
        this.cryptoList = fileHandler.loadCryptocurrencies();
        System.out.println("Loaded " + cryptoList.size() + " cryptocurrencies from file");

        setTitle("Manage Cryptocurrencies - CRUD");
        setSize(1000, 700);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
    } //

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 40));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel("CRUD - Manage Cryptocurrencies");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(220, 220, 220));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Input form panel
        JPanel formPanel = createFormPanel();

        // Table panel
        JPanel tablePanel = createTablePanel();

        // Button panel
        JPanel buttonPanel = createButtonPanel();

        // Status bar
        JPanel statusPanel = createStatusPanel();

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(40, 40, 50));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(70, 130, 180)),
                        "ADD / EDIT CRYPTOCURRENCY",
                        TitledBorder.CENTER,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14),
                        new Color(220, 220, 220)
                ),
                new EmptyBorder(15, 15, 15, 15)
        ));
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(350, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 15, 5);

        // Name Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(new Color(220, 220, 220));
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nameField = createTextField();
        nameField.setToolTipText("Enter cryptocurrency name (e.g., Bitcoin)");
        panel.add(nameField, gbc);

        // Symbol Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel symbolLabel = new JLabel("Symbol:");
        symbolLabel.setForeground(new Color(220, 220, 220));
        symbolLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(symbolLabel, gbc);

        gbc.gridx = 1;
        symbolField = createTextField();
        symbolField.setToolTipText("Enter 3-5 letter symbol (e.g., BTC)");
        panel.add(symbolField, gbc);

        // Category Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setForeground(new Color(220, 220, 220));
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(categoryLabel, gbc);

        gbc.gridx = 1;
        categoryField = createTextField();
        categoryField.setToolTipText("Enter category (e.g., Currency, Platform, Exchange)");
        panel.add(categoryField, gbc);

        // Info label
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("* Required fields");
        infoLabel.setForeground(new Color(150, 150, 150));
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panel.add(infoLabel, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 50));
        panel.setBorder(new EmptyBorder(0, 10, 0, 0));

        // Table header with refresh button
        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(new Color(40, 40, 50));
        tableHeader.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel tableTitle = new JLabel("CRYPTOCURRENCY LIST");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(new Color(220, 220, 220));

        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setBackground(new Color(70, 130, 180));
        refreshBtn.setForeground(Color.BLACK);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        refreshBtn.addActionListener(e -> refreshTable());

        tableHeader.add(tableTitle, BorderLayout.WEST);
        tableHeader.add(refreshBtn, BorderLayout.EAST);

        // Table
        String[] columns = {"No", "Name", "Symbol", "Category", "Price (USD)", "24h Change", "Last Updated"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 5) return String.class; // 24h Change
                return Object.class;
            }
        };

        dataTable = new JTable(tableModel);
        dataTable.setBackground(new Color(40, 40, 50));
        dataTable.setForeground(new Color(220, 220, 220));
        dataTable.setRowHeight(35);
        dataTable.getTableHeader().setBackground(new Color(50, 50, 60));
        dataTable.getTableHeader().setForeground(Color.BLACK);
        dataTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Set column widths
        dataTable.getColumnModel().getColumn(0).setPreferredWidth(40);  // No
        dataTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
        dataTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // Symbol
        dataTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Category
        dataTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Price
        dataTable.getColumnModel().getColumn(5).setPreferredWidth(90);  // 24h Change
        dataTable.getColumnModel().getColumn(6).setPreferredWidth(150); // Last Updated

        // Custom cell renderer for price change
        dataTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (value != null) {
                    String change = value.toString();
                    if (change.contains("+")) {
                        c.setForeground(new Color(0, 200, 0)); // Green for positive
                        setText("<html><b>" + change + "</b></html>");
                    } else if (change.contains("-")) {
                        c.setForeground(new Color(220, 0, 0)); // Red for negative
                        setText("<html><b>" + change + "</b></html>");
                    }
                }

                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        // Add selection listener
        dataTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = dataTable.getSelectedRow();
                if (selectedRow >= 0) {
                    loadSelectedCrypto(selectedRow);
                    updateStatus("Selected: " + cryptoList.get(selectedRow).getName());
                }
            }
        });

        // Double-click to edit
        dataTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = dataTable.getSelectedRow();
                    if (row >= 0) {
                        loadSelectedCrypto(row);
                        updateStatus("Ready to edit: " + cryptoList.get(row).getName());
                    }
                }
            }
        });

        // Load data into table
        refreshTable();

        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.getViewport().setBackground(new Color(40, 40, 50));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 70)));

        panel.add(tableHeader, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(new Color(30, 30, 40));
        panel.setBorder(new EmptyBorder(20, 0, 0, 0));

        // HAPUS EMOJI dari semua button
        addBtn = createStyledButton("Add New", new Color(50, 205, 50), Color.BLACK);
        updateBtn = createStyledButton("Update", new Color(255, 140, 0), Color.BLACK);
        deleteBtn = createStyledButton("Delete", new Color(220, 20, 60), Color.BLACK);
        clearBtn = createStyledButton("Clear Form", new Color(100, 100, 120), Color.BLACK);

        // Initially disable update and delete buttons
        updateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        // Add action listeners
        addBtn.addActionListener(e -> addCrypto());
        updateBtn.addActionListener(e -> updateCrypto());
        deleteBtn.addActionListener(e -> deleteCrypto());
        clearBtn.addActionListener(e -> clearForm());

        // Tooltips
        addBtn.setToolTipText("Add new cryptocurrency to database");
        updateBtn.setToolTipText("Update selected cryptocurrency");
        deleteBtn.setToolTipText("Delete selected cryptocurrency");
        clearBtn.setToolTipText("Clear form and deselect");

        panel.add(addBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);
        panel.add(clearBtn);

        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        statusLabel = new JLabel(" Ready - Total: " + cryptoList.size() + " cryptocurrencies");
        statusLabel.setForeground(new Color(180, 180, 180));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        panel.add(statusLabel, BorderLayout.WEST);
        return panel;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200, 40));
        field.setBackground(new Color(50, 50, 60));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 80)),
                BorderFactory.createEmptyBorder(8, 12, 8, 10)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        return field;
    }

    private JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker()),
                BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(bgColor.brighter().darker()),
                        BorderFactory.createEmptyBorder(10, 25, 10, 25)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(bgColor.darker()),
                        BorderFactory.createEmptyBorder(10, 25, 10, 25)
                ));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(bgColor.darker());
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(bgColor.brighter());
                button.setForeground(textColor);
            }
        });

        return button;
    }

    private void refreshTable() {
        System.out.println("\n=== REFRESHING TABLE ===");

        // Reload data from file
        cryptoList = fileHandler.loadCryptocurrencies();
        System.out.println("Reloaded " + cryptoList.size() + " cryptocurrencies");

        tableModel.setRowCount(0);

        for (int i = 0; i < cryptoList.size(); i++) {
            Cryptocurrency crypto = cryptoList.get(i);

            // Format change with color indicator
            String change = crypto.getFormattedChange();
            String changeDisplay = change;
            if (change.contains("+")) {
                changeDisplay = "▲ " + change;
            } else if (change.contains("-")) {
                changeDisplay = "▼ " + change;
            }

            Object[] row = {
                    i + 1,
                    crypto.getName(),
                    crypto.getSymbol(),
                    crypto.getCategory(),
                    crypto.getFormattedPrice(),
                    changeDisplay,
                    crypto.getLastUpdated().toString().substring(0, 19).replace("T", " ")
            };
            tableModel.addRow(row);
        }

        updateStatus("Table refreshed - Total: " + cryptoList.size() + " cryptocurrencies");
        System.out.println("Table updated with " + cryptoList.size() + " rows");
    }

    private void loadSelectedCrypto(int row) {
        if (row >= 0 && row < cryptoList.size()) {
            editingIndex = row;
            Cryptocurrency crypto = cryptoList.get(row);

            nameField.setText(crypto.getName());
            symbolField.setText(crypto.getSymbol());
            categoryField.setText(crypto.getCategory());

            // Enable update and delete buttons
            updateBtn.setEnabled(true);
            deleteBtn.setEnabled(true);
            addBtn.setEnabled(false); // Disable add when editing

            System.out.println("Loaded for editing: " + crypto.getName() + " (" + crypto.getSymbol() + ")");
        }
    }

    private void addCrypto() {
        System.out.println("\n=== ATTEMPTING TO ADD CRYPTO ===");

        String name = nameField.getText().trim();
        String symbol = symbolField.getText().trim().toUpperCase();
        String category = categoryField.getText().trim();

        System.out.println("Input - Name: '" + name + "', Symbol: '" + symbol + "', Category: '" + category + "'");

        // Validation
        if (name.isEmpty() || symbol.isEmpty() || category.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    " Please fill in ALL required fields!\n\n" +
                            "• Name: " + (name.isEmpty() ? "EMPTY" : "OK") + "\n" +
                            "• Symbol: " + (symbol.isEmpty() ? "EMPTY" : "OK") + "\n" +
                            "• Category: " + (category.isEmpty() ? "EMPTY" : "OK"),
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            updateStatus("Add failed: Please fill all fields");
            return;
        }

        // Validate symbol length
        if (symbol.length() < 2 || symbol.length() > 5) {
            JOptionPane.showMessageDialog(this,
                    " Symbol must be 2-5 characters!\n\n" +
                            "Current: '" + symbol + "' (" + symbol.length() + " characters)",
                    "Invalid Symbol",
                    JOptionPane.WARNING_MESSAGE);
            symbolField.requestFocus();
            symbolField.selectAll();
            return;
        }

        // Check for duplicate symbol
        for (Cryptocurrency crypto : cryptoList) {
            if (crypto.getSymbol().equalsIgnoreCase(symbol)) {
                JOptionPane.showMessageDialog(this,
                        " Cryptocurrency with symbol '" + symbol + "' already exists!\n\n" +
                                "Existing: " + crypto.getName() + " (" + crypto.getSymbol() + ")\n" +
                                "Please use a different symbol.",
                        "Duplicate Symbol",
                        JOptionPane.ERROR_MESSAGE);
                symbolField.requestFocus();
                symbolField.selectAll();
                updateStatus("Add failed: Duplicate symbol '" + symbol + "'");
                return;
            }
        }

        // Create new cryptocurrency
        String id = symbol + "-" + System.currentTimeMillis();
        Cryptocurrency newCrypto = new Cryptocurrency(id, name, symbol, category);

        // Set initial price from API service
        double initialPrice = apiService.getCryptoPrice(name);
        newCrypto.setCurrentPrice(initialPrice);

        // Set initial change
        double initialChange = (Math.random() - 0.5) * 10;
        newCrypto.setPriceChangePercentage24h(initialChange);
        newCrypto.setPriceChange24h(initialPrice * (initialChange / 100));

        System.out.println("Creating: " + name + " (" + symbol + ") - Price: $" + initialPrice);

        // Add to list
        cryptoList.add(newCrypto);
        System.out.println("Added to list. New size: " + cryptoList.size());

        // Save to file
        boolean saved = fileHandler.saveCryptocurrencies(cryptoList);

        if (saved) {
            System.out.println("✓ SAVE SUCCESSFUL!");

            // Refresh table
            refreshTable();
            clearForm();

            // Refresh parent dashboard
            if (parent != null) {
                parent.refreshDashboardData();
                System.out.println("✓ Dashboard refreshed");
            }

            // Show success message
            JOptionPane.showMessageDialog(this,
                    " CRYPTOCURRENCY ADDED SUCCESSFULLY!\n\n" +
                            "Name: " + name + "\n" +
                            "Symbol: " + symbol + "\n" +
                            "Category: " + category + "\n" +
                            "Initial Price: " + newCrypto.getFormattedPrice() + "\n" +
                            "24h Change: " + newCrypto.getFormattedChange() + "\n\n" +
                            "Total cryptocurrencies: " + cryptoList.size(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            updateStatus("Added: " + name + " (" + symbol + ")");

        } else {
            System.err.println("✗ SAVE FAILED!");

            // Remove from list if save failed
            cryptoList.remove(newCrypto);

            JOptionPane.showMessageDialog(this,
                    " FAILED TO SAVE CRYPTOCURRENCY!\n\n" +
                            "Could not write to data file.\n" +
                            "Check console for error details.",
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);

            updateStatus("Save failed: Check file permissions");
        }
    }

    private void updateCrypto() {
        if (editingIndex < 0 || editingIndex >= cryptoList.size()) {
            JOptionPane.showMessageDialog(this,
                    "Please SELECT a cryptocurrency to update.\n" +
                            "Click on a row in the table first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            updateStatus("Update failed: No selection");
            return;
        }

        System.out.println("\n=== ATTEMPTING TO UPDATE CRYPTO ===");

        try {
            String name = nameField.getText().trim();
            String symbol = symbolField.getText().trim().toUpperCase();
            String category = categoryField.getText().trim();

            System.out.println("Editing index: " + editingIndex);
            System.out.println("New values - Name: '" + name + "', Symbol: '" + symbol + "', Category: '" + category + "'");

            // Validation
            if (name.isEmpty() || symbol.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill in ALL required fields!",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Cryptocurrency crypto = cryptoList.get(editingIndex);
            System.out.println("Updating: " + crypto.getName() + " (" + crypto.getSymbol() + ")");

            // Check for duplicate symbol (only if symbol changed)
            if (!crypto.getSymbol().equalsIgnoreCase(symbol)) {
                for (Cryptocurrency c : cryptoList) {
                    if (c != crypto && c.getSymbol().equalsIgnoreCase(symbol)) {
                        JOptionPane.showMessageDialog(this,
                                "Cryptocurrency with symbol '" + symbol + "' already exists!\n\n" +
                                        "Existing: " + c.getName() + " (" + c.getSymbol() + ")\n" +
                                        "Please use a different symbol.",
                                "Duplicate Symbol",
                                JOptionPane.ERROR_MESSAGE);
                        symbolField.requestFocus();
                        symbolField.selectAll();
                        return;
                    }
                }
            }

            // Save old values for message
            String oldName = crypto.getName();
            String oldSymbol = crypto.getSymbol();

            // Update crypto data
            crypto.setName(name);
            crypto.setSymbol(symbol);
            crypto.setCategory(category);

            System.out.println("Updated: " + oldName + " -> " + name + ", " + oldSymbol + " -> " + symbol);

            // Save to file
            boolean saved = fileHandler.saveCryptocurrencies(cryptoList);

            if (saved) {
                System.out.println("✓ UPDATE SAVED SUCCESSFULLY!");

                // Refresh table
                refreshTable();
                clearForm();

                // Refresh parent dashboard
                if (parent != null) {
                    parent.refreshDashboardData();
                    System.out.println("✓ Dashboard refreshed");
                }

                // Show success message
                JOptionPane.showMessageDialog(this,
                        "CRYPTOCURRENCY UPDATED SUCCESSFULLY!\n\n" +
                                "Old: " + oldName + " (" + oldSymbol + ")\n" +
                                "New: " + name + " (" + symbol + ")\n" +
                                "Category: " + category + "\n" +
                                "Price: " + crypto.getFormattedPrice(),
                        "Update Success",
                        JOptionPane.INFORMATION_MESSAGE);

                updateStatus("Updated: " + name + " (" + symbol + ")");

            } else {
                System.err.println("✗ UPDATE SAVE FAILED!");

                // Revert changes if save failed
                crypto.setName(oldName);
                crypto.setSymbol(oldSymbol);

                JOptionPane.showMessageDialog(this,
                        "FAILED TO SAVE CHANGES!\n\n" +
                                "Could not write to data file.\n" +
                                "Original data restored.",
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE);

                updateStatus("Update failed: Could not save");
            }

        } catch (Exception e) {
            System.err.println("✗ ERROR updating: " + e.getMessage());
            e.printStackTrace();

            JOptionPane.showMessageDialog(this,
                    "ERROR UPDATING CRYPTOCURRENCY!\n\n" +
                            "Error: " + e.getMessage(),
                    "Update Error",
                    JOptionPane.ERROR_MESSAGE);

            updateStatus("Update error: " + e.getMessage());
        }
    }

    private void deleteCrypto() {
        if (editingIndex < 0 || editingIndex >= cryptoList.size()) {
            JOptionPane.showMessageDialog(this,
                    "Please SELECT a cryptocurrency to delete.\n" +
                            "Click on a row in the table first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            updateStatus("Delete failed: No selection");
            return;
        }

        Cryptocurrency cryptoToDelete = cryptoList.get(editingIndex);

        System.out.println("\n=== ATTEMPTING TO DELETE CRYPTO ===");
        System.out.println("To delete: " + cryptoToDelete.getName() + " (" + cryptoToDelete.getSymbol() + ")");
        System.out.println("Current list size: " + cryptoList.size());

        int confirm = JOptionPane.showConfirmDialog(this,
                "CONFIRM DELETE\n\n" +
                        "Are you sure you want to PERMANENTLY DELETE:\n\n" +
                        "● Name: " + cryptoToDelete.getName() + "\n" +
                        "● Symbol: " + cryptoToDelete.getSymbol() + "\n" +
                        "● Category: " + cryptoToDelete.getCategory() + "\n" +
                        "● Price: " + cryptoToDelete.getFormattedPrice() + "\n\n" +
                        "This action CANNOT be undone!",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Remove from list
                cryptoList.remove(editingIndex);
                System.out.println("Removed from list. New size: " + cryptoList.size());

                // Save to file
                boolean saved = fileHandler.saveCryptocurrencies(cryptoList);

                if (saved) {
                    System.out.println("✓ DELETE SAVED SUCCESSFULLY!");

                    // Refresh table
                    refreshTable();
                    clearForm();

                    // Refresh parent dashboard
                    if (parent != null) {
                        parent.refreshDashboardData();
                        System.out.println("✓ Dashboard refreshed");
                    }

                    JOptionPane.showMessageDialog(this,
                            "CRYPTOCURRENCY DELETED SUCCESSFULLY!\n\n" +
                                    "Deleted: " + cryptoToDelete.getName() + " (" + cryptoToDelete.getSymbol() + ")\n" +
                                    "Remaining: " + cryptoList.size() + " cryptocurrencies",
                            "Delete Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    updateStatus("Deleted: " + cryptoToDelete.getName() + " (" + cryptoToDelete.getSymbol() + ")");

                } else {
                    System.err.println("✗ DELETE SAVE FAILED!");

                    // Restore to list if save failed
                    cryptoList.add(editingIndex, cryptoToDelete);

                    JOptionPane.showMessageDialog(this,
                            "FAILED TO DELETE FROM FILE!\n\n" +
                                    "Could not write to data file.\n" +
                                    "Cryptocurrency restored to list.",
                            "Delete Error",
                            JOptionPane.ERROR_MESSAGE);

                    updateStatus("Delete failed: Could not save");
                }

            } catch (Exception e) {
                System.err.println("✗ ERROR deleting: " + e.getMessage());
                e.printStackTrace();

                JOptionPane.showMessageDialog(this,
                        "ERROR DELETING CRYPTOCURRENCY!\n\n" +
                                "Error: " + e.getMessage(),
                        "Delete Error",
                        JOptionPane.ERROR_MESSAGE);

                updateStatus("Delete error: " + e.getMessage());
            }
        } else {
            System.out.println("Delete cancelled by user");
            updateStatus("Delete cancelled");
        }
    }

    private void clearForm() {
        nameField.setText("");
        symbolField.setText("");
        categoryField.setText("");
        dataTable.clearSelection();
        editingIndex = -1;

        // Reset button states
        addBtn.setEnabled(true);
        updateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        // Set focus to name field
        nameField.requestFocus();

        updateStatus("Form cleared - Ready for new entry");
        System.out.println("Form cleared");
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(" " + message + " | Total: " + cryptoList.size() + " cryptocurrencies");
        }
    }

    // Public method untuk refresh dari external
    public void refreshData() {
        refreshTable();
    }
}