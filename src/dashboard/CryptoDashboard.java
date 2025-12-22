package dashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Timer;
import models.Cryptocurrency;
import services.FileHandler;
import services.APIService;
import components.SidebarPanel;
import components.ModernButton;

public class CryptoDashboard extends JFrame {
    private JTable cryptoTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> sortComboBox;
    private List<Cryptocurrency> cryptoList;
    private Timer refreshTimer;
    private FileHandler fileHandler;
    private APIService apiService;

    public CryptoDashboard() {
        setTitle("Crypto Dashboard - Real-time Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Initialize services
        fileHandler = new FileHandler();
        apiService = new APIService();

        // Load initial data
        cryptoList = fileHandler.loadCryptocurrencies();

        // Setup UI
        setupUI();

        // Start real-time updates
        startRealTimeUpdates();
    }

    private void setupUI() {
        // Main panel with dark theme
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 40));

        // Sidebar
        SidebarPanel sidebar = new SidebarPanel(this);
        mainPanel.add(sidebar, BorderLayout.WEST);

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(30, 30, 40));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = createHeaderPanel();
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Table panel
        JPanel tablePanel = createTablePanel();
        contentPanel.add(new JScrollPane(tablePanel), BorderLayout.CENTER);

        // Stats panel
        JPanel statsPanel = createStatsPanel();
        contentPanel.add(statsPanel, BorderLayout.SOUTH);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Title
        JLabel titleLabel = new JLabel("Cryptocurrency Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(220, 220, 220));

        // Controls panel
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlsPanel.setBackground(new Color(40, 40, 50));

        // Search field
        searchField = new JTextField(20);
        searchField.setBackground(new Color(50, 50, 60));
        searchField.setForeground(Color.white);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 80)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable();
            }
        });

        // Sort combo box
        sortComboBox = new JComboBox<>(new String[]{"Sort by Name", "Sort by Price", "Sort by Change"});
        sortComboBox.setBackground(new Color(50, 50, 60));
        sortComboBox.setForeground(Color.black);
        sortComboBox.addActionListener(e -> sortTable());

        // Refresh button
        ModernButton refreshBtn = new ModernButton("Refresh", new Color(70, 130, 180));
        refreshBtn.addActionListener(e -> refreshData());

        controlsPanel.add(new JLabel("Search:"));
        controlsPanel.add(searchField);
        controlsPanel.add(sortComboBox);
        controlsPanel.add(refreshBtn);

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(controlsPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 50));

        // Table model
        String[] columns = {"No", "Name", "Symbol", "Price (USD)", "24h Change", "Status", "Last Updated", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Only actions column is editable
            }
        };

        cryptoTable = new JTable(tableModel);
        cryptoTable.setBackground(new Color(40, 40, 50));
        cryptoTable.setForeground(new Color(220, 220, 220));
        cryptoTable.setRowHeight(40);
        cryptoTable.getTableHeader().setBackground(new Color(50, 50, 60));
        cryptoTable.getTableHeader().setForeground(Color.black);
        cryptoTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Add action buttons to table
        cryptoTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        cryptoTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JCheckBox()));

        // Populate table
        refreshTableData();

        JScrollPane scrollPane = new JScrollPane(cryptoTable);
        scrollPane.getViewport().setBackground(new Color(40, 40, 50));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 70)));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 0));
        panel.setBackground(new Color(30, 30, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Stats cards
        String[] statsLabels = {"Total Cryptos", "24h Volume", "Market Cap", "Active Pairs"};
        String[] statsValues = {"1500+", "$45.2B", "$1.7T", "45,000+"};
        Color[] colors = {
                new Color(70, 130, 180),
                new Color(50, 205, 50),
                new Color(255, 140, 0),
                new Color(138, 43, 226)
        };

        for (int i = 0; i < 4; i++) {
            JPanel card = createStatCard(statsLabels[i], statsValues[i], colors[i]);
            panel.add(card);
        }

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(40, 40, 50));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 70)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(new Color(180, 180, 180));
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(color);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void refreshTableData() {
        tableModel.setRowCount(0);

        for (int i = 0; i < cryptoList.size(); i++) {
            Cryptocurrency crypto = cryptoList.get(i);
            Object[] row = {
                    i + 1,
                    crypto.getName(),
                    crypto.getSymbol(),
                    crypto.getFormattedPrice(),
                    crypto.getFormattedChange(),
                    crypto.getStatus(),
                    crypto.getLastUpdated().toString(),
                    "View Chart"
            };
            tableModel.addRow(row);
        }
    }

    private void filterTable() {
        String query = searchField.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        cryptoTable.setRowSorter(sorter);

        if (query.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
        }
    }

    private void sortTable() {
        String selected = (String) sortComboBox.getSelectedItem();
        Comparator<Cryptocurrency> comparator = null;

        switch (selected) {
            case "Sort by Name":
                comparator = Comparator.comparing(Cryptocurrency::getName);
                break;
            case "Sort by Price":
                comparator = Comparator.comparingDouble(Cryptocurrency::getCurrentPrice).reversed();
                break;
            case "Sort by Change":
                comparator = Comparator.comparingDouble(Cryptocurrency::getPriceChangePercentage24h).reversed();
                break;
        }

        if (comparator != null) {
            cryptoList.sort(comparator);
            refreshTableData();
        }
    }

    public void refreshDashboardData() {
        System.out.println("Refreshing dashboard data from file...");

        // Reload data dari file
        cryptoList = fileHandler.loadCryptocurrencies();

        // Refresh tabel
        refreshTableData();

        // Update UI
        revalidate();
        repaint();

        System.out.println("Dashboard refreshed. Total cryptos: " + cryptoList.size());
    }

    private void refreshData() {
        // Simulate API call using our simplified APIService
        new Thread(() -> {
            try {
                // Update prices using APIService simulation
                for (Cryptocurrency crypto : cryptoList) {
                    double newPrice = apiService.getCryptoPrice(crypto.getName());
                    double oldPrice = crypto.getCurrentPrice();
                    double change = ((newPrice - oldPrice) / oldPrice) * 100;

                    crypto.setCurrentPrice(newPrice);
                    crypto.setPriceChange24h(newPrice - oldPrice);
                    crypto.setPriceChangePercentage24h(change);
                }

                // Save updated data
                fileHandler.saveCryptocurrencies(cryptoList);

                SwingUtilities.invokeLater(() -> {
                    refreshTableData();
                });

            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "Error refreshing data: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void startRealTimeUpdates() {
        refreshTimer = new Timer();
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refreshData();
            }
        }, 0, 10000); // Update every 10 seconds
    }

    // Navigation methods
    public void showDetailChart() {
        int selectedRow = cryptoTable.getSelectedRow();
        if (selectedRow >= 0) {
            String cryptoName = (String) tableModel.getValueAt(selectedRow, 1);
            DetailChartFrame detailFrame = new DetailChartFrame(cryptoName);
            detailFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a cryptocurrency first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void showDataInput() {
        DataInputFrame inputFrame = new DataInputFrame(this);
        inputFrame.setVisible(true);
    }

    public void showReport() {
        ReportFrame reportFrame = new ReportFrame();
        reportFrame.setVisible(true);
    }

    // Inner classes for table buttons
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(new Color(70, 130, 180));
            setForeground(Color.black);
            setText("View Chart");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            button.setBackground(new Color(70, 130, 180));
            button.setForeground(Color.black);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                showDetailChart();
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}