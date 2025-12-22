package dashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import models.Cryptocurrency;
import services.FileHandler;

public class DataInputFrame extends JFrame {
    private JTextField nameField, symbolField, categoryField;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private List<Cryptocurrency> cryptoList;
    private FileHandler fileHandler;
    private CryptoDashboard parent;
    private JButton addBtn, updateBtn, deleteBtn, clearBtn;

    public DataInputFrame(CryptoDashboard parent) {
        this.parent = parent;
        this.fileHandler = new FileHandler();
        this.cryptoList = fileHandler.loadCryptocurrencies();

        setTitle("Manage Cryptocurrencies");
        setSize(1000, 700);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 40));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel("Manage Cryptocurrencies");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(220, 220, 220));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Input form panel
        JPanel formPanel = createFormPanel();

        // Table panel
        JPanel tablePanel = createTablePanel();

        // Button panel
        JPanel buttonPanel = createButtonPanel();

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
                        BorderFactory.createLineBorder(new Color(70, 70, 80)),
                        "Add/Edit Cryptocurrency",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14),
                        new Color(220, 220, 220)
                ),
                new EmptyBorder(15, 15, 15, 15)
        ));
        panel.setLayout(new GridLayout(4, 2, 10, 15));
        panel.setPreferredSize(new Dimension(350, 0));

        // Labels and fields
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(new Color(220, 220, 220));
        nameField = createTextField();

        JLabel symbolLabel = new JLabel("Symbol:");
        symbolLabel.setForeground(new Color(220, 220, 220));
        symbolField = createTextField();

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setForeground(new Color(220, 220, 220));
        categoryField = createTextField();

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(symbolLabel);
        panel.add(symbolField);
        panel.add(categoryLabel);
        panel.add(categoryField);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 50));
        panel.setBorder(new EmptyBorder(0, 10, 0, 0));

        // Table
        String[] columns = {"ID", "Name", "Symbol", "Category", "Current Price", "Last Updated"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        dataTable = new JTable(tableModel);
        dataTable.setBackground(new Color(40, 40, 50));
        dataTable.setForeground(new Color(220, 220, 220));
        dataTable.setRowHeight(35);
        dataTable.getTableHeader().setBackground(new Color(50, 50, 60));
        dataTable.getTableHeader().setForeground(Color.black);
        dataTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Add selection listener
        dataTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = dataTable.getSelectedRow();
                if (selectedRow >= 0) {
                    loadSelectedCrypto(selectedRow);
                }
            }
        });

        // Load data into table
        refreshTable();

        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.getViewport().setBackground(new Color(40, 40, 50));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 70)));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(new Color(30, 30, 40));
        panel.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Create buttons with colors
        addBtn = createStyledButton("Add New", new Color(50, 205, 50));
        updateBtn = createStyledButton("Update", new Color(255, 140, 0));
        deleteBtn = createStyledButton("Delete", new Color(220, 20, 60));
        clearBtn = createStyledButton("Clear Form", new Color(100, 100, 120));

        // Add action listeners
        addBtn.addActionListener(e -> addCrypto());
        updateBtn.addActionListener(e -> updateCrypto());
        deleteBtn.addActionListener(e -> deleteCrypto());
        clearBtn.addActionListener(e -> clearForm());

        panel.add(addBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);
        panel.add(clearBtn);

        return panel;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setBackground(new Color(50, 50, 60));
        field.setForeground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 80)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.black);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        for (Cryptocurrency crypto : cryptoList) {
            Object[] row = {
                    crypto.getId(),
                    crypto.getName(),
                    crypto.getSymbol(),
                    crypto.getCategory(),
                    crypto.getFormattedPrice(),
                    crypto.getLastUpdated().toString()
            };
            tableModel.addRow(row);
        }
    }

    private void loadSelectedCrypto(int row) {
        if (row >= 0 && row < cryptoList.size()) {
            Cryptocurrency crypto = cryptoList.get(row);
            nameField.setText(crypto.getName());
            symbolField.setText(crypto.getSymbol());
            categoryField.setText(crypto.getCategory());
        }
    }

    private void addCrypto() {
        try {
            String name = nameField.getText().trim();
            String symbol = symbolField.getText().trim().toUpperCase();
            String category = categoryField.getText().trim();

            // Validation
            if (name.isEmpty() || symbol.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Check for duplicate symbol
            for (Cryptocurrency crypto : cryptoList) {
                if (crypto.getSymbol().equalsIgnoreCase(symbol)) {
                    JOptionPane.showMessageDialog(this, "Cryptocurrency with this symbol already exists.",
                            "Duplicate Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Create new cryptocurrency
            String id = "CRYPTO_" + System.currentTimeMillis();
            Cryptocurrency newCrypto = new Cryptocurrency(id, name, symbol, category);

            // Set initial random price
            newCrypto.setCurrentPrice(10000 + Math.random() * 50000);
            newCrypto.setPriceChangePercentage24h((Math.random() - 0.5) * 10);

            cryptoList.add(newCrypto);
            fileHandler.saveCryptocurrencies(cryptoList);

            refreshTable();
            clearForm();

            JOptionPane.showMessageDialog(this, "Cryptocurrency added successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding cryptocurrency: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCrypto() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a cryptocurrency to update.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Cryptocurrency crypto = cryptoList.get(selectedRow);
            crypto.setName(nameField.getText().trim());
            crypto.setSymbol(symbolField.getText().trim().toUpperCase());
            crypto.setCategory(categoryField.getText().trim());

            fileHandler.saveCryptocurrencies(cryptoList);
            refreshTable();

            JOptionPane.showMessageDialog(this, "Cryptocurrency updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating cryptocurrency: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCrypto() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a cryptocurrency to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this cryptocurrency?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                cryptoList.remove(selectedRow);
                fileHandler.saveCryptocurrencies(cryptoList);
                refreshTable();
                clearForm();

                JOptionPane.showMessageDialog(this, "Cryptocurrency deleted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting cryptocurrency: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        nameField.setText("");
        symbolField.setText("");
        categoryField.setText("");
        dataTable.clearSelection();
    }
}