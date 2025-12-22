package dashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import models.Cryptocurrency;
import services.FileHandler;

public class ReportFrame extends JFrame {
    private JTable historyTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> cryptoFilterCombo;
    private JComboBox<String> dateFilterCombo;
    private JTextArea summaryArea;
    private FileHandler fileHandler;
    private List<Cryptocurrency> cryptoList;

    public ReportFrame() {
        this.fileHandler = new FileHandler();
        this.cryptoList = fileHandler.loadCryptocurrencies();

        setTitle("Cryptocurrency Report & History");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadReportData();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 40));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header
        JPanel headerPanel = createHeaderPanel();

        // Content panel with two sections
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);
        splitPane.setDividerSize(3);

        // Left panel - Charts and Summary
        JPanel leftPanel = createLeftPanel();

        // Right panel - History Table
        JPanel rightPanel = createRightPanel();

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 50));
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Title
        JLabel titleLabel = new JLabel("Cryptocurrency Reports & History");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(220, 220, 220));

        // Filter controls
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setBackground(new Color(40, 40, 50));

        cryptoFilterCombo = new JComboBox<>();
        cryptoFilterCombo.addItem("All Cryptocurrencies");
        for (Cryptocurrency crypto : cryptoList) {
            cryptoFilterCombo.addItem(crypto.getName() + " (" + crypto.getSymbol() + ")");
        }
        cryptoFilterCombo.setBackground(new Color(50, 50, 60));
        cryptoFilterCombo.setForeground(Color.black);
        cryptoFilterCombo.addActionListener(e -> filterHistory());

        dateFilterCombo = new JComboBox<>(new String[]{
                "Today", "Last 7 Days", "Last 30 Days", "All Time"
        });
        dateFilterCombo.setBackground(new Color(50, 50, 60));
        dateFilterCombo.setForeground(Color.black);
        dateFilterCombo.addActionListener(e -> filterHistory());

        JButton exportBtn = new JButton("Export Report");
        exportBtn.setBackground(new Color(70, 130, 180));
        exportBtn.setForeground(Color.black);
        exportBtn.addActionListener(e -> exportReport());

        filterPanel.add(new JLabel("Filter by:"));
        filterPanel.add(cryptoFilterCombo);
        filterPanel.add(dateFilterCombo);
        filterPanel.add(exportBtn);

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(filterPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 50));
        panel.setBorder(new EmptyBorder(0, 0, 0, 10));

        // Summary panel
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBackground(new Color(40, 40, 50));
        summaryPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 80)),
                "Market Summary",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(220, 220, 220)
        ));

        summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setBackground(new Color(50, 50, 60));
        summaryArea.setForeground(new Color(220, 220, 220));
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        summaryArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane summaryScroll = new JScrollPane(summaryArea);
        summaryScroll.setBorder(null);

        summaryPanel.add(summaryScroll, BorderLayout.CENTER);

        // Chart panel
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(new Color(40, 40, 50));
        chartPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 80)),
                "Market Distribution",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(220, 220, 220)
        ));

        // Create pie chart
        ChartPanel pieChartPanel = createPieChart();
        chartPanel.add(pieChartPanel, BorderLayout.CENTER);

        // Add to left panel with split
        JSplitPane leftSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        leftSplit.setDividerLocation(250);
        leftSplit.setDividerSize(3);
        leftSplit.setTopComponent(summaryPanel);
        leftSplit.setBottomComponent(chartPanel);

        panel.add(leftSplit, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 50));

        // Table
        String[] columns = {"Date", "Cryptocurrency", "Price (USD)", "24h Change", "Volume"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        historyTable = new JTable(tableModel);
        historyTable.setBackground(new Color(40, 40, 50));
        historyTable.setForeground(new Color(220, 220, 220));
        historyTable.setRowHeight(35);
        historyTable.getTableHeader().setBackground(new Color(50, 50, 60));
        historyTable.getTableHeader().setForeground(Color.black);
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Add some sample data
        generateSampleHistoryData();

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.getViewport().setBackground(new Color(40, 40, 50));
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 80)),
                "Price History",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(220, 220, 220)
        ));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private ChartPanel createPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Add sample data
        dataset.setValue("Bitcoin", 40.5);
        dataset.setValue("Ethereum", 18.2);
        dataset.setValue("Binance Coin", 8.7);
        dataset.setValue("Cardano", 6.3);
        dataset.setValue("Solana", 5.8);
        dataset.setValue("Others", 20.5);

        JFreeChart chart = ChartFactory.createPieChart(
                "Market Cap Distribution",
                dataset,
                true,
                true,
                false
        );

        // Apply dark theme
        chart.setBackgroundPaint(new Color(40, 40, 50));
        chart.getTitle().setPaint(new Color(220, 220, 220));

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(40, 40, 50));
        plot.setLabelBackgroundPaint(new Color(40, 40, 50));
        plot.setLabelPaint(new Color(220, 220, 220));
        plot.setOutlinePaint(new Color(60, 60, 70));

        // Set colors for each slice
        plot.setSectionPaint("Bitcoin", new Color(247, 147, 26));
        plot.setSectionPaint("Ethereum", new Color(98, 126, 234));
        plot.setSectionPaint("Binance Coin", new Color(240, 185, 11));
        plot.setSectionPaint("Cardano", new Color(0, 130, 206));
        plot.setSectionPaint("Solana", new Color(0, 255, 163));
        plot.setSectionPaint("Others", new Color(128, 128, 128));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(new Color(40, 40, 50));

        return chartPanel;
    }

    private void loadReportData() {
        // Generate summary
        generateSummary();

        // Load history data
        loadHistoryData();
    }

    private void generateSummary() {
        StringBuilder summary = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Calculate totals
        double totalMarketCap = 0;
        double totalVolume = 0;
        int totalCryptos = cryptoList.size();
        int gainers = 0;
        int losers = 0;

        for (Cryptocurrency crypto : cryptoList) {
            totalMarketCap += crypto.getCurrentPrice() * 1000000; // Simplified
            totalVolume += Math.abs(crypto.getPriceChange24h()) * 100000;

            if (crypto.getPriceChangePercentage24h() >= 0) {
                gainers++;
            } else {
                losers++;
            }
        }

        summary.append("=== CRYPTOCURRENCY MARKET REPORT ===\n\n");
        summary.append(String.format("Report Date: %s\n", LocalDateTime.now().format(dateFormatter)));
        summary.append(String.format("Total Cryptocurrencies: %d\n", totalCryptos));
        summary.append(String.format("Market Cap: $%,.2fB\n", totalMarketCap / 1000000000));
        summary.append(String.format("24h Volume: $%,.2fB\n", totalVolume / 1000000000));
        summary.append(String.format("Gainers: %d | Losers: %d\n\n", gainers, losers));
        summary.append("Top Performers (24h):\n");
        summary.append("----------------------\n");

        // Sort by performance
        cryptoList.sort((c1, c2) ->
                Double.compare(c2.getPriceChangePercentage24h(), c1.getPriceChangePercentage24h()));

        for (int i = 0; i < Math.min(5, cryptoList.size()); i++) {
            Cryptocurrency crypto = cryptoList.get(i);
            summary.append(String.format("%s: %+.2f%% ($%,.2f)\n",
                    crypto.getSymbol(),
                    crypto.getPriceChangePercentage24h(),
                    crypto.getCurrentPrice()));
        }

        summaryArea.setText(summary.toString());
    }

    private void loadHistoryData() {
        // Load from file or generate sample
        generateSampleHistoryData();
    }

    private void generateSampleHistoryData() {
        tableModel.setRowCount(0);

        String[] cryptoNames = {"Bitcoin", "Ethereum", "Binance Coin", "Cardano", "Solana"};
        Random random = new Random();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (int i = 0; i < 20; i++) {
            LocalDateTime date = LocalDateTime.now().minusHours(random.nextInt(168)); // Last 7 days
            String crypto = cryptoNames[random.nextInt(cryptoNames.length)];
            double price = 10000 + random.nextDouble() * 50000;
            double change = (random.nextDouble() - 0.5) * 15;
            double volume = 1000000 + random.nextDouble() * 9000000;

            Object[] row = {
                    date.format(formatter),
                    crypto,
                    String.format("$%,.2f", price),
                    String.format("%+.2f%%", change),
                    String.format("$%,.0f", volume)
            };
            tableModel.addRow(row);
        }
    }

    private void filterHistory() {
        String selectedCrypto = (String) cryptoFilterCombo.getSelectedItem();
        String selectedDate = (String) dateFilterCombo.getSelectedItem();

        // In a real application, this would filter the actual data
        // For demo, we'll just regenerate sample data
        generateSampleHistoryData();

        // Update summary based on filter
        generateSummary();
    }

    private void exportReport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Report");
        fileChooser.setSelectedFile(new File("crypto_report_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                // Export summary and history to file
                // Implementation would write data to selected file
                JOptionPane.showMessageDialog(this, "Report exported successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error exporting report: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}